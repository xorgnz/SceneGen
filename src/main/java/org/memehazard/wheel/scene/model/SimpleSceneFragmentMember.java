package org.memehazard.wheel.scene.model;

import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.style.model.Style;

public class SimpleSceneFragmentMember
{
    private int    assetId;
    private Entity entity;
    private Style  style;


    public int getAssetId()
    {
        return assetId;
    }


    public Entity getEntity()
    {
        return entity;
    }


    public Style getStyle()
    {
        return style;
    }


    public boolean isVisible()
    {
        return visible;
    }


    public boolean visible;


    public void setAssetId(int assetId)
    {
        this.assetId = assetId;
    }


    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }


    public void setStyle(Style style)
    {
        this.style = style;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
}