package com.java2nb.novel.service;


import com.java2nb.novel.core.bean.PageBean;
import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.entity.UserBuyRecord;
import com.java2nb.novel.entity.UserFeedback;
import com.java2nb.novel.entity.UserTokenList;
import com.java2nb.novel.vo.BookReadHistoryVO;
import com.java2nb.novel.vo.BookShelfVO;
import com.java2nb.novel.entity.User;
import com.java2nb.novel.vo.UserFeedbackVO;

import java.util.Date;
import java.util.List;

/**
 * @author 11797
 */
public interface UserService {

    /**
     * 用户注册
     * @param user 用户注册信息类
     * @return jwt载体信息类
     * */
    UserDetails register(User user);

    /**
     * 用户登陆
     * @param user 用户登陆信息类
     * @return jwt载体信息类
     * */
    UserDetails login(User user);

    /**
     * 查询小说是否已加入书架
     * @param userId 用户ID
     * @param bookId 小说ID
     * @return true:已加入书架，未加入书架
     * */
    Boolean queryIsInShelf(Long userId, Long bookId);

    /**
     * 加入书架
     * @param userId 用户ID
     * @param bookId 小说ID
     * @param preContentId 阅读的内容ID
     * */
    void addToBookShelf(Long userId, Long bookId, Long preContentId);

    /**
     * 移出书架
     * @param userId 用户ID
     * @param bookId 小说ID
     * */
    void removeFromBookShelf(Long userId, Long bookId);

    /**
     * 查询书架
     * @param userId 用户ID
     * @param page
     * @param pageSize
     * @return 书架分页信息
     * */
    PageBean<BookShelfVO> listBookShelfByPage(Long userId, int page, int pageSize);

    /**
     * 添加阅读记录
     * @param userId 用户id
     * @param bookId 书籍id
     * @param preContentId 阅读的目录id
     * */
    void addReadHistory(Long userId, Long bookId, Long preContentId);

    /**
     * 添加反馈
     * @param userId 用户id
     * @param content 反馈内容
     * */
    void addFeedBack(Long userId, String content);

    /**
     * 分页查询我的反馈列表
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 分页大小
     * @return 反馈分页数据
     * */
    PageBean<UserFeedback> listUserFeedBackByPage(Long userId, int page, int pageSize);

    /**
     * 查询个人信息
     * @param userId 用户id
     * @return 用户信息
     * */
    User userInfo(Long userId);

    /**
     * 分页查询阅读记录
     * @param userId 用户id
     * @param page 页码
     * @param pageSize 分页大小
     * @return 分页数据
     * */
    PageBean<BookReadHistoryVO> listReadHistoryByPage(Long userId, int page, int pageSize);

    /**
     * 更新个人信息
     * @param userId 用户id
     * @param user 需要更新的信息
     * */
    void updateUserInfo(Long userId, User user);

    /**
     * 更新密码
     * @param userId 用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * */
    void updatePassword(Long userId, String oldPassword, String newPassword);


    /**
     * 增加用户余额
     * @param userId 用户ID
     * @param amount 增加的余额 */
    void addAmount(Long userId, int amount);

    /**
     * 判断用户是否购买过该小说章节
     * @param userId 用户ID
     * @param bookIndexId 章节目录ID
     * @return true:购买过，false:没购买
     * */
    boolean queryIsBuyBookIndex(Long userId, Long bookIndexId);

    /**
     * 购买小说章节
     * @param userId 用户ID
     * @param buyRecord 购买信息
     * */
    void buyBookIndex(Long userId, UserBuyRecord buyRecord);

    /**
     * 转移区块链的余额
     * @param userId 用户ID
     * @param toAddress 目标的合约地址
     * @param amount 转移原生通证数量
     * @param password 用户的账户密码
     * @return txHash 交易的HASH地址，交易有不成功的可能，需要
     *
     * */
    public String transferBalance(Long userId, String srcAddress, String toAddress, int amount, String password);

    /**
     * 转移通证，用户ID可以对应多个账户地址
     * @param userId 用户ID
     * @param srcAddress 用户的账户地址
     * @param toAddress 目标的合约地址
     * @param tokenAddress 通证的合约地址
     * @param amount 通证数量
     * @param password 用户的账户密码
     * @return txHash 交易的HASH地址，交易有不成功的可能，需要
     *
     * */
    public String transferTokenBalance(Long userId, String srcAddress, String toAddress, String tokenAddress, int amount, String password);

    /**
     * 查询作品时间段内的订阅人数
     * @param bookId 作品ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订阅人数
     */
    int queryBuyMember(Long bookId, Date startTime, Date endTime);

    /**
     * 查询作品时间段内的订阅次数
     * @param bookId 作品ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订阅次数
     */
    int queryBuyCount(Long bookId, Date startTime, Date endTime);

    /**
     * 查询作品时间段内的订阅总额（屋币）
     * @param bookId 作品ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订阅总额（屋币）
     */
    int queryBuyAccount(Long bookId, Date startTime, Date endTime);

    /**
     * 查询作者时间段内的订阅人数
     * @param bookIds z作者的所有作品ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订阅人数
     */
    int queryBuyTotalMember(List<Long> bookIds, Date startTime, Date endTime);

    /**
     * 查询用户通证余额
     * @param userId 用户ID
     * @param page
     * @param pageSize
     * @return 通证分页信息
     * */
    PageBean<UserTokenList> listTokenBalanceByPage(Long userId, int page, int pageSize);

    /**
     * 使用输入密码解锁钱包文件，并返回私钥
     * @param userId 用户ID
     * @param password 用户钱包密码
     * @return privateKey
     */
    String getPrivateKey(Long userId,String password);

    /**
     * 用户使用区块链账户登陆第一步
     * 根据输入的账户信息返回用于签名验证的随机整数。
     * @param user 用户登陆信息类
     * @return 随机整数，用于签名验证
     * */
    int getUserAccountNonce(String publicAddress);

    /**
     * 用户使用区块链账户登陆第二步
     * 根据返回的签名信息验证用户账户
     * @param user 用户登陆信息类，包括签名信息
     * @return jwt载体信息类
     * */
    UserDetails loginWithBlockchainAccountSignature(String publicAddress, String signature);
}
