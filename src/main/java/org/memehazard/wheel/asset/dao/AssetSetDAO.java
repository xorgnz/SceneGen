package org.memehazard.wheel.asset.dao;

import java.util.List;

import org.memehazard.wheel.asset.model.AssetSet;
import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;

public interface AssetSetDAO extends MyBatisDAO
{

    public void add(AssetSet a);


    public void delete(int id);


    public List<AssetSet> listAll();


    public List<AssetSet> listAllWithCounts();


    public List<AssetSet> listAllWithAssets();


    public AssetSet getByName(String name);


    public AssetSet get(int id);


    public void update(AssetSet a);
}
