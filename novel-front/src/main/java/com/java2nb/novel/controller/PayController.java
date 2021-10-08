package com.java2nb.novel.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.java2nb.novel.core.bean.ResultBean;
import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.config.AlipayProperties;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.core.exception.BusinessException;
import com.java2nb.novel.service.BlockchainService;
import com.java2nb.novel.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author 11797
 * 增加 blockchainService 用于区块链账户的充值
 */
@Controller
@RequestMapping("pay")
@RequiredArgsConstructor
@Slf4j
public class PayController extends BaseController {


    private final AlipayProperties alipayConfig;

    private final OrderService orderService;


    /**
     * 支付宝支付
     */
    @SneakyThrows
    @PostMapping("aliPay")
    public void aliPay(Integer payAmount,HttpServletRequest request,HttpServletResponse httpResponse) {

        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            //未登录，跳转到登陆页面
            httpResponse.sendRedirect("/user/login.html?originUrl=/pay/aliPay?payAmount="+payAmount);
            return;
        }else {
            //创建内部充值订单号
            Long outTradeNo = orderService.createPayOrder((byte)1,payAmount,userDetails.getId());

            httpResponse.sendRedirect("/pay/aliPay/notify?out_trade_no="+outTradeNo);

            // //获得初始化的AlipayClient
            // AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getGatewayUrl(), alipayConfig.getAppId(), alipayConfig.getMerchantPrivateKey(), "json", alipayConfig.getCharset(), alipayConfig.getPublicKey(), alipayConfig.getSignType());
            // //创建API对应的request
            // AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            // alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());
            // //在公共参数中设置回跳和通知地址
            // alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());
            // //填充业务参数
            // alipayRequest.setBizContent("{" +
            //         "    \"out_trade_no\":\"" + outTradeNo + "\"," +
            //         "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
            //         "    \"total_amount\":" + payAmount + "," +
            //         "    \"subject\":\"小说精品屋-plus\"" +
            //         "  }");
            // //调用SDK生成表单
            // String form = alipayClient.pageExecute(alipayRequest).getBody();

            // httpResponse.setContentType("text/html;charset=utf-8");
            // //直接将完整的表单html输出到页面
            // httpResponse.getWriter().write(form);
            // httpResponse.getWriter().flush();
            // httpResponse.getWriter().close();
        }




    }

    /**
     * 支付宝支付通知
     * */
    @SneakyThrows
    @RequestMapping("aliPay/notify")
    public void aliPayNotify(HttpServletRequest request,HttpServletResponse httpResponse){

        PrintWriter out = httpResponse.getWriter();

         //获取支付宝POST过来反馈信息
         Map<String,String> params = new HashMap<String,String>();
         Map<String,String[]> requestParams = request.getParameterMap();
         for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
             String name = (String) iter.next();
             String[] values = (String[]) requestParams.get(name);
             String valueStr = "";
             for (int i = 0; i < values.length; i++) {
                 valueStr = (i == values.length - 1) ? valueStr + values[i]
                         : valueStr + values[i] + ",";
             }
             params.put(name, valueStr);
         }

        // //调用SDK验证签名
         boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getPublicKey(), alipayConfig.getCharset(), alipayConfig.getSignType());
