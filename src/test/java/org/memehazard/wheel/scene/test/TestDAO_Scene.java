package org.memehazard.wheel.scene.test;

import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;

public interface TestDAO_Scene extends MyBatisDAO
{
    public void prep_SceneDAO_crud();


    public void prep_SceneFacade_addSceneFragmentWithParams();


    public void prep_SceneFacade_getFullScene();


    public void prep_SceneFacade_updateSceneFragmentWithParams();


    public void prep_SceneFragmentDAO_crud();


    public void prep_SceneFragmentDAO_deleteByScene();


    public void prep_SceneFragmentDAO_listBySceneWithAssociations();


    public void prep_sceneFragmentMemberDAO_crud();


    public void prep_SceneFragmentMemberDAO_crud();


    public void prep_SceneFragmentMemberDAO_deleteByFragment();


    public void prep_SceneFragmentMemberDAO_listByFragment();
}
