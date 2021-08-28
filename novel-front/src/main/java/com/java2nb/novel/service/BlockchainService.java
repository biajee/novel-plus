package com.java2nb.novel.service;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author 11797
 * 使用Web3j创建相应区块链交易功能
 */
public interface BlockchainService {

    /**
     * 查询区块高度功能
     * @return 返回当前平台的最新区块高度
     * */
    BigInteger getBlockNumber();

    /**
     * 生成新账户，使用输入的密码作为账户密码，
     * @return 返回账户地址
     * */
    String createNewAccount(String password);

    /**
     * 查询原生通证余额功能
     * @return 返回输入通证的最新余额
     * */
    BigInteger getAccountBalance(String accountAddress);

    /**
     * 账号解锁
     * @return 返回成功与否
     * */
    Boolean unlockAccount(String address, String password) ;

    /**
     * 创建作品合约
     *
     * @param fromUserAddress 创建合约的用户账户
     * @return 创建的合约地址
     * */
    String createBookTokenContract(String fromUserAddress);

    /**
     * 查询通证余额功能
     * @return 返回输入通证的最新余额
     * */
    BigInteger getTokenBalance(String tokenAddress, String accountAddress);

    /**
     * 查询通证名称功能
     * @return 返回输入通证的名称
     * */
    String getTokenName(String tokenAddress);

    /**
     * 查询通证精度
     * @return 返回输入通证的数值精度
     * */
    int getTokenDecimals(String tokenAddress);

    /**
     * 支付功能
     * @param srcAddress 发送地址账户
     * @param toAddress  目标地址账户
     * @param amount 数额
     * @param privateKey 发送账户的私钥
     * @return 返回交易的HASH
     * */
     String sendSignedTransaction(String srcAddress, String toAddress, double amount, String privateKey);

    /**
     * 从用户地址转移指定数额的token到指定地址
     *
     * @param bookTokenAddress 通证合约地址
     * @param fromUserAddress      拥有通证的用户地址
     * @param toUserAddress 目标地址
     * @param Amount 通证数量
     * @param privateKey 发送账户的私钥
     * @return 交易的HASH值
     * */
    String transferBookToken(String bookTokenAddress, String fromUserAddress, String toUserAddress, double amount, String privateKey);
}
