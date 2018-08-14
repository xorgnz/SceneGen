package org.memehazard.wheel.textCompletion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;

public interface TextCompletionDAO extends MyBatisDAO
{
    public List<String> listCompletions(@Param("prefix") String prefix, @Param("limit") int limit);


    public List<String> listCompletionsWithPrefix(@Param("prefix") String prefix, @Param("limit") int limit);
}
