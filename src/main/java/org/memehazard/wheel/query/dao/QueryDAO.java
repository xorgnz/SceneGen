package org.memehazard.wheel.query.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.model.QueryParameter;

public interface QueryDAO extends MyBatisDAO
{
    public void add(Query obj);


    public void addParameters(@Param("queryId") int queryId, @Param("parameters") List<QueryParameter> parameters);


    public void delete(int id);


    public void deleteParametersByQuery(int queryId);


    public Query get(int id);


    public Query findByQueryId(int qid);


    public List<Query> listAll();


    public void update(Query obj);


    public void updateParameters(@Param("queryId") int queryId, @Param("parameters") List<QueryParameter> parameters);
}
