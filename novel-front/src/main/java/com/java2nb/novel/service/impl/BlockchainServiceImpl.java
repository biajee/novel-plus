package com.java2nb.novel.service.impl;

import com.java2nb.novel.core.bean.ResultBean;
import com.java2nb.novel.core.enums.ResponseStatus;
import com.java2nb.novel.core.exception.BusinessException;
import com.java2nb.novel.service.BlockchainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.*;
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
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.tx.ChainId;
import org.web3j.utils.Numeric;
import org.web3j.utils.Convert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.java2nb.novel.core.config.BlockchainProperties;
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
    private static final String emptyAddress = "0x0000000000000000000000000000000000000000";


    private final BlockchainProperties networkConfig = null;

    private int defaultGasPrice = 3000;
    private static final int defaultTokenDecimal = 18;
    private static final int defaultGasLimit = 21000;// for moac TX only, Ether is 21000
    private static final int defaultTokenGasLimit = 60000;// for moac TX only, Ether is 21000

    private static byte defaultBlockchainID = 100;
    public static String LOCALNODE = "http://127.0.0.1:8545/";
//    public static String TESTNET = "https://web3.testnet.moacchain.net";//"http://34.217.90.193:8932";//"http://gateway.moac.io/testnet";
    //Mainnet:   http://web3.moac.moacchain.net, China,  https://web3.moac.moacchain.net.
    //Testnet:   http://gateway.moac.io/testnet, China,https://web3.testnet.moacchain.net
    //Local node: http://localhost:8545
    public BlockchainServiceImpl() {


        //This service is running on the address passed as a parameter below
        if ( networkConfig == null){
            //default uses local node as config
            this.web3j = Web3j.build(new HttpService(LOCALNODE));
            this.admin = Admin.build(new HttpService(LOCALNODE));
            log.info("BlockchainServiceImpl: using LOCAL NODE===============" );
        }else{
            this.web3j = Web3j.build(new HttpService(networkConfig.getNetwork()));
            this.admin = Admin.build(new HttpService(networkConfig.getNetwork()));
            this.defaultGasPrice = networkConfig.getDefaultGasPrice();
            log.info("BlockchainServiceImpl:"+networkConfig.getNetwork()+" info!!!" );
        }

        // Check to make sure the defaultBlockchainID matches the connecting node
        try {
            EthChainId chainId = web3j.ethChainId().send();
            log.debug("查询ChainID " +chainId.getChainId());
//            if (chainId.getChainId() != defaultBlockchainID) defaultBlockchainID = chainId.getChainId();
        }catch (IOException e) {
            e.printStackTrace();
        }

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
     * 增加检测输入账户的合理性，抛出地址错误
     */
    @Override
    public BigInteger getAccountBalance(String accountAddress) {
        BigInteger balance = null;

        if (WalletUtils.isValidAddress(accountAddress)){
            try {
                EthGetBalance ethGetBalance = web3j.ethGetBalance(accountAddress, DefaultBlockParameterName.LATEST).send();
                balance = ethGetBalance.getBalance();
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.debug("查询原生币address " + accountAddress + " balance " + balance + "wei");
        }else{
            throw new BusinessException(ResponseStatus.BLOCKCHAIN_ACCOUNT_ERROR);
        }

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
        log.debug("查询通证："+tokenAddress+" 账户 " + accountAddress + " balance " + balanceValue + " decimal");
        // debug only
        if (networkConfig != null){
            log.info("getTokenBalance:"+networkConfig.getName()+" info!!!" );
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
        log.debug("查询通证："+tokenAddress+" 名称 " + name);
        return name;
    }

    /**
     * 查询代币精度
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public int getTokenDecimals(String contractAddress) {
        String methodName = "decimals";
        String fromAddr = emptyAddress;
        int decimal = 0;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint8> typeReference = new TypeReference<Uint8>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            decimal = Integer.parseInt(results.get(0).getValue().toString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return decimal;
    }

    /**
     * Admin: 创建新账号
     * 使用web3j本身的crypto来产生钱包文件
     * TODO：研究一下安全问题
     */
    @Override
    public String createNewAccount(String password) {
//        try {
//            NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
//            String address = newAccountIdentifier.getAccountId();
//            log.debug("生成new account address " + address);
//            return address;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
        WalletFile walletFile;
        try {
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            walletFile = Wallet.createStandard(password, ecKeyPair);
            log.debug("新钱包 address " + walletFile.getAddress());
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            String jsonStr = objectMapper.writeValueAsString(walletFile);
            log.debug("keystore json file created");
            return jsonStr;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String createBookTokenContract(String fromUserAddress) {

        return fromUserAddress;
    }

    /**
     * 从用户地址转移指定数额的token到指定地址
     *
     * @param bookTokenAddress 通证合约地址
     * @param fromAddress      拥有通证的用户地址
     * @param toAddress 目标地址
     * @param inValue 通证数量
     * @param privateKey 发送账户的私钥
     * @return 交易的HASH值
     * */
    @Override
    public String transferBookToken(String bookTokenAddress, String fromAddress, String toAddress, double inValue, String privateKey) {
        String txHash = null;

        try {

            String methodName = "transfer";
            List<Type> inputParameters = new ArrayList<>();
            List<TypeReference<?>> outputParameters = new ArrayList<>();

            Address tAddress = new Address(toAddress);

            // Convert the input value to int with token decimal setting
            BigDecimal scalingFactor = BigDecimal.TEN.pow(defaultTokenDecimal);
            BigInteger amount = BigDecimal.valueOf(inValue).multiply(scalingFactor).toBigInteger();
            log.debug("Token amount before:"+BigDecimal.valueOf(inValue)+" after "+BigDecimal.valueOf(inValue).multiply(scalingFactor));
//            BigInteger amount = BigDecimal.valueOf(inValue).toBigInteger();
            Uint256 value = new Uint256(amount);
            log.debug("Token amount:"+amount+" value "+value.getValue());
            inputParameters.add(tAddress);
            inputParameters.add(value);

            TypeReference<Bool> typeReference = new TypeReference<Bool>() {
            };
            outputParameters.add(typeReference);

            Function function = new Function(methodName, inputParameters, outputParameters);

            String data = FunctionEncoder.encode(function);

            EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).sendAsync().get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            BigInteger gasPrice = Convert.toWei(BigDecimal.valueOf(defaultGasPrice), Convert.Unit.GWEI).toBigInteger();

            log.debug("Token info:"+nonce+" amount "+value.getTypeAsString());
            // Create the Contract call TX and sign with private key
            //
            String signedData;

            signedData = signTransaction(nonce, gasPrice, BigInteger.valueOf(defaultGasLimit), bookTokenAddress,
                    (BigInteger)null, data, defaultBlockchainID, privateKey);
            if (signedData != null) {
                log.debug("Token SignedData:"+signedData);
                EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedData).send();
                txHash = ethSendTransaction.getTransactionHash();
                log.debug("Token TXhash:"+txHash);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return txHash;

    }

    /**
     * Admin: 发送原生币交易
     * 使用钱包对象签名并发送原生币交易
     */
    @Override
    public String sendSignedTransaction(String srcAddress, String toAddress, double amount, String privateKey) {
        BigInteger nonce;
        String txHash="";

        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            //找到最新的nonce
            ethGetTransactionCount = web3j.ethGetTransactionCount(srcAddress, DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ethGetTransactionCount == null) return null;
        nonce = ethGetTransactionCount.getTransactionCount();
        log.debug("srcAddress:"+srcAddress+" toAddress "+toAddress+" : "+amount+" nonce: "+nonce);

        // Use predefined chainID
        String data = "";//Optional
        byte chainId = defaultBlockchainID;//ChainId.ROPSTEN;//测试网络

        BigInteger gasPrice = Convert.toWei(BigDecimal.valueOf(defaultGasPrice), Convert.Unit.GWEI).toBigInteger();
        BigInteger gasLimit = BigInteger.valueOf(defaultGasLimit);

        BigInteger value = Convert.toWei(BigDecimal.valueOf(amount), Convert.Unit.ETHER).toBigInteger();

        String signedData;
        try {
            signedData = signTransaction(nonce, gasPrice, gasLimit, toAddress, value, data, chainId, privateKey);
            if (signedData != null) {
                log.debug("SignedData:"+signedData);
                EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedData).send();
                txHash = ethSendTransaction.getTransactionHash();
                log.debug("TXhash:"+txHash);
            }
            return txHash;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对输入交易使用私钥进行签名
     */
    public static String signTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
                                         BigInteger value, String data, byte chainId, String privateKey) throws IOException {
        byte[] signedMessage;
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                to,
                value,
                data);

        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Credentials credentials = Credentials.create(ecKeyPair);

        if (chainId > ChainId.NONE) {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        } else {
            // Should not be used, need throw error
            signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        }

        String hexValue = Numeric.toHexString(signedMessage);
        return hexValue;
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

            log.info("account unlock " + isUnlocked);
            return isUnlocked;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解锁钱包文件
     * @param keystore 用户钱包文件的String
     * @param password 用户钱包密码
     * @return privateKey
     */
    @Override
    public String decryptWallet(String keystore, String password) {
        String privateKey = null;
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        log.info("decryptWallet 钱包密码：<"+password+">");
        try {
            WalletFile walletFile = objectMapper.readValue(keystore, WalletFile.class);
            ECKeyPair ecKeyPair = null;
            ecKeyPair = Wallet.decrypt(password, walletFile);
            privateKey = ecKeyPair.getPrivateKey().toString(16);
        } catch (CipherException e) {
            if ("Invalid password provided".equals(e.getMessage())) {
                log.info("钱包密码错误");
            }
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // 返回标明为HEX
        return "0x"+privateKey;
    }

    /**
     * 使用内置的随机数和用户的公钥地址，验证用户签名，
     * @param publicAddress 用户钱包文件的String
     * @param signature 用户钱包密码
     * @return 返回成功与否
     */
    @Override
    public Boolean verifySignature(String publicAddress, String signature)
    {
        return false;

    }
}
