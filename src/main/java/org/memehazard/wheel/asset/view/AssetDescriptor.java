package org.memehazard.wheel.asset.view;

import org.apache.commons.lang.text.StrBuilder;
import org.memehazard.wheel.asset.model.Asset;

public class AssetDescriptor
{
    private AssetSetDescriptor assetSet    = new AssetSetDescriptor();
    private double[]           centroid    = new double[] { 0, 0, 0 };
    private Integer            entityId    = 0;
    private Integer            id          = 0;
    private double[]           max         = new double[] { -Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE };
    private double[]           min         = new double[] { Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE };
    private String             name        = "";
    private String             objFilename = "";
    private String[]           styleTags   = new String[] {};
    private String             x3dFilename = "";


    public AssetDescriptor()
    {
    }


    public AssetDescriptor(Asset asset)
    {
        if (asset != null)
        {
            this.entityId = asset.getEntityId();
            this.id = asset.getId();
            this.styleTags = asset.getStyleTags().toArray(new String[] {});
            this.objFilename = asset.getObjFilename();
            this.x3dFilename = asset.getX3dFilename();
            this.name = asset.getName();

            if (asset.getStats() != null)
            {
                this.centroid = asset.getStats().getCentroid();
                this.min = asset.getStats().getMin();
                this.max = asset.getStats().getMax();
            }

            if (asset.getAssetSet() != null)
                this.assetSet = new AssetSetDescriptor(asset.getAssetSet());
        }
    }


    public AssetSetDescriptor getAssetSet()
    {
        return assetSet;
    }


    public double[] getCentroid()
    {
        return centroid;
    }


    public Integer getEntityId()
    {
        return entityId;
    }


    public Integer getId()
    {
        return id;
    }


    public double[] getMax()
    {
        return max;
    }


    public double[] getMin()
    {
        return min;
    }


    public String getName()
    {
        return name;
    }


    public String getObjFilename()
    {
        return objFilename;
    }


    public String[] getStyleTags()
    {
        return styleTags;
    }


    public String getX3dFilename()
    {
        return x3dFilename;
    }


    public boolean hasModelFile()
    {
        return objFilename != null && ! objFilename.trim().equals("");
    }


    public void setAssetSet(AssetSetDescriptor set)
    {
        this.assetSet = set;
    }


    @Override
    public String toString()
    {
        StrBuilder sb = new StrBuilder();

        sb.appendln("AssetDescriptor");
        sb.appendln("\tFiles: OBJ (" + objFilename + "), X3D (" + x3dFilename + ")");
        sb.appendln("\tCentroid: (" + this.centroid[0] + ", " + this.centroid[1] + ", " + this.centroid[2] + ") ");
        sb.appendln("\tMin:(" + this.min[0] + " " + this.min[1] + " " + this.min[2] + ") ");
        sb.appendln("\tMax:(" + this.max[0] + " " + this.max[1] + " " + this.max[2] + ") ");

        return sb.toString();
    }
}
