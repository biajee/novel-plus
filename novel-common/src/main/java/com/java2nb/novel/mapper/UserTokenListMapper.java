package com.java2nb.novel.mapper;

import com.java2nb.novel.entity.User;
import com.java2nb.novel.entity.UserTokenList;
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

//import static com.java2nb.novel.mapper.UserDynamicSqlSupport.*;
//import static com.java2nb.novel.mapper.UserDynamicSqlSupport.id;
//import static com.java2nb.novel.mapper.UserDynamicSqlSupport.updateTime;
import static com.java2nb.novel.mapper.UserTokenListDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Mapper
public interface UserTokenListMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, userId, bookId, tokenName, tokenAddress, tokenBalance, tokenIncome, updateTime);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<UserTokenList> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("UserTokenListResult")
    UserTokenList selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, userTokenList, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="UserTokenListResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="book_id", property="bookId", jdbcType=JdbcType.BIGINT),
        @Result(column="token_name", property="tokenName", jdbcType=JdbcType.VARCHAR),
        @Result(column="token_address", property="tokenAddress", jdbcType=JdbcType.VARCHAR),
        @Result(column="token_balance", property="tokenBalance", jdbcType=JdbcType.BIGINT),
        @Result(column="token_income", property="tokenIncome", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    }) List<UserTokenList> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(UserTokenList record) {
        return MyBatis3Utils.insert(this::insert, record, userTokenList, c ->
                c.map(id).toProperty("id")
                .map(userId).toProperty("userId")
                .map(bookId).toProperty("bookId")
                .map(tokenName).toProperty("tokenName")
                .map(tokenAddress).toProperty("tokenAddress")
                .map(tokenBalance).toProperty("tokenBalance")
                .map(tokenIncome).toProperty("tokenIncome")
                .map(updateTime).toProperty("updateTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(UserTokenList record) {
        return update(c ->
                c.set(userId).equalToWhenPresent(record::getUserId)
                .set(bookId).equalToWhenPresent(record::getBookId)
                .set(tokenName).equalToWhenPresent(record::getTokenName)
                .set(tokenAddress).equalToWhenPresent(record::getTokenAddress)
                .set(tokenBalance).equalToWhenPresent(record::getTokenBalance)
                .set(tokenIncome).equalToWhenPresent(record::getTokenIncome)
                .set(updateTime).equalToWhenPresent(record::getUpdateTime)
                .where(id, isEqualTo(record::getId))
        );
    }

}