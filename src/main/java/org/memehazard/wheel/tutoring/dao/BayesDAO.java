package org.memehazard.wheel.tutoring.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;
import org.memehazard.wheel.tutoring.model.BayesValue;
import org.memehazard.wheel.tutoring.model.DomainModelNode;

public interface BayesDAO extends MyBatisDAO
{
    public void delete(@Param("domainId") long domainId, @Param("studentId") long studentId);


    public void deleteAllByDomainId(@Param("domainId") long domainId);


    public void deleteAllByMultipleDomainIds(@Param("domainIds") List<Long> domainIds);


    public void deleteAllByMultipleDomainModelNodes(@Param("nodes") DomainModelNode[] nodes);


    public void deleteByMultipleDomainModelNodes(@Param("nodes") DomainModelNode[] nodes, @Param("studentId") long studentId);


    public BayesValue get(@Param("domainId") long domainId, @Param("studentId") long studentId);


    public List<BayesValue> list(@Param("domainIds") List<Long> domainIds, @Param("studentId") long studentId);


    public List<BayesValue> listForAllStudents(@Param("domainId") long domainId);


    public List<BayesValue> listByMultipleDomainModelNodes(@Param("nodes") DomainModelNode[] nodes, @Param("studentId") long studentId);


    public void set(@Param("domainId") long domainId, @Param("studentId") long studentId, @Param("p") double p, @Param("timestamp") Date timestamp);


    public void setMultiple(@Param("bvals") List<BayesValue> values);


    public void setForMultipleStudents(@Param("domainId") long domainId, @Param("studentIds") List<Long> studentIds, @Param("p") double p,
            @Param("timestamp") Date timestamp);


    public List<BayesValue> listByCurriculum(Long nodeId);
}
