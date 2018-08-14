package org.memehazard.wheel.explorer.view;

import java.util.ArrayList;
import java.util.List;

import org.memehazard.wheel.asset.model.Asset;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.view.EntityDescriptor;
import org.memehazard.wheel.style.model.Stylable;
import org.memehazard.wheel.style.model.Style;
import org.memehazard.wheel.style.view.StyleDescriptor;

public class RenderableEntityDescriptor extends EntityDescriptor implements Stylable
{
    private String          filename  = null;
    private StyleDescriptor style     = null;
    private List<String>    styleTags = new ArrayList<String>();


    public RenderableEntityDescriptor(Entity e, Asset asset)
    {
        super(e);

        if (asset != null)
        {
            this.filename = asset.getObjFilename();
            this.styleTags.addAll(asset.getStyleTags());
        }

    }


    public String getFilename()
    {
        return filename;
    }


    public StyleDescriptor getStyle()
    {
        return style;
    }


    @Override
    public void setStyle(Style style)
    {
        this.style = new StyleDescriptor(style);
    }


    @Override
    public boolean needsStyle()
    {
        return filename != null;
    }


    @Override
    public String[] getStyleTags()
    {
        return styleTags.toArray(new String[] {});
    }
}
