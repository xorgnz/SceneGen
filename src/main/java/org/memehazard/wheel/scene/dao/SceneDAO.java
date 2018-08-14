package org.memehazard.wheel.scene.dao;

import java.util.List;

import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;
import org.memehazard.wheel.scene.model.Scene;


public interface SceneDAO extends MyBatisDAO
{
    public void add(Scene obj);


    public void delete(int id);


    /**
     * Retrieve the given <code>Scene</code>.
     * 
     * @param id
     * @return
     */
    public Scene get(int id);


    public List<Scene> listAll();


    public void update(Scene obj);
}
