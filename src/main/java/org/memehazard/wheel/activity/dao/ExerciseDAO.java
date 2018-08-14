package org.memehazard.wheel.activity.dao;

import java.util.List;

import org.memehazard.wheel.activity.model.Exercise;
import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;

public interface ExerciseDAO extends MyBatisDAO
{
    public void add(Exercise exercise);


    public void delete(int id);


    public List<Exercise> listAll();


    public List<Exercise> listByCurriculum(long id);


    public List<Exercise> listByActivityInstance(int id);
}
