package org.memehazard.wheel.asset.view;

import org.memehazard.wheel.asset.model.AssetSet;

public class AssetSetDescriptor
{
    private int     id;
    private String  name;
    private String  maintainer;
    private Integer assetCount;


    // Blank constructor required to produce stubs for JSON
    public AssetSetDescriptor()
    {
        this.id = 0;
        this.name = "";
        this.maintainer = "";
        this.assetCount = 0;
    }


    public AssetSetDescriptor(AssetSet set)
    {
        this.id = set.getId();
        this.assetCount = set.getAssetCount();
        this.maintainer = set.getMaintainer();
        this.name = set.getName();
    }


    public int getAssetCount()
    {
        return assetCount;
    }


    public String getName()
    {
        return name;
    }


    public String getMaintainer()
    {
        return maintainer;
    }


    public int getId()
    {
        return id;
    }


}
