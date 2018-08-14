package org.memehazard.wheel.tutoring.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;
import org.memehazard.wheel.tutoring.model.Event;

public interface EventDAO extends MyBatisDAO
{
    public void add(Event e);


    public void delete(long id);


    public Event get(long id);


    public void update(Event e);


    public void deleteByCurriculumAndStudent(@Param("curriculumId") long curriculumId, @Param("studentId") long studentId);


    public List<Event> listByCurriculumAndStudent(@Param("curriculumId") long curriculumId, @Param("studentId") long studentId);
}
