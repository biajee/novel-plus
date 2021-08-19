package com.java2nb.novel.service.impl;

import com.java2nb.novel.service.BlockchainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;

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
    private static String emptyAddress = "0x0000000000000000000000000000000000000000";

//    private Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545/"));
    //Mainnet:   http://web3.moac.moacchain.net
    //Testnet:   http://gateway.moac.io/testnet
    //Local node: http://localhost:8545
    public BlockchainServiceImpl() {

        //This service is running on the address passed as a parameter below
        this.web3j = Web3j.build(new HttpService("http://gateway.moac.io/testnet"));
        this.admin = Admin.build(new HttpService("http://gateway.moac.io/testnet"));
    }

    /**
     * Web3j: 查询区块高度功能
     */
    @Override
    public BigInteger getBlockNumber() {
        BigInteger blockNumber = null;
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
     * Web3j: 查询原生币余额功能
     */
    @Override
    public BigInteger getAccountBalance(String accountAddress) {
        BigInteger balance = null;
        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(accountAddress, DefaultBlockParameterName.LATEST).send();
            balance = ethGetBalance.getBalance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("查询原生币address " + accountAddress + " balance " + balance + "wei");
        return balance;
    }

    /**
     * Web3j: 查询通证余额功能
     */
    @Override
    public BigInteger getTokenBalance(String tokenAddress, String accountAddress) {
        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address address = new Address(accountAddress);
        inputParameters.add(address);

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(accountAddress, tokenAddress, data);

        EthCall ethCall;
        BigInteger balanceValue = BigInteger.ZERO;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            balanceValue = (BigInteger) results.get(0).getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return balanceValue;
    }

    /**
     * Web3j: 查询通证名称功能
     */
    @Override
    public String getTokenName(String tokenAddress) {
        String methodName = "name";
        String name = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, tokenAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            name = results.get(0).getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return name;
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
