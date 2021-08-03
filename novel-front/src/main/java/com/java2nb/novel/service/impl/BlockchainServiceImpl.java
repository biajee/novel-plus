package com.java2nb.novel.service.impl;

import com.java2nb.novel.service.BlockchainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/**
 * @author 11797
 * @slf4j used for logger
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BlockchainServiceImpl implements BlockchainService {


//    @Autowired
    private final Web3j web3j;
    private final Admin admin;
//    private Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545/"));
//Mainnet:    http://web3.moac.moacchain.net

    public BlockchainServiceImpl() {

        //This service is running on the address passed as a parameter below
        this.web3j = Web3j.build(new HttpService("http://localhost:8545"));
        this.admin = Admin.build(new HttpService("http://localhost:8545"));
    }

    @Override
    public BigInteger getBlockNumber() {
        BigInteger blockNumber = new BigInteger("-999");
        try {
            //客户端版本
            //区块数量
            EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
            blockNumber = ethBlockNumber.getBlockNumber();
            log.debug("获取区块高度"+blockNumber);
            return blockNumber;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blockNumber;
    }

    /**
     * Admin: 创建新账号
     */
    @Override
    public String createNewAccount(String password) {
        try {
            NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
            String address = newAccountIdentifier.getAccountId();
            System.out.println("new account address " + address);
            return address;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String createBookTokenContract(String fromUserAddress) {

        return fromUserAddress;
    }

    @Override
    public String transferBookToken(String bookTokenAddress, String fromUserAddress, String toUserAddress, int Amount) {

        if (Amount > 0) {

            if (bookTokenAddress == fromUserAddress)
                return fromUserAddress;
            else
                return toUserAddress;
        }else{
            return bookTokenAddress;
        }


    }


    /**
     * 账号解锁
     * @return
     */
    @Override
    public Boolean unlockAccount(String address, String password) {
        //账号解锁持续时间 单位秒 缺省值300秒
        BigInteger unlockDuration = BigInteger.valueOf(60L);
        try {
            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(address, password, unlockDuration).send();
            Boolean isUnlocked = personalUnlockAccount.accountUnlocked();

            System.out.println("account unlock " + isUnlocked);
            return isUnlocked;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
