package com.java2nb.novel.controller;

import com.java2nb.novel.core.bean.ResultBean;
import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.cache.CacheService;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.core.utils.RandomValidateCodeUtil;
import com.java2nb.novel.core.valid.AddGroup;
import com.java2nb.novel.core.valid.UpdateGroup;
import com.java2nb.novel.entity.User;
import com.java2nb.novel.entity.UserBuyRecord;
import com.java2nb.novel.service.BlockchainService;
import com.java2nb.novel.service.BookService;
import com.java2nb.novel.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author 11797
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Slf4j
public class UserController extends BaseController {


    private final CacheService cacheService;

    private final UserService userService;

    private final BookService bookService;

    private final BlockchainService blockchainService;

    /**
     * 登陆
     */
    @PostMapping("login")
    public ResultBean login(User user) {

        //登陆
        UserDetails userDetails = userService.login(user);

        Map<String, Object> data = new HashMap<>(1);
        data.put("token", jwtTokenUtil.generateToken(userDetails));

        log.info("login： "+userDetails.getUsername()+" "+userDetails.getAccountAddress());

        return ResultBean.ok(data);
    }

    /**
     * 注册
     */
    @PostMapping("register")
    public ResultBean register(@Validated({AddGroup.class}) User user, @RequestParam(value = "velCode", defaultValue = "") String velCode) {


        //判断验证码是否正确
        if (!velCode.equals(cacheService.get(RandomValidateCodeUtil.RANDOM_CODE_KEY))) {
            return ResultBean.fail(ResponseStatus.VEL_CODE_ERROR);
        }

        //注册
        UserDetails userDetails = userService.register(user);
        Map<String, Object> data = new HashMap<>(1);
        data.put("token", jwtTokenUtil.generateToken(userDetails));

        log.info("register： "+userDetails.getUsername()+" "+userDetails.getAccountAddress());
        return ResultBean.ok(data);


    }


    /**
     * 刷新token
     * 加入区块链地址
     */
    @PostMapping("refreshToken")
    public ResultBean refreshToken(HttpServletRequest request) {
        String token = getToken(request);
        if (jwtTokenUtil.canRefresh(token)) {
            token = jwtTokenUtil.refreshToken(token);
            Map<String, Object> data = new HashMap<>(2);
            data.put("token", token);
            UserDetails userDetail = jwtTokenUtil.getUserDetailsFromToken(token);
            data.put("username", userDetail.getUsername());
            data.put("nickName", userDetail.getNickName());
            data.put("accountAddress", userDetail.getAccountAddress());
            return ResultBean.ok(data);

        } else {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }

    }

    /**
     * 查询小说是否已加入书架
     */
    @GetMapping("queryIsInShelf")
    public ResultBean queryIsInShelf(Long bookId, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        return ResultBean.ok(userService.queryIsInShelf(userDetails.getId(), bookId));
    }

    /**
     * 加入书架
     * */
    @PostMapping("addToBookShelf")
    public ResultBean addToBookShelf(Long bookId,Long preContentId, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        userService.addToBookShelf(userDetails.getId(),bookId,preContentId);
        return ResultBean.ok();
    }

    /**
     * 移出书架
     * */
    @DeleteMapping("removeFromBookShelf/{bookId}")
    public ResultBean removeFromBookShelf(@PathVariable("bookId") Long bookId, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        userService.removeFromBookShelf(userDetails.getId(),bookId);
        return ResultBean.ok();
    }

