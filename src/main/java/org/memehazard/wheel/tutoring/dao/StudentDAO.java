package org.memehazard.wheel.tutoring.dao;

import java.util.List;

import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;
import org.memehazard.wheel.tutoring.model.Student;

public interface StudentDAO extends MyBatisDAO
{
    public List<Student> listAll();
}
