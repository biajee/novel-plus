package com.java2nb.novel.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class UserWalletDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final UserWallet userWallet = new UserWallet();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> id = userWallet.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> userId = userWallet.userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> userAddress = userWallet.userAddress;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> privateKey = userWallet.privateKey;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> keystore = userWallet.keystore;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class UserWallet extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> userId = column("user_id", JDBCType.BIGINT);

        public final SqlColumn<String> userAddress = column("user_address", JDBCType.VARCHAR);

        public final SqlColumn<String> privateKey = column("private_key", JDBCType.VARCHAR);

        public final SqlColumn<String> keystore = column("keystore", JDBCType.VARCHAR);

        public UserWallet() {
            super("user_wallet");
        }
    }
}