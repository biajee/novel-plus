package com.java2nb.novel.entity;

import javax.annotation.Generated;

public class UserWallet {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String userAddress;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String privateKey;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String keystore;

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
    public String getUserAddress() {
        return userAddress;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress == null ? null : userAddress.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getPrivateKey() {
        return privateKey;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey == null ? null : privateKey.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getKeystore() {
        return keystore;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setKeystore(String keystore) {
        this.keystore = keystore == null ? null : keystore.trim();
    }
}