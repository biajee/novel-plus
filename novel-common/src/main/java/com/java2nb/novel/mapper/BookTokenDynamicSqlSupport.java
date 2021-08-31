package com.java2nb.novel.mapper;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

import javax.annotation.Generated;
import java.sql.JDBCType;
import java.util.Date;

/*
 * Mapping the Java variable to mysql table columns
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
public final class BookTokenDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final BookToken bookToken = new BookToken();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> id = bookToken.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> bookId = bookToken.bookId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> tokenName = bookToken.tokenName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> tokenAddress = bookToken.tokenAddress;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> tokenSymbol = bookToken.tokenSymbol;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> tokenCreator = bookToken.tokenCreatorr;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> tokenTxhash = bookToken.tokenTxhash;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> tokenSupply = bookToken.tokenSupply;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> tokenDecimals = bookToken.tokenDecimals;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> createTime = bookToken.createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class BookToken extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> bookId = column("book_id", JDBCType.BIGINT);

        public final SqlColumn<String> tokenName = column("token_name", JDBCType.VARCHAR);

        public final SqlColumn<String> tokenAddress = column("token_address", JDBCType.VARCHAR);

        public final SqlColumn<String> tokenSymbol = column("token_symbol", JDBCType.VARCHAR);

        public final SqlColumn<String> tokenTxhash = column("token_txhash", JDBCType.VARCHAR);

        public final SqlColumn<String> tokenCreatorr = column("token_creator", JDBCType.VARCHAR);

        public final SqlColumn<Integer> tokenSupply = column("token_supply", JDBCType.INTEGER);

        public final SqlColumn<Byte> tokenDecimals = column("token_decimals", JDBCType.TINYINT);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);



        public BookToken() {
            super("book_token");
        }
    }
}