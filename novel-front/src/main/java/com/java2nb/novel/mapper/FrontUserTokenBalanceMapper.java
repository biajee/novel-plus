package com.java2nb.novel.mapper;

import com.java2nb.novel.entity.UserTokenList;
import com.java2nb.novel.vo.BookReadHistoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 */
public interface FrontUserTokenBalanceMapper extends UserTokenListMapper {

    List<UserTokenList> listTokenBalance(@Param("userId") Long userId);

}
