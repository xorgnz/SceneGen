/**
 * 
 */
package org.memehazard.wheel.viewer.view;

import java.util.Map;

import org.apache.commons.lang.text.StrBuilder;
import org.memehazard.wheel.asset.view.AssetDescriptor;
import org.memehazard.wheel.query.view.EntityDescriptor;
import org.memehazard.wheel.style.model.Stylable;
import org.memehazard.wheel.style.model.Style;
import org.memehazard.wheel.style.view.StyleDescriptor;


/**
 * @author xorgnz
 * 
 */
public class SceneObjectDescriptor implements Stylable
{
    protected AssetDescriptor  asset   = new AssetDescriptor();
    protected EntityDescriptor entity  = new EntityDescriptor();
    protected int              id;
    protected StyleDescriptor  style   = null;
    protected boolean          visible = true;


    public SceneObjectDescriptor()
    {
        this.id = -1;
    }


    public SceneObjectDescriptor(AssetDescriptor asset)
    {
        if (asset != null)
        {
            this.asset = asset;
            this.entity = new EntityDescriptor(this.asset.getEntityId(), this.getAsset().getName(), "Unknown");
            this.id = asset.getId();
        }

        this.style = new StyleDescriptor();
    }


    public SceneObjectDescriptor(AssetDescriptor asset, EntityDescriptor entity)
    {
        this.entity = entity;
        this.style = new StyleDescriptor();
        this.id = entity.getId();

        if (asset != null)
            this.asset = asset;
    }


    public AssetDescriptor getAsset()
    {
        return asset;
    }


    @Override
    public Map<String, Object> getData()
    {
        return entity.getData();
    }


    public EntityDescriptor getEntity()
    {
        return entity;
    }


    public int getId()
    {
        return id;
    }


    public StyleDescriptor getStyle()
    {
        return style;
    }


    @Override
    public String[] getStyleTags()
    {
        return this.asset.getStyleTags();
    }


    public boolean isMissing()
    {
        return !this.asset.hasModelFile();
    }


    public boolean isValid()
    {
        return entity != null && entity.isValid() && asset != null && asset.hasModelFile();
    }


    public boolean isVisible()
    {
        return visible;
    }


    @Override
    public boolean needsStyle()
    {
        return !this.isMissing();
    }


    public void setId(int id)
    {
        this.id = id;
    }


    @Override
    public void setStyle(Style s)
    {
        this.style = new StyleDescriptor(s);
    }


    public void setStyle(StyleDescriptor style)
    {
        this.style = style;
    }


    @Override
    public String toString()
    {
        StrBuilder sb = new StrBuilder();
        sb.appendln("SceneObjectDescriptor");
        sb.appendln("-> " + (this.entity != null ? this.entity.toString() : "No entity"));
        sb.appendln("-> " + (this.asset != null ? this.asset.toString() : "No asset"));
        sb.appendln("-> " + (this.style != null ? this.style.toString() : "No style"));

        return sb.toString();
    }
}
