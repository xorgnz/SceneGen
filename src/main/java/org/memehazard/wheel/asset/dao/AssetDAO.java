package org.memehazard.wheel.asset.dao;

import java.util.List;

import org.memehazard.wheel.asset.model.Asset;
import org.memehazard.wheel.core.persist.mybatis.MyBatisDAO;

public interface AssetDAO extends MyBatisDAO
{

    public void add(Asset a);


    public void delete(int id);


    public void deleteStyleTags(int id);


    public List<Asset> listAll();


    public List<Integer> listIdsByAssetSet(int id);


    public List<Asset> listByAssetSet(int id);


    public Asset get(int id);


    public void update(Asset a);


    public void updateStyleTags(Asset a);
}