    /**
     * 分页查询书架
     * */
    @GetMapping("listBookShelfByPage")
    public ResultBean listBookShelfByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "10") int pageSize,HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        return ResultBean.ok(userService.listBookShelfByPage(userDetails.getId(),page,pageSize));
    }

    /**
     * 分页查询阅读记录
     * */
    @GetMapping("listReadHistoryByPage")
    public ResultBean listReadHistoryByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "10") int pageSize,HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        return ResultBean.ok(userService.listReadHistoryByPage(userDetails.getId(),page,pageSize));
    }

    /**
     * 添加阅读记录
     * */
    @PostMapping("addReadHistory")
    public ResultBean addReadHistory(Long bookId,Long preContentId, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        userService.addReadHistory(userDetails.getId(),bookId,preContentId);
        return ResultBean.ok();
    }

    /**
     * 添加反馈
     * */
    @PostMapping("addFeedBack")
    public ResultBean addFeedBack(String content, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        userService.addFeedBack(userDetails.getId(),content);
        return ResultBean.ok();
    }

    /**
     * 分页查询我的反馈列表
     * */
    @GetMapping("listUserFeedBackByPage")
    public ResultBean listUserFeedBackByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "5") int pageSize, HttpServletRequest request){
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        return ResultBean.ok(userService.listUserFeedBackByPage(userDetails.getId(),page,pageSize));
    }

    /**
     * 查询个人信息
     * */
    @GetMapping("userInfo")
    public ResultBean userInfo(HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);

        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }

        return ResultBean.ok(userService.userInfo(userDetails.getId()));
    }

    /**
     * 更新个人信息
     * */
    @PostMapping("updateUserInfo")
    public ResultBean updateUserInfo(@Validated({UpdateGroup.class}) User user, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        userService.updateUserInfo(userDetails.getId(),user);
        if(user.getNickName() != null){
            userDetails.setNickName(user.getNickName());
            Map<String, Object> data = new HashMap<>(1);
            data.put("token", jwtTokenUtil.generateToken(userDetails));
            return ResultBean.ok(data);
        }
        return ResultBean.ok();
    }


    /**
     * 更新密码
     * */
    @PostMapping("updatePassword")
    public ResultBean updatePassword(String oldPassword,String newPassword1,String newPassword2,HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        if(!(StringUtils.isNotBlank(newPassword1) && newPassword1.equals(newPassword2))){
            ResultBean.fail(ResponseStatus.TWO_PASSWORD_DIFF);
        }
        userService.updatePassword(userDetails.getId(),oldPassword,newPassword1);
        return ResultBean.ok();
    }

    /**
     * 分页查询用户书评
     * */
    @GetMapping("listCommentByPage")
    public ResultBean listCommentByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "5") int pageSize,HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        return ResultBean.ok(bookService.listCommentByPage(userDetails.getId(),null,page,pageSize));
    }


    /**
     * 购买小说章节
     * */
    @PostMapping("buyBookIndex")
    public ResultBean buyBookIndex(UserBuyRecord buyRecord, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        buyRecord.setBuyAmount(bookService.queryBookIndex(buyRecord.getBookIndexId()).getBookPrice());
        userService.buyBookIndex(userDetails.getId(),buyRecord);
        return ResultBean.ok();
    }

    /**
     * 分页查询通证的余额情况
     * */
    @GetMapping("listTokenBalanceByPage")
    public ResultBean listTokenBalanceByPage(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "10") int pageSize,HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        return ResultBean.ok(userService.listTokenBalanceByPage(userDetails.getId(),page,pageSize));
    }

    /**
     * 查询区块链最新区块高度
     * */
    @GetMapping("listBlockchainHeight")
    public ResultBean listBlockchainHeight(){
        return ResultBean.ok(blockchainService.getBlockNumber());
    }

    /**
     * 查询用户区块链账户的余额
     * 仅允许查询本用户的余额，查询其它用户的需要使用Blockchain或者ContractController
     * */
    @GetMapping("getAccountBalance")
    public ResultBean getAccountBalance(HttpServletRequest request){
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        log.info("USERCONTROLLER getAccountBalance:"+userDetails.getId()+" : "+userDetails.getAccountAddress());
        if (userDetails.getAccountAddress() == null){
            return ResultBean.fail(ResponseStatus.BLOCKCHAIN_ACCOUNT_ERROR);
        }
        return ResultBean.ok(blockchainService.getAccountBalance(userDetails.getAccountAddress()));
    }

    /**
     * 查询用户区块链账户通证的余额
     * tokenAddress: 作品合约的地址
     * accountAddress: 用户区块链账户地址
     * */
    @GetMapping("getTokenBalance")
    public ResultBean getTokenBalance(String tokenAddress, String accountAddress){
        return ResultBean.ok(blockchainService.getTokenBalance(tokenAddress, accountAddress));
    }

    /**
     * 查询用户区块链账户私钥，本函数仅用于调试账户操作
     * tokenAddress: 作品合约的地址
     * accountAddress: 用户区块链账户地址
     * */
    @GetMapping("getAccountPrivateKey")
    public ResultBean getAccountPrivateKey(String password, HttpServletRequest request){
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        log.info("USERCONTROLLER getAccountPrivateKey:"+userDetails.getId()+" : "+password);
        return ResultBean.ok(userService.getPrivateKey(userDetails.getId(),password));
    }

    /**
     * 使用原生币支付，
     * 通过登录用户信息，及输入的交易密码，转移区块链的原生通证。
     * 并增加用户书币的数量。
     * 为与原有系统兼容，输入单位最小设为0.01元。
     * 返回交易HASH
     * */
    @PostMapping("payWithBlockchain")
    public ResultBean payWithBlockchain(String toAddress,double value, String password, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        // 检测用户是否有区块链钱包，如果有解锁相应钱包
        log.debug("payWithBlockchain src： "+userDetails.getId()+" "+userDetails.getAccountAddress());

        int intValue = (int) (value * 100);
        return ResultBean.ok(userService.transferBalance(userDetails.getId(), userDetails.getAccountAddress(), toAddress, intValue, password));
//        return ResultBean.ok(blockchainService.sendSignedTransaction(srcAddress, toAddress, value, privateKey));
    }

    /**
     * 使用用户账户的通证支付
     * 返回交易HASH
     * tokenAddress - 通证合约地址
     * srcAddress - 发起账户地址（默认为用户本人地址），用于调出私钥，进行签名
     * toAddress - 通证转移目标地址
     * value - 转移的通证数量，需要使用合约中的decimal变量转换为bigint
     * */
    @PostMapping("payWithToken")
    public ResultBean payWithToken(String toAddress, String tokenAddress, double value, String password, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(request);
        if (userDetails == null) {
            return ResultBean.fail(ResponseStatus.NO_LOGIN);
        }
        log.debug("payWithToken src： "+userDetails.getId()+" "+userDetails.getAccountAddress());
        log.debug("payWithToken： "+tokenAddress+" to "+toAddress);
        int intValue = (int) (value * 100);

        return ResultBean.ok(userService.transferTokenBalance(userDetails.getId(), userDetails.getAccountAddress(), toAddress, tokenAddress, intValue, password));
    }

    /**
     * 区块链账户登陆
     * 参考实现方式：
     * 1.后端：修改后用户模型内容，加入publicAddress信息（UserDetails）；
     * 2.后端：加入生成随机数 nonce 机制，对于数据库中的每个用户，在 nonce 字段中生成随机字符串。例如， nonce 可以是一个大的随机整数；
     * 3.前端：用户在登录时，首先获取与其账户相应的随机数（Step One）；
     * 4.前端：用户签署包含随机数的数字签名，发送后台进行验证（Step Two）；
     * 5.后端：签名验证，完成用户登录；
     * 6.后端：更改Nonce
     */
    /**
     * 查询用户区块链账户相应的随机数信息，用于签名验证
     * */
    @GetMapping("getUserAccountNonce")
    public ResultBean getUserAccountNonce(String publicAddress,HttpServletRequest request) {
        //
//        Map<String, Object> data = new HashMap<>(1);
//        // 暂时用随机数，TODO 修改 UserDetals内部记录的随机数
//        data.put("nonce", new Random().nextInt(10));
        return ResultBean.ok(userService.getUserAccountNonce(publicAddress));
    }

    @PostMapping("loginWithBlockchainAccount")
    public ResultBean loginWithBlockchainAccount(String publicAddress, String signature) {

//        //登陆
//        UserDetails userDetails = userService.loginWithBlockchainAccount(user);
//
        Map<String, Object> data = new HashMap<>(1);
        // 暂时用随机数，TODO 修改 UserDetals内部记录的
        data.put("nonce", new Random().nextInt(10));

        return ResultBean.ok(data);
    }

}
