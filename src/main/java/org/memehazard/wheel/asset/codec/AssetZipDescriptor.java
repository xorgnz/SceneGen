package org.memehazard.wheel.asset.codec;

public class AssetZipDescriptor
{
    public int    entityId;
    public String name;


    public AssetZipDescriptor(String name, Integer entityId)
    {
        this.name = name;
        this.entityId = entityId;
    }


    public int getEntityId()
    {
        return entityId;
    }


    public String getName()
    {
        return name;
    }


    public void setEntityId(int entityId)
    {
        this.entityId = entityId;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    @Override
    public String toString()
    {
        return name + "(" + entityId + ")";
    }
}