package org.memehazard.wheel.query.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

public class QueryParameter implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String            label            = "";
    private Set<String>       tags             = new HashSet<String>();
    private String            variable         = "";


    // Default constructor necessary for Hibernate
    public QueryParameter()
    {

    }


    public QueryParameter(String variable, String label, Set<? extends String> tags)
    {
        this.setVariable(variable);
        this.setLabel(label);
        this.setTags(tags);
    }


    public void addTag(String tag)
    {
        tags.add(tag);
    }


    public String getLabel()
    {
        return label;
    }


    public Set<String> getTags()
    {
        return tags;
    }


    public String getTagString()
    {
        return StringUtils.join(tags, ":");
    }


    public String getVariable()
    {
        return variable;
    }


    public boolean isFlag_entityId()
    {
        return tags.contains("entityId");
    }


    public boolean isFlag_entityName()
    {
        return tags.contains("entityName");
    }


    public void removeTag(String tag)
    {
        tags.remove(tag);
    }


    public void setFlag_entityId(boolean flag)
    {
        if (flag)
            tags.add("entityId");
        else
            tags.remove("entityId");
    }


    public void setFlag_entityName(boolean flag)
    {
        if (flag)
            tags.add("entityName");
        else
            tags.remove("entityName");
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public void setTags(Set<? extends String> tags)
    {
        this.tags.clear();
        this.tags.addAll(tags);
    }


    public void setTagString(String tagString)
    {
        String[] tagArray = StringUtils.split(tagString, ":");

        this.tags.clear();

        for (String s : tagArray)
            if (!s.trim().equals(""))
                this.tags.add(s.trim());
    }


    public void setVariable(String variable)
    {
        this.variable = variable;
    }


    public String toString()
    {
        StrBuilder sb = new StrBuilder();

        sb.append("QueryParameter: " + variable + " | " + label + " | " + tags);

        return sb.toString();
    }
}
