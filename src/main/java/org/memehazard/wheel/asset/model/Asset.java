package org.memehazard.wheel.asset.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;


public class Asset implements Serializable
{
    public static final int   DEFAULT_ENTITY_ID = -1;

    private static final long serialVersionUID  = 1L;

    private AssetSet          assetSet          = null;
    private int               entityId          = DEFAULT_ENTITY_ID;
    private int               id                = 0;
    @NotEmpty(message = "{Asset.name.empty}")
    private String            name              = "";
    private String            objFilename       = "";
    private Asset3DStatistics stats             = null;
    private List<String>      styleTags         = new ArrayList<String>();
    private String            x3dFilename       = "";


    public Asset()
    {

    }


    public Asset(AssetSet assetSet, String name, int entityId, String objFilename, String x3dFilename, Asset3DStatistics stats,
            List<String> styleTags)
    {
        this.assetSet = assetSet;
        this.name = name;
        this.entityId = entityId;
        this.objFilename = objFilename;
        this.x3dFilename = x3dFilename;
        this.stats = stats;
        this.styleTags.addAll(styleTags);
    }


    public void addStyleTag(String tag)
    {
        this.styleTags.add(tag);
    }


    public AssetSet getAssetSet()
    {
        return assetSet;
    }


    public int getEntityId()
    {
        return entityId;
    }


    public int getId()
    {
        return id;
    }


    public String getName()
    {
        return name;
    }


    public String getObjFilename()
    {
        return objFilename;
    }


    public Asset3DStatistics getStats()
    {
        return stats;
    }


    public List<String> getStyleTags()
    {
        return this.styleTags;
    }


    public String getStyleTagsAsString()
    {
        return StringUtils.join(styleTags, ", ");
    }


    public String getX3dFilename()
    {
        return x3dFilename;
    }


    public void removeStyleTag(String tag)
    {
        this.styleTags.remove(tag);
    }


    public void setAssetSet(AssetSet assetSet)
    {
        this.assetSet = assetSet;
    }


    public void setEntityId(int entityId)
    {
        this.entityId = entityId;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setObjFilename(String objFilename)
    {
        this.objFilename = objFilename;
    }


    public void setStats(Asset3DStatistics stats)
    {
        this.stats = stats;
    }


    public void setStyleTags(List<String> styleTags)
    {
        this.styleTags.clear();
        this.styleTags.addAll(styleTags);
    }


    public void setX3dFilename(String x3dFilename)
    {
        this.x3dFilename = x3dFilename;
    }


    @Override
    public String toString()
    {
        return name + " ENTITY_ID[" + entityId + "] ID(" + id + ")";
    }


    public void setId(Integer id)
    {
        this.id = id;
    }
}