//        boolean signVerified = true;

        //——请在这里编写您的程序（以下代码仅作参考）——

	/* 实际验证过程建议商户务必添加以下校验：
	1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
	4、验证app_id是否为该商户本身。
	*/
        if(signVerified) {
            //验证成功
            //商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
             String tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");


            //交易状态
             String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

            //更新订单状态
            orderService.updatePayOrder(Long.parseLong(outTradeNo), tradeNo, tradeStatus);

            out.println("success");

            httpResponse.sendRedirect("/pay");

        }else {//验证失败
            out.println("fail");

            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);
        }

    }

    /**
     * 区块链支付第一步
     * 1. 根据用户信息，生成充值单号；
     * 2. 跳转到 blockchainPay.html 页面，要求用户输入支付密码，或者生成rawTransaction；
     * 3. 从 blockchainPay.html 页面提交相应信息到 blockchainPay/notify
     */
    @SneakyThrows
    @PostMapping("blockchainPay")
    public void blockchainPay(Integer payAmount,HttpServletRequest request,HttpServletResponse httpResponse) {

        // 需要检查payAmount是否为大于0的整数值
        if ( payAmount > 0){
            UserDetails userDetails = getUserDetails(request);
            //debuging to show user name
            log.info("blockchainPay： " + userDetails.getUsername(), "支付数额： ", payAmount);

            if (userDetails == null) {
                //未登录，跳转到登陆页面
                httpResponse.sendRedirect("/user/login.html?originUrl=/pay/blockchainPay?payAmount=" + payAmount);
                return;
            } else {
                //创建充值订单
                Long outTradeNo = orderService.createPayOrder((byte) 1, payAmount, userDetails.getId());
                log.debug("blockchainPay 创建充值订单:", outTradeNo, "支付数额： ", payAmount, "账户：", userDetails.getAccountAddress());
    //            httpResponse.sendRedirect("/pay/blockchainPay/notify?out_trade_no="+outTradeNo);

                httpResponse.sendRedirect("/pay/blockchainPay.html?payAmount=" + payAmount + "&outTradeNo=" + outTradeNo);

            }
        }else{
            //
            throw new BusinessException(ResponseStatus.BLOCKCHAIN_PARAM_ERROR);
        }

        return;

    }

    /**
     * 区块链充值过程
     * 根据用户输入的keystore密码，或者已经签名的交易
     * 提交并等待Receipt，然后提交buyRecord到数据库
     * 需要显示用户的区块链钱包信息，并要求输入
     * TODO 如果是用MetaMask，那么需要签名并发送交易
     * TODO 需异步等待并返回结果
     * */
    @SneakyThrows
    @PostMapping("blockchainPaySubmitTx")
    public ResultBean blockchainPayNotify(HttpServletRequest request,HttpServletResponse httpResponse){

        UserDetails userDetails = getUserDetails(request);

        // 用于检测输入的
        boolean signedTx = false;
        String txReceipt = null;

//        String txReceipt = request.getParameter("txReceipt");

        String toAddress = "0x7312F4B8A4457a36827f185325Fd6B66a3f8BB8B";//系统固定钱包，将来需要作为设置，从xml文件中读取
//
        String inputPassword = request.getParameter("password");

        Integer payAmount = new Integer(request.getParameter("amount"));

        //商户订单号
        String outTradeNo = new String(request.getParameter("tradeNumber").getBytes("ISO-8859-1"), "UTF-8");
        log.info("Get inputs: password,", txReceipt, "trade_no",outTradeNo);
        // TODO 将来可以使用用户已经签名的交易。


        if (!signedTx) {
            //验证成功

            log.debug("blockchainPay 获得充值订单:", outTradeNo, "支付数额： ", payAmount, "账户：", userDetails.getAccountAddress());
            //使用区块链交易HASH代替中心化的交易单号，
            //需确认交易成功，检测getTransactionReceipt，并获取状态
            txReceipt = orderService.submitPayOrder(userDetails.getId(), userDetails.getAccountAddress(), toAddress, payAmount, inputPassword);
        }else{
            // 直接提交由用户签名后的RawTransaction
            // TODO
        }

        log.info("txReceipt:"+txReceipt);
        // TODO 加入Receipt的验证
        if(txReceipt.length() > 0){
            String tradeStatus = "TRADE_SUCCESS";

            log.debug("交易成功： ", txReceipt, "tradeStatus：", tradeStatus);
            //更新订单状态并存入mysql数据库
            orderService.updatePayOrder(Long.parseLong(outTradeNo), txReceipt, tradeStatus);

            // 构建返回信息
            Map<String, Object> data = new HashMap<>(2);
            data.put("tradeNumber", outTradeNo);
            data.put("receipt", txReceipt);
            return ResultBean.ok(data);


        }else {//验证失败，返回之前支付目录

            log.debug("区块链充值过程，交易验证失败!");
//            httpResponse.sendRedirect("/pay/index.html");
            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);
            return ResultBean.fail(ResponseStatus.BLOCKCHAIN_TX_ERROR);
        }

    }



}
