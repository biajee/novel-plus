package com.java2nb.novel.entity;

import javax.annotation.Generated;
import java.util.Date;

/*
 * change token_income from double to Long
 */
public class UserTokenList {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long bookId;


    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String tokenName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String tokenAddress;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long tokenBalance;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long tokenIncome;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date updateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getUserId() {
        return userId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getBookId() {
        return bookId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getTokenName() {
        return tokenName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getTokenAddress() {
        return tokenAddress;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getTokenBalance() {
        return tokenBalance;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTokenBalance(Long inBalance) {
        this.tokenBalance = inBalance;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getTokenIncome() {
        return tokenIncome;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTokenIncome(Long tokenIncome) {
        this.tokenIncome = tokenIncome;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getUpdateTime() {
        return updateTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}