package com.java2nb.novel.entity;

import javax.annotation.Generated;
import java.util.Date;

/*
 * Data structure holds token for each book
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `book_id` bigint(20) DEFAULT NULL COMMENT '小说ID',
  `token_name` varchar(20) DEFAULT NULL COMMENT '通证名称',
  `token_symbol` varchar(20) DEFAULT NULL COMMENT '通证代码',
  `token_address` varchar(42) DEFAULT NULL COMMENT '通证合约地址',
  `token_txhash` varchar(100) DEFAULT NULL COMMENT '通证合约产生的交易HASH',
  `token_supply` bigint(20) DEFAULT NULL COMMENT '通证总量',
  `token_creator` varchar(42) DEFAULT NULL COMMENT '通证的拥有者或者管理者',
  `token_decimals` tinyint(4) DEFAULT '16' COMMENT '通证小数位置，默认使用以太坊的1e-16',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
 */
public class BookToken {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long bookId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String tokenName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String tokenSymbol;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String tokenAddress;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String tokenTxhash;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer tokenSupply;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String tokenCreator;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte tokenDecimals;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    public String getTokenTxhash() {
        return tokenTxhash;
    }

    public void setTokenTxhash(String tokenTxhash) {
        this.tokenTxhash = tokenTxhash;
    }

    public Integer getTokenSupply() {
        return tokenSupply;
    }

    public void setTokenSupply(Integer tokenSupply) {
        this.tokenSupply = tokenSupply;
    }

    public String getTokenCreator() {
        return tokenCreator;
    }

    public void setTokenCreator(String tokenCreator) {
        this.tokenCreator = tokenCreator;
    }

    public Byte getTokenDecimals() {
        return tokenDecimals;
    }

    public void setTokenDecimals(Byte tokenDecimals) {
        this.tokenDecimals = tokenDecimals;
    }


    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}