package com.java2nb.novel.mapper;

import com.java2nb.novel.entity.BookToken;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

import javax.annotation.Generated;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.java2nb.novel.mapper.BookTokenDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Mapper
public interface BookTokenMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, bookId, tokenName, tokenSymbol, tokenAddress, tokenTxhash, tokenSupply, tokenCreator, tokenDecimals, createTime);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<BookToken> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("BookTokenResult")
    Optional<BookToken> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="BookTokenResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="book_id", property="bookId", jdbcType=JdbcType.BIGINT),
        @Result(column="token_name", property="tokenName", jdbcType=JdbcType.VARCHAR),
        @Result(column="token_symbol", property="tokenSymbol", jdbcType=JdbcType.VARCHAR),
        @Result(column="token_address", property="tokenAddress", jdbcType=JdbcType.VARCHAR),
        @Result(column="token_txhash", property="tokenTxhash", jdbcType=JdbcType.VARCHAR),
        @Result(column="token_supply", property="tokenSupply", jdbcType=JdbcType.INTEGER),
        @Result(column="token_creator", property="tokenCreator", jdbcType=JdbcType.VARCHAR),
        @Result(column="token_decimals", property="tokenDecimals", jdbcType=JdbcType.TINYINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<BookToken> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, bookToken, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, bookToken, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(BookToken record) {
        return MyBatis3Utils.insert(this::insert, record, bookToken, c ->
            c.map(id).toProperty("id")
            .map(bookId).toProperty("bookId")
            .map(tokenName).toProperty("tokenName")
            .map(tokenSymbol).toProperty("tokenSymbol")
            .map(tokenAddress).toProperty("tokenAddress")
            .map(tokenTxhash).toProperty("tokenTxhash")
            .map(tokenCreator).toProperty("tokenCreator")
            .map(tokenSupply).toProperty("tokenSupply")
            .map(tokenDecimals).toProperty("tokenDecimals")
            .map(createTime).toProperty("createTime")
        );
    }


    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(BookToken record) {
        return MyBatis3Utils.insert(this::insert, record, bookToken, c ->
            c.map(id).toPropertyWhenPresent("id", record::getId)
            .map(bookId).toPropertyWhenPresent("bookId", record::getBookId)
            .map(tokenName).toPropertyWhenPresent("tokenName", record::getTokenName)
            .map(tokenSymbol).toPropertyWhenPresent("tokenSymbol", record::getTokenSymbol)
            .map(tokenAddress).toPropertyWhenPresent("tokenAddress", record::getTokenAddress)
            .map(tokenTxhash).toPropertyWhenPresent("tokenTxhash", record::getTokenTxhash)
            .map(tokenCreator).toPropertyWhenPresent("tokenCreator", record::getTokenCreator)
            .map(tokenSupply).toPropertyWhenPresent("tokenSupply", record::getTokenSupply)
            .map(tokenDecimals).toPropertyWhenPresent("tokenDecimals", record::getTokenDecimals)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<BookToken> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, bookToken, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<BookToken> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, bookToken, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<BookToken> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, bookToken, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<BookToken> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, bookToken, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(BookToken record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(bookId).equalTo(record::getBookId)
                .set(tokenName).equalTo(record::getTokenName)
                .set(tokenSymbol).equalTo(record::getTokenSymbol)
                .set(tokenAddress).equalTo(record::getTokenAddress)
                .set(tokenTxhash).equalTo(record::getTokenTxhash)
                .set(tokenCreator).equalTo(record::getTokenCreator)
                .set(tokenSupply).equalTo(record::getTokenSupply)
                .set(tokenDecimals).equalTo(record::getTokenDecimals)
                .set(createTime).equalTo(record::getCreateTime);
    }


    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(BookToken record) {
        return update(c ->
            c.set(bookId).equalTo(record::getBookId)
            .set(tokenName).equalTo(record::getTokenName)
            .set(tokenSymbol).equalTo(record::getTokenSymbol)
            .set(tokenAddress).equalTo(record::getTokenAddress)
            .set(tokenTxhash).equalTo(record::getTokenTxhash)
            .set(tokenSupply).equalTo(record::getTokenSupply)
            .set(tokenCreator).equalTo(record::getTokenCreator)
            .set(tokenDecimals).equalTo(record::getTokenDecimals)
            .set(createTime).equalTo(record::getCreateTime)
            .where(id, isEqualTo(record::getId))
        );
    }

}