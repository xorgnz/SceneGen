package org.memehazard.wheel.tutoring.test;

import java.util.List;

import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;
import org.memehazard.wheel.tutoring.model.BayesValue;
import org.memehazard.wheel.tutoring.model.Enrolment;

public interface TutoringTestMyBatisDAO extends MyBatisDAO
{
    public void createTestData();


    public void deleteAll();


    public List<BayesValue> listAllBayes();


    public List<Enrolment> listAllEnrolments();
}
