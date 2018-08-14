package org.memehazard.wheel.tutoring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;
import org.memehazard.wheel.tutoring.model.Enrolment;

public interface EnrolmentDAO extends MyBatisDAO
{
    public void add(Enrolment e);


    public void delete(@Param("curriculumId") long curriculumId, @Param("studentId") long studentId);


    public boolean isEnrolled(@Param("curriculumId") long curriculumId, @Param("studentId") long studentId);


    public List<Enrolment> listByCurriculum(@Param("curriculumId") long curriculumId);


    public List<Enrolment> listByStudent(@Param("studentId") long studentId);


    public List<Long> listStudentIdsByCurriculum(@Param("curriculumId") long curriculumId);
}
