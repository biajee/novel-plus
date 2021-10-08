package com.java2nb.novel.service;



/**
 * @author 11797
 */
public interface OrderService {


    /**
     * 创建充值订单
     *
     * @param payChannel 支付渠道
     * @param payAmount 支付金额
     * @param userId 用户ID
     * @return 商户订单号
     * */
    Long createPayOrder(Byte payChannel, Integer payAmount, Long userId);


    /**
     * 更新订单状态
     * @param outTradeNo 商户订单号
     * @param tradeNo      支付宝/微信 订单号
     * @param tradeStatus 支付状态
     * */
    void updatePayOrder(Long outTradeNo, String tradeNo, String tradeStatus);

    /**
     * 提交区块链充值订单，
     * TODO 允许使用用户的多个账户地址，或者签名后的交易
     *
     * @param userId 用户ID
     * @param srcAddress 用户的账户地址
     * @param desAddress 支付对象账户地址
     * @param payAmount 支付金额
     * @param inPassword 用户账户的支付密码
     * @return 商户订单号
     * */
    String submitPayOrder(Long userId, String srcAddress, String desAddress, int payAmount, String inPassword);

}
