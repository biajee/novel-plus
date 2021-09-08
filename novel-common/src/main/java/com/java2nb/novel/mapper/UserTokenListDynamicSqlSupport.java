package com.java2nb.novel.mapper;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

import javax.annotation.Generated;
import java.sql.JDBCType;
import java.util.Date;

public final class UserTokenListDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final UserTokenList userTokenList = new UserTokenList();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> id = userTokenList.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> userId = userTokenList.userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> bookId = userTokenList.bookId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> tokenName = userTokenList.tokenName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> tokenAddress = userTokenList.tokenAddress;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> tokenBalance = userTokenList.tokenBalance;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> tokenIncome = userTokenList.tokenIncome;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> updateTime = userTokenList.updateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class UserTokenList extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> userId = column("user_id", JDBCType.BIGINT);

        public final SqlColumn<Long> bookId = column("book_id", JDBCType.BIGINT);

        public final SqlColumn<String> tokenName = column("token_name", JDBCType.VARCHAR);

        public final SqlColumn<String> tokenAddress = column("token_address", JDBCType.VARCHAR);

        public final SqlColumn<Long> tokenBalance = column("token_balance", JDBCType.BIGINT);

        public final SqlColumn<Long> tokenIncome = column("token_income", JDBCType.BIGINT);

        public final SqlColumn<Date> updateTime = column("update_time", JDBCType.TIMESTAMP);

        public UserTokenList() {
            super("user_token_list");
        }
    }
}