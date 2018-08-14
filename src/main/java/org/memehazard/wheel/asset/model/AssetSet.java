package org.memehazard.wheel.asset.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class AssetSet implements Serializable
{
    private static final long serialVersionUID = -4956794390951951365L;

    private int               assetCount;
    private List<Asset>       assets           = new ArrayList<Asset>();
    private int               id;
    @NotEmpty(message = "{AssetSet.maintainer.empty}")
    private String            maintainer;
    @NotEmpty(message = "{AssetSet.name.empty}")
    private String            name;


    public AssetSet()
    {
    }


    public AssetSet(String name, String maintainer)
    {
        this.name = name;
        this.maintainer = maintainer;
    }


    public void addAsset(Asset asset)
    {
        assets.add(asset);
        asset.setAssetSet(this);
    }


    public int getAssetCount()
    {
        return assetCount;
    }


    public List<Asset> getAssets()
    {
        return assets;
    }


    public int getId()
    {
        return id;
    }


    public String getMaintainer()
    {
        return maintainer;
    }


    public String getName()
    {
        return name;
    }


    public void setAssetCount(int assetCount)
    {
        this.assetCount = assetCount;
    }


    public void setAssets(List<Asset> assets)
    {
        this.assets.clear();
        this.assets.addAll(assets);
    }


    public void setId(int id)
    {
        this.id = id;
    }


    public void setMaintainer(String maintainer)
    {
        this.maintainer = maintainer;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    @Override
    public String toString()
    {
        return name + "(" + id + ")";
    }
}
