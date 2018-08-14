package org.memehazard.wheel.query.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;
import org.memehazard.wheel.query.model.QueryCacheLine;

public interface QueryCacheLineDAO extends MyBatisDAO
{
    public void add(QueryCacheLine obj);


    public void delete(int id);


    public void deleteAll();


    public void deleteByQuery(int queryId);


    public QueryCacheLine get(int id);


    public QueryCacheLine getByQueryAndParam(@Param("queryId") int queryId, @Param("paramString") String paramString);


    public List<QueryCacheLine> listAll();


    public List<QueryCacheLine> listByQuery(int queryId);


    public void update(QueryCacheLine obj);
}
