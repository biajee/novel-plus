package com.java2nb.novel.controller;

import com.java2nb.novel.core.bean.ResultBean;
import com.java2nb.novel.service.BlockchainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 11797
 * 用于处理合约相关的操作
 */
@RequestMapping("contract")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ContractController {

    private final BlockchainService blockchainService;

    /**
     * 查询通证名称
     * */
    @GetMapping("getTokenName")
    public ResultBean getTokenName(String tokenAddress){
        return ResultBean.ok(blockchainService.getTokenName(tokenAddress));
    }

    /**
     * 查询账户的通证余额
     * */
    @GetMapping("getTokenBalance")
    public ResultBean getTokenBalance(String tokenAddress, String accountAddress){
        return ResultBean.ok(blockchainService.getTokenBalance(tokenAddress,accountAddress));
    }

    /**
     * 查询通证价格
     * */

    /**
     * 查询区块链最新区块高度
     * */
    @GetMapping("listBlockchainHeight")
    public ResultBean listBlockchainHeight(){
        return ResultBean.ok(blockchainService.getBlockNumber());
    }

    /**
     * 查询账户的原生通证余额
     * */
    @GetMapping("getAccountBalance")
    public ResultBean getAccountBalance(String accountAddress){
        return ResultBean.ok(blockchainService.getAccountBalance(accountAddress));
    }
}
