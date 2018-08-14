package org.memehazard.wheel.scene.dao;

import java.util.List;

import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;
import org.memehazard.wheel.scene.model.SceneFragment;


public interface SceneFragmentDAO extends MyBatisDAO
{
    public void addListType(SceneFragment obj);


    public void addMixedListType(SceneFragment obj);


    public void addQueryType(SceneFragment obj);


    public void delete(int id);


    public void deleteByScene(int sceneId);


    public SceneFragment get(int id);


    public List<SceneFragment> listAll();


    // TODO - Unit test for listBySceneWithAssociations
    public List<SceneFragment> listBySceneWithAssociations(int sceneId);


    // TODO - Unit Test for list type
    public void updateListType(SceneFragment obj);


    public void updateQueryType(SceneFragment obj);
}
