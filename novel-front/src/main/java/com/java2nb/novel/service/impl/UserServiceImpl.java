package com.java2nb.novel.service.impl;

import com.github.pagehelper.PageHelper;
import com.java2nb.novel.core.bean.PageBean;
import com.java2nb.novel.core.bean.UserDetails;
import com.java2nb.novel.core.utils.BeanUtil;
import com.java2nb.novel.entity.*;
import com.java2nb.novel.entity.User;
import com.java2nb.novel.service.AuthorService;
import com.java2nb.novel.service.BlockchainService;
import com.java2nb.novel.service.UserService;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.core.exception.BusinessException;
import com.java2nb.novel.mapper.*;
import com.java2nb.novel.vo.BookReadHistoryVO;
import com.java2nb.novel.vo.BookShelfVO;
import com.java2nb.novel.core.utils.IdWorker;
import com.java2nb.novel.core.utils.MD5Util;
import com.java2nb.novel.vo.UserFeedbackVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOExceptionWithCause;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.*;// use to create account keypairs
import org.web3j.protocol.ObjectMapperFactory;

import java.util.Date;
import java.util.List;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.io.IOException;
import java.util.Random;

//import static com.java2nb.novel.mapper.BookDynamicSqlSupport.id;
import static com.java2nb.novel.mapper.UserBookshelfDynamicSqlSupport.userBookshelf;
import static com.java2nb.novel.mapper.UserDynamicSqlSupport.*;
import static com.java2nb.novel.mapper.UserFeedbackDynamicSqlSupport.userFeedback;
import static com.java2nb.novel.mapper.UserReadHistoryDynamicSqlSupport.userReadHistory;
import static com.java2nb.novel.mapper.UserTokenListDynamicSqlSupport.bookId;
import static com.java2nb.novel.mapper.UserTokenListDynamicSqlSupport.userTokenList;
import static com.java2nb.novel.mapper.UserWalletDynamicSqlSupport.userAddress;
import static com.java2nb.novel.mapper.UserWalletDynamicSqlSupport.userWallet;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.mybatis.dynamic.sql.select.SelectDSL.select;

