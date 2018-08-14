package org.memehazard.wheel.scene.dao;

import java.util.List;

import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;
import org.memehazard.wheel.scene.model.SceneFragmentMember;

public interface SceneFragmentMemberDAO extends MyBatisDAO
{
    public void add(SceneFragmentMember sfm);


    public void update(SceneFragmentMember sfm);


    public void delete(int id);


    public void deleteByFragment(int fragmentId);


    public SceneFragmentMember get(int id);


    public List<SceneFragmentMember> listAll();


    public List<SceneFragmentMember> listByFragment(int fragmentId);
}
