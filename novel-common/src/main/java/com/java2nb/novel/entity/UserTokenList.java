package com.java2nb.novel.entity;

import javax.annotation.Generated;
import java.util.Date;

public class UserTokenList {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long bookId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String bookName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String bookBlockchainAddress;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long tokenBalance;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long updateBlockHeight;

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
    public String getBookName() {
        return bookName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBookName(String inBookName) {
        this.bookName = inBookName == null ? null : inBookName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getBlockchainAddress() {
        return bookBlockchainAddress;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setBlockchainAddress(String inBlockchainAddress) {
        this.bookBlockchainAddress = inBlockchainAddress == null ? null : inBlockchainAddress.trim();
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
    public Long getUpdateBlockHeight() {
        return updateBlockHeight;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUpdateBlockHeight(Long inBlockHeight) {
        this.updateBlockHeight = inBlockHeight;
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