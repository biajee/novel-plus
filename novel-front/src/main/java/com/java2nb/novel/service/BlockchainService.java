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
     * 支付功能
     * @param outTradeNo 商户订单号
     * @param tradeNo      支付宝/微信 订单号
     * @param tradeStatus 支付状态
     * */
//    void payBookToken(Long outTradeNo, String tradeNo, String tradeStatus);

    /**
     * 从用户地址转移指定数额的token到指定地址
     *
     * @param bookTokenAddress 通证合约地址
     * @param fromUserAddress      拥有通证的用户地址
     * @param toUserAddress 目标地址
     * @param Amount 通证数量
     * @return 交易的HASH值
     * */
    String transferBookToken(String bookTokenAddress, String fromUserAddress, String toUserAddress, int Amount);
}
