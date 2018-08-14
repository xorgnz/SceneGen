/**
 * 
 */
package org.memehazard.wheel.style.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.memehazard.Helper;

/**
 * @author xorgnz
 * 
 */

public class Stylesheet implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String            description      = "";
    private int               id;
    private String            name;
    private int               styleCount;
    private List<Style>       styles           = new ArrayList<Style>();
    private String[]          tags;


    public Stylesheet()
    {

    }


    public Stylesheet(String name, String description, String tags)
    {
        super();
        this.name = name;
        this.setTags(tags);

        if (description != null)
            this.description = description;
    }


    public void addStyle(Style s)
    {
        this.styles.add(s);
    }


    public String getDescription()
    {
        return description;
    }


    public int getId()
    {
        return id;
    }


    public String getName()
    {
        return name;
    }


    public int getStyleCount()
    {
        return styleCount;
    }


    public List<Style> getStyles()
    {
        return styles;
    }


    public String getTags()
    {
        return StringUtils.join(tags, ":");
    }


    public String[] getTagsAsArray()
    {
        return tags;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public void setId(int id)
    {
        this.id = id;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setStyleCount(int styleCount)
    {
        this.styleCount = styleCount;
    }


    public void setStyles(List<Style> styles)
    {
        this.styles.clear();
        this.styles.addAll(styles);
    }


    public void setTags(String tagString)
    {
        if (tagString != null)
            tags = tagString.split(":");
    }


    /**
     * Apply a stylesheet to a set of stylable objects.
     * - Inspect each object in turn and create StyleDescriptors for any matches
     * 
     * @param objects List of <code>Stylable</code> objects to apply styles to
     */
    public void styleObjects(List<? extends Stylable> objects)
    {
        List<Style> styles = this.getStyles();

        for (Stylable obj : objects)
            if (obj.needsStyle())
                for (Style style : styles)
                    if (style.apply(obj))
                        break;

    }


    /**
     * Apply a stylesheet to an array of <code>Stylable</code> objects.
     * 
     * @param objects
     */
    public void styleObjects(Stylable[] objects)
    {
        List<Stylable> list = new ArrayList<Stylable>();
        list.addAll(Helper.asList(objects));
    }


    @Override
    public String toString()
    {
        StrBuilder sb = new StrBuilder();

        sb.appendln("Stylesheet '" + name + "' : tags: " + tags);
        for (Style s : this.getStyles())
            sb.appendln("    " + s.toString());

        return sb.toString();
    }
}
