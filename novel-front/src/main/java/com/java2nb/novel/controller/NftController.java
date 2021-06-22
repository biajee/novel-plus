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

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;

/**
 * @author 11797
 */
@RestController
@RequestMapping("nft")
@RequiredArgsConstructor
@Slf4j
public class NftController extends BaseController {

    private Web3j myWeb3;
    private Credentials credentials;

    public NftController() throws Exception {

        super();
        
        myWeb3 = Web3j.build(new HttpService("http://localhost:8545"));
        // Web3j web3b = Web3j.build(new HttpService("YOUR_PROVIDER_HERE"));
        // Web3j myEtherWallet = Web3j.build(
        //   new HttpService("https://api.myetherapi.com/eth"));

        Credentials credentials =
        WalletUtils.loadCredentials(
           "<password>",
           "/path/to/<walletfile>");

    }

    public EthBlockNumber getBlockNumber() {
        EthBlockNumber result = new EthBlockNumber();
        result = this.myWeb3.ethBlockNumber()
          .sendAsync()
          .get();
        return result;
    }

    /**
     * 生成
     */
    @PostMapping("create")
    public create() {

    }

    /**
     * 读取
     */
    @PostMapping("get")
    public get() {

    }


    /**
     * 刷新token
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





}