/**
 * @author 11797
 * 增加WalletMapper，
 * getUserWalletPrivateKey
 *
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserWalletMapper userWalletMapper;

    private final FrontUserBookshelfMapper userBookshelfMapper;

    private final FrontUserReadHistoryMapper userReadHistoryMapper;

    private final UserFeedbackMapper userFeedbackMapper;

    private final UserBuyRecordMapper userBuyRecordMapper;

    // List the balance of each token
    private final UserTokenListMapper userTokenListMapper;

    private final BlockchainService blockchainService;

    /*
     * 增加区块链钱包的生成
     * 增加对区块链账户签名的验证和登录许可
     *
     */
    @Override
    public UserDetails register(User user) {

        WalletFile walletFile;

        //查询用户名是否已注册
        SelectStatementProvider selectStatement = select(count(id))
                .from(UserDynamicSqlSupport.user)
                .where(username, isEqualTo(user.getUsername()))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        long count = userMapper.count(selectStatement);
        if (count > 0) {
            //用户名已注册
            throw new BusinessException(ResponseStatus.USERNAME_EXIST);
        }


        // 数据库生成注册记录，
        // 生成用户对象
        User entity = new User();
        BeanUtils.copyProperties(user,entity);
        // 用户钱包，为新用户生成钱包对象
        UserWallet entityWallet = new UserWallet();

        Long id = new IdWorker().nextId();
        entity.setId(id);
        entity.setNickName(entity.getUsername());
        Date currentDate = new Date();
        entity.setCreateTime(currentDate);
        entity.setUpdateTime(currentDate);

        entityWallet.setUserId(id);
        //使用web3j的内置功能，生成公私钥对，存储公钥到user记录中
        //生成钱包文件，并存入userWallet中。
        try{
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            String privateKey = ecKeyPair.getPrivateKey().toString(16);
            String accountAddress = "0x"+Keys.getAddress(ecKeyPair);

            entityWallet.setUserAddress(accountAddress);
            entityWallet.setPrivateKey(privateKey);
            // 注意，生成钱包文件，需要使用用户输入的password，而数据库存储的仅仅是password的MD5encode
            walletFile = Wallet.createStandard(entity.getPassword(), ecKeyPair);
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            String jsonStr = objectMapper.writeValueAsString(walletFile);
            entityWallet.setKeystore(jsonStr);

            entity.setBlockchainAddress(accountAddress);
        }catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | CipherException | JsonProcessingException e) {
            log.debug("In User register, 密钥对生成错误:"+e);
        }
        entity.setPassword(MD5Util.MD5Encode(entity.getPassword(), Charsets.UTF_8.name()));


        // 存入mysql数据库
        userMapper.insertSelective(entity);
        userWalletMapper.insert(entityWallet);

        //生成UserDetail对象并返回
        UserDetails userDetails = new UserDetails();
        userDetails.setId(id);
        userDetails.setUsername(entity.getUsername());
        userDetails.setNickName(entity.getNickName());
        userDetails.setAccountAddress(entity.getBlockchainAddress());
        return userDetails;
    }

    /*
    * 加入UserDetails的内容
    * 注意要加上blockchainAddress
    */
    @Override
    public UserDetails login(User user) {
        //根据用户名密码查询记录
        SelectStatementProvider selectStatement = select(id, username,nickName,blockchainAddress)
                .from(UserDynamicSqlSupport.user)
                .where(username, isEqualTo(user.getUsername()))
                .and(password, isEqualTo(MD5Util.MD5Encode(user.getPassword(), Charsets.UTF_8.name())))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        List<User> users = userMapper.selectMany(selectStatement);
        if (users.size() == 0) {
            throw new BusinessException(ResponseStatus.USERNAME_PASS_ERROR);
        }
        //生成UserDetail对象并返回
        UserDetails userDetails = new UserDetails();
        user = users.get(0);
        userDetails.setId(user.getId());
        userDetails.setNickName(user.getNickName());
        userDetails.setUsername(user.getUsername());
        userDetails.setAccountAddress(user.getBlockchainAddress());
        return userDetails;
    }

    /*
     * 从数据库中检索用户的publicAddress，
     *
     */
    @Override
    public UserDetails loginWithBlockchainAccountSignature(String publicAddress, String signature) {
        //根据用户的账户地址查询记录
        SelectStatementProvider selectStatement = select(id, username,userAddress)
                .from(UserDynamicSqlSupport.user)
                .where(userAddress, isEqualTo(publicAddress))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        List<User> users = userMapper.selectMany(selectStatement);
        if (users.size() == 0) {
            throw new BusinessException(ResponseStatus.BLOCKCHAIN_ACCOUNT_ERROR);
        }
        // 验证用户签名的正确性
        //生成UserDetail对象并返回
        UserDetails userDetails = new UserDetails();
        User user = users.get(0);
        userDetails.setId(user.getId());
        userDetails.setNickName(user.getNickName());
        userDetails.setUsername(user.getUsername());
        userDetails.setAccountAddress(user.getBlockchainAddress());
        return userDetails;
    }

    @Override
    public Boolean queryIsInShelf(Long userId, Long bookId) {
        SelectStatementProvider selectStatement = select(count(UserBookshelfDynamicSqlSupport.id))
                .from(userBookshelf)
                .where(UserBookshelfDynamicSqlSupport.userId, isEqualTo(userId))
                .and(UserBookshelfDynamicSqlSupport.bookId, isEqualTo(bookId))
                .build()
                .render(RenderingStrategies.MYBATIS3);

        return userBookshelfMapper.count(selectStatement) > 0;
    }

    @Override
    public void addToBookShelf(Long userId, Long bookId, Long preContentId) {
        if (!queryIsInShelf(userId, bookId)) {
            UserBookshelf shelf = new UserBookshelf();
            shelf.setUserId(userId);
            shelf.setBookId(bookId);
            shelf.setPreContentId(preContentId);
            shelf.setCreateTime(new Date());
            userBookshelfMapper.insert(shelf);
        }

    }

    @Override
    public void removeFromBookShelf(Long userId, Long bookId) {
        DeleteStatementProvider deleteStatement = deleteFrom(userBookshelf)
                .where(UserBookshelfDynamicSqlSupport.userId, isEqualTo(userId))
                .and(UserBookshelfDynamicSqlSupport.bookId, isEqualTo(bookId))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        userBookshelfMapper.delete(deleteStatement);

    }

    @Override
    public PageBean<BookShelfVO> listBookShelfByPage(Long userId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return new PageBean<>(userBookshelfMapper.listBookShelf(userId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addReadHistory(Long userId, Long bookId, Long preContentId) {

        Date currentDate = new Date();
        //删除该书以前的历史记录
        DeleteStatementProvider deleteStatement = deleteFrom(userReadHistory)
                .where(UserReadHistoryDynamicSqlSupport.bookId, isEqualTo(bookId))
                .and(UserReadHistoryDynamicSqlSupport.userId, isEqualTo(userId))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        userReadHistoryMapper.delete(deleteStatement);

        //插入该书新的历史记录
        UserReadHistory userReadHistory = new UserReadHistory();
        userReadHistory.setBookId(bookId);
        userReadHistory.setUserId(userId);
        userReadHistory.setPreContentId(preContentId);
        userReadHistory.setCreateTime(currentDate);
        userReadHistory.setUpdateTime(currentDate);
        userReadHistoryMapper.insertSelective(userReadHistory);


        //更新书架的阅读历史
        UpdateStatementProvider updateStatement = update(userBookshelf)
                .set(UserBookshelfDynamicSqlSupport.preContentId)
                .equalTo(preContentId)
                .set(UserBookshelfDynamicSqlSupport.updateTime)
                .equalTo(currentDate)
                .where(UserBookshelfDynamicSqlSupport.userId, isEqualTo(userId))
                .and(UserBookshelfDynamicSqlSupport.bookId, isEqualTo(bookId))
                .build()
                .render(RenderingStrategies.MYBATIS3);

        userBookshelfMapper.update(updateStatement);

    }

    @Override
    public void addFeedBack(Long userId, String content) {
        UserFeedback feedback = new UserFeedback();
        feedback.setUserId(userId);
        feedback.setContent(content);
        feedback.setCreateTime(new Date());
        userFeedbackMapper.insertSelective(feedback);
    }

    @Override
    public PageBean<UserFeedback> listUserFeedBackByPage(Long userId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        SelectStatementProvider selectStatement = select(UserFeedbackDynamicSqlSupport.content, UserFeedbackDynamicSqlSupport.createTime)
                .from(userFeedback)
                .where(UserFeedbackDynamicSqlSupport.userId, isEqualTo(userId))
                .orderBy(UserFeedbackDynamicSqlSupport.id.descending())
                .build()
                .render(RenderingStrategies.MYBATIS3);
        List<UserFeedback> userFeedbacks = userFeedbackMapper.selectMany(selectStatement);
        PageBean<UserFeedback> pageBean = new PageBean<>(userFeedbacks);
        pageBean.setList(BeanUtil.copyList(userFeedbacks,UserFeedbackVO.class));
        return pageBean;
    }

    @Override
    public User userInfo(Long userId) {
        SelectStatementProvider selectStatement = select(username, nickName, blockchainAddress, userPhoto,userSex,accountBalance)
                .from(user)
                .where(id, isEqualTo(userId))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return userMapper.selectMany(selectStatement).get(0);
    }

    @Override
    public PageBean<BookReadHistoryVO> listReadHistoryByPage(Long userId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return new PageBean<>(userReadHistoryMapper.listReadHistory(userId));
    }

    @Override
    public void updateUserInfo(Long userId, User user) {
        user.setId(userId);
        user.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(user);

    }

    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        SelectStatementProvider selectStatement = select(password)
                .from(user)
                .where(id,isEqualTo(userId))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        if(!userMapper.selectMany(selectStatement).get(0).getPassword().equals(MD5Util.MD5Encode(oldPassword, Charsets.UTF_8.name()))){
            throw new BusinessException(ResponseStatus.OLD_PASSWORD_ERROR);
        }
        UpdateStatementProvider updateStatement = update(user)
                .set(password)
                .equalTo(MD5Util.MD5Encode(newPassword, Charsets.UTF_8.name()))
                .where(id,isEqualTo(userId))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        userMapper.update(updateStatement);

        //需要修改Wallet中的密码并更新Keystore文件
        updateWalletPassword(userId, oldPassword, newPassword);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addAmount(Long userId, int amount) {
        User user = this.userInfo(userId);
        user.setId(userId);
        user.setAccountBalance(user.getAccountBalance()+amount);
        userMapper.updateByPrimaryKeySelective(user);

    }

    @Override
    public boolean queryIsBuyBookIndex(Long userId, Long bookIndexId) {

        return userBuyRecordMapper.count(c ->
                c.where(UserBuyRecordDynamicSqlSupport.userId, isEqualTo(userId))
                .and(UserBuyRecordDynamicSqlSupport.bookIndexId,isEqualTo(bookIndexId))) > 0;
    }

    /*
     * 购买阅读章节时，可能会有相应的通证收入，这部分需要做的更加灵活一些
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void buyBookIndex(Long userId, UserBuyRecord buyRecord) {
        //查询用户余额
        long balance = userInfo(userId).getAccountBalance();
        if(balance<buyRecord.getBuyAmount()){
            //余额不足
            throw new BusinessException(ResponseStatus.USER_NO_BALANCE);
        }
        buyRecord.setUserId(userId);
        buyRecord.setCreateTime(new Date());
        //生成购买记录
        userBuyRecordMapper.insertSelective(buyRecord);

        // 根据用户的权益通证数量，分配额外的收入红利给通证持有者
        // 这会影响用户的accountBalance.
//        Float distPercentage = Float(0.3);
//        spTokenDist(buyRecord.getId(), distPercentage);


        // 计算用户获取的收益，
        int userTokenNum = queryUserTokenCount(buyRecord.getBookId(), buyRecord.getUserId());
        int totalTokenNum = queryBookTokenCount(buyRecord.getBookId());
        log.debug(("buyBookIndex:"+userTokenNum+" : "+totalTokenNum+" amount: "+buyRecord.getBuyAmount()));
        int tokenRewards = buyRecord.getBuyAmount()  * userTokenNum / totalTokenNum;
        // 计算用户余额的变化，加入tokenRewards
        long finalBalance = balance - buyRecord.getBuyAmount() + tokenRewards;
        log.debug(("buyBookIndex: rewards"+tokenRewards+" :finalBalance"+finalBalance));


        //减少用户余额
        userMapper.update(update(user)
                .set(UserDynamicSqlSupport.accountBalance)
                .equalTo(finalBalance)
                .where(id,isEqualTo(userId))
                .build()
                .render(RenderingStrategies.MYBATIS3));

        // 修改相应用户和作品通证的收入
        // 选择对应作品的通证
        SelectStatementProvider selectStatement = select(UserTokenListDynamicSqlSupport.id,UserTokenListDynamicSqlSupport.userId,UserTokenListDynamicSqlSupport.bookId,UserTokenListDynamicSqlSupport.tokenIncome, UserTokenListDynamicSqlSupport.updateTime)
                .from(userTokenList)
                .where(UserTokenListDynamicSqlSupport.userId, isEqualTo(userId))
                .and(UserTokenListDynamicSqlSupport.bookId, isEqualTo(buyRecord.getBookId()))
                .build()
                .render(RenderingStrategies.MYBATIS3);

        UserTokenList userTokenList = this.userTokenListMapper.selectOne(selectStatement);
        log.info("buyBookIndex: update record:"+userTokenList.getTokenIncome()+" with tokenRewards:"+tokenRewards);
        updateUserTokenIncomeInfo(userTokenList, new Long(tokenRewards)+userTokenList.getTokenIncome());
        // TODO，更新更多账户的信息
    }

    @Override
    public int queryBuyMember(Long bookId, Date startTime, Date endTime) {
        return userMapper.selectStatistic(select(countDistinct(UserBuyRecordDynamicSqlSupport.userId))
                .from(UserBuyRecordDynamicSqlSupport.userBuyRecord)
                .where(UserBuyRecordDynamicSqlSupport.bookId,isEqualTo(bookId))
                .and(UserBuyRecordDynamicSqlSupport.createTime,isGreaterThanOrEqualTo(startTime))
                .and(UserBuyRecordDynamicSqlSupport.createTime,isLessThanOrEqualTo(endTime))
                .build()
                .render(RenderingStrategies.MYBATIS3));
    }

    @Override
    public int queryBuyCount(Long bookId, Date startTime, Date endTime) {
        return userMapper.selectStatistic(select(count(UserBuyRecordDynamicSqlSupport.id))
                .from(UserBuyRecordDynamicSqlSupport.userBuyRecord)
                .where(UserBuyRecordDynamicSqlSupport.bookId,isEqualTo(bookId))
                .and(UserBuyRecordDynamicSqlSupport.createTime,isGreaterThanOrEqualTo(startTime))
                .and(UserBuyRecordDynamicSqlSupport.createTime,isLessThanOrEqualTo(endTime))
                .build()
                .render(RenderingStrategies.MYBATIS3));
    }

    @Override
    public int queryBuyAccount(Long bookId, Date startTime, Date endTime) {
        return userMapper.selectStatistic(select(sum(UserBuyRecordDynamicSqlSupport.buyAmount))
                .from(UserBuyRecordDynamicSqlSupport.userBuyRecord)
                .where(UserBuyRecordDynamicSqlSupport.bookId,isEqualTo(bookId))
                .and(UserBuyRecordDynamicSqlSupport.createTime,isGreaterThanOrEqualTo(startTime))
                .and(UserBuyRecordDynamicSqlSupport.createTime,isLessThanOrEqualTo(endTime))
                .build()
                .render(RenderingStrategies.MYBATIS3));
    }

    @Override
    public int queryBuyTotalMember(List<Long> bookIds, Date startTime, Date endTime) {
        return userMapper.selectStatistic(select(countDistinct(UserBuyRecordDynamicSqlSupport.userId))
                .from(UserBuyRecordDynamicSqlSupport.userBuyRecord)
                .where(UserBuyRecordDynamicSqlSupport.bookId,isIn(bookIds))
                .and(UserBuyRecordDynamicSqlSupport.createTime,isGreaterThanOrEqualTo(startTime))
                .and(UserBuyRecordDynamicSqlSupport.createTime,isLessThanOrEqualTo(endTime))
                .build()
                .render(RenderingStrategies.MYBATIS3));
    }

    @Override
    public PageBean<UserTokenList> listTokenBalanceByPage(Long userId, int page, int pageSize) {
        log.debug("listTokenBalanceByPage userID:"+userId);
        PageHelper.startPage(page, pageSize);
        SelectStatementProvider selectStatement = select(UserTokenListDynamicSqlSupport.bookId,UserTokenListDynamicSqlSupport.tokenName,UserTokenListDynamicSqlSupport.tokenAddress, UserTokenListDynamicSqlSupport.tokenBalance, UserTokenListDynamicSqlSupport.tokenIncome, UserTokenListDynamicSqlSupport.updateTime)
            .from(userTokenList)
            .where(UserTokenListDynamicSqlSupport.userId, isEqualTo(userId))
            .orderBy(UserTokenListDynamicSqlSupport.id.descending())
            .build()
            .render(RenderingStrategies.MYBATIS3);
        List<UserTokenList> userTokenLists = userTokenListMapper.selectMany(selectStatement);
        PageBean<UserTokenList> pageBean = new PageBean<>(userTokenLists);
        return pageBean;
    }

    // Return the number of book tokens current user owned
    // count from the buy_amount
    public int queryUserTokenCount(Long bookId, Long userId) {
        return userMapper.selectStatistic(select(sum(UserBuyRecordDynamicSqlSupport.buyAmount))
                .from(UserBuyRecordDynamicSqlSupport.userBuyRecord)
                .where(UserBuyRecordDynamicSqlSupport.bookId,isEqualTo(bookId))
                .and(UserBuyRecordDynamicSqlSupport.userId,isEqualTo(userId))
                .build()
                .render(RenderingStrategies.MYBATIS3));
    }

    // Return the number of book tokens current distributed
    // count from the buy_amount
    public int queryBookTokenCount(Long bookId) {
        return userMapper.selectStatistic(select(sum(UserBuyRecordDynamicSqlSupport.buyAmount))
                .from(UserBuyRecordDynamicSqlSupport.userBuyRecord)
                .where(UserBuyRecordDynamicSqlSupport.bookId,isEqualTo(bookId))
                .build()
                .render(RenderingStrategies.MYBATIS3));
    }


    // Added the token reward income to the userTokenList
    // and update the UpdateTime
    public void updateUserTokenIncomeInfo(UserTokenList userTokenList, Long tokenIncome) {
        log.debug("updateUserTokenIncomeInfo userID:"+userTokenList.getId()+" income: "+tokenIncome);

        userTokenList.setId(userTokenList.getId());
        userTokenList.setTokenIncome(tokenIncome);
        userTokenList.setUpdateTime(new Date());
        userTokenListMapper.updateByPrimaryKeySelective(userTokenList);
    }

    /*
     * 交换原生通证
     * TODO 使用用户的密码，解锁钱包，目前仅使用暂存的私钥
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String transferBalance(Long inUserId, String srcAddress, String toAddress, int amount, String inPassword) {
        User user = this.userInfo(inUserId);
        String privateKey = this.getPrivateKeyFromUserWallet(inUserId, srcAddress, inPassword);
        if (privateKey == null) {
            throw new BusinessException(ResponseStatus.BLOCKCHAIN_PRIVATEKEY_ERROR);
        }
        return blockchainService.sendSignedTransaction(user.getBlockchainAddress(), toAddress, amount, privateKey);
    }

    /*
     * 交换合约通证，用户可以指定所用钱包地址。
     * TODO 考虑使用用户的密码，而不是直接用私钥
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String transferTokenBalance(Long inUserId, String srcAddress,String toAddress, String tokenAddress, int amount, String inPassword) {
        User user = this.userInfo(inUserId);
        String privateKey = this.getPrivateKeyFromUserWallet(inUserId, srcAddress, inPassword);
        if (privateKey == null) {
            throw new BusinessException(ResponseStatus.BLOCKCHAIN_PRIVATEKEY_ERROR);
        }
        return blockchainService.transferBookToken(tokenAddress, user.getBlockchainAddress(), toAddress, amount, privateKey);
    }

    /*
     * 根据用户的id，找到并解锁用户钱包，返回privateKey，用于交易签名
     * TODO，目前默认只有一个keystore，将来需要可以处理用户的多个钱包
     */
    private String getPrivateKeyFromUserWallet(Long inUserId, String srcAddress, String password) {

        SelectStatementProvider selectStatement = select(UserWalletDynamicSqlSupport.keystore, UserWalletDynamicSqlSupport.userAddress)
                .from(userWallet)
                .where(UserWalletDynamicSqlSupport.userId, isEqualTo(inUserId))
                .and(UserWalletDynamicSqlSupport.userAddress, isEqualTo(srcAddress))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        List<UserWallet> users = userWalletMapper.selectMany(selectStatement);
        if (users.size() == 0) {
            throw new BusinessException(ResponseStatus.BLOCKCHAIN_PRIVATEKEY_ERROR);
        }
        UserWallet myUserWallet = users.get(0);


        //使用用户密码对钱包文件解锁，返回私钥
        if ( myUserWallet != null){
            log.info("getUserWalletPrivateKey：找到钱包ID"+myUserWallet.getUserAddress());
            log.info("getUserWalletPrivateKey：unlock with <"+password+">");
            return blockchainService.decryptWallet(myUserWallet.getKeystore(),password);
        }else {
            return null;
        }

    }

    /*
     * 根据用户的id，找到并解锁用户钱包，返回privateKey，用于交易签名
     * TODO，目前默认只有一个keystore，将来需要可以处理用户的多个钱包
     */
    @Override
    public String getPrivateKey(Long inUserId, String password) {

        SelectStatementProvider selectStatement = select(UserWalletDynamicSqlSupport.userAddress,UserWalletDynamicSqlSupport.privateKey)
                .from(userWallet)
                .where(UserWalletDynamicSqlSupport.userId, isEqualTo(inUserId))
                .build()
                .render(RenderingStrategies.MYBATIS3);
//        List<UserWallet> myWallets = userWalletMapper.selectMany(selectStatement);
        List<UserWallet> users = userWalletMapper.selectMany(selectStatement);
        if (users.size() == 0) {
            throw new BusinessException(ResponseStatus.BLOCKCHAIN_PRIVATEKEY_ERROR);
        }
        UserWallet myUserWallet = users.get(0);
        log.info("getPrivateKey：找到钱包ID"+myUserWallet.getUserAddress()+" key:"+myUserWallet.getPrivateKey());
        return myUserWallet.getPrivateKey();
//        return blockchainService.decryptWallet(userWallet.getKeystore(),password);

    }

    /*
     * 修改钱包的密码:
     * 1. 找到当前用户的钱包记录；
     * 2. 使用当前密码导出私钥；
     * 3. 使用新密码生成钱包文件，更新记录。
     */

    private void updateWalletPassword(Long inUserId, String oldPassword, String newPassword) {
        // 1. 找到当前用户的钱包记录；
        SelectStatementProvider selectStatement = select(UserWalletDynamicSqlSupport.keystore)
                .from(userWallet)
                .where(UserWalletDynamicSqlSupport.userId, isEqualTo(inUserId))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        List<UserWallet> users = userWalletMapper.selectMany(selectStatement);
        if (users.size() == 0) {
            throw new BusinessException(ResponseStatus.BLOCKCHAIN_PRIVATEKEY_ERROR);
        }
        UserWallet myUserWallet = users.get(0);

        String privateKey = null;
        // 2. 使用当前密码导出私钥，解锁钱包
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            WalletFile walletFile = objectMapper.readValue(myUserWallet.getKeystore(), WalletFile.class);

            ECKeyPair ecKeyPair = null;
            ecKeyPair = Wallet.decrypt(oldPassword, walletFile);

            // 注意，生成新钱包文件，需要使用新的password和KeyPair
            walletFile = Wallet.createStandard(newPassword, ecKeyPair);
            String jsonStr = objectMapper.writeValueAsString(walletFile);
            myUserWallet.setKeystore(jsonStr);
        } catch (CipherException |JsonProcessingException e) {
            if ("Invalid password provided".equals(e.getMessage())) {
                log.info("更新钱包：密码错误");
            }
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        //Update the record
        userWalletMapper.updateByPrimaryKey(myUserWallet);
    }

    /*
     * 根据输入的publicAddress查询相应的UserDetails
     * 注意，这个nonce不是交易的Nonce，而是账户登录所使用的随机数
     */
    public int getUserAccountNonce(String accountAddress) {
        return new Random().nextInt(10);

    }

//    @Override
//    public void spTokenDist(Long userBuyRecordId, Float distPercentage) {
//        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_token_dist");
//
//        query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter(2, Float.class, ParameterMode.IN);
//
//        query.setParameter(1, userBuyRecordId);
//        query.setParameter(2, distPercentage);
//
//        query.execute();
//
//        return;
//    }


}
