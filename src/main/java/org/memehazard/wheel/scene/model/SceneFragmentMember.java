package org.memehazard.wheel.scene.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.memehazard.wheel.asset.model.Asset;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.style.model.Stylable;
import org.memehazard.wheel.style.model.Style;

public class SceneFragmentMember implements Serializable, Stylable
{
    private static final long serialVersionUID = 3525004906708919295L;
    private Asset             asset            = new Asset();
    private Integer           fragmentId;
    private Integer           id;
    private Style             style            = new Style();
    private Transform         transform        = new Transform();
    private boolean           visible;
    private Entity            entity           = new Entity();


    public SceneFragmentMember()
    {
    }


    public SceneFragmentMember(SceneFragment fragment, boolean visible, Entity entity, Asset asset, Transform transform, Style style)
    {
        this.fragmentId = fragment.getId();
        this.visible = visible;

        this.entity = entity;

        this.asset = asset;

        if (transform == null)
            transform = new Transform();
        this.transform = transform;

        if (style == null)
            style = new Style();
        this.style = style;
    }


    public Asset getAsset()
    {
        return asset;
    }


    public int getFragmentId()
    {
        return fragmentId;
    }


    public int getId()
    {
        return id;
    }


    public Style getStyle()
    {
        return style;
    }


    public Transform getTransform()
    {
        return transform;
    }


    public boolean isVisible()
    {
        return visible;
    }


    public void setAsset(Asset asset)
    {
        this.asset = asset;
    }


    public void setAssetId(int assetId)
    {
        this.asset.setId(assetId);
    }


    public void setFragmentId(int fragmentId)
    {
        this.fragmentId = fragmentId;
    }


    public void setId(int id)
    {
        this.id = id;
    }


    public void setStyle(Style style)
    {
        this.style = style;
    }


    public void setTransform(Transform transform)
    {
        this.transform = transform;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    @Override
    public boolean needsStyle()
    {
        return asset != null;
    }


    @Override
    public String[] getStyleTags()
    {
        return asset.getStyleTags().toArray(new String[] {});
    }


    @Override
    public Map<String, Object> getData()
    {
        return new HashMap<String, Object>();
    }


    public Entity getEntity()
    {
        return entity;
    }


    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }
}
