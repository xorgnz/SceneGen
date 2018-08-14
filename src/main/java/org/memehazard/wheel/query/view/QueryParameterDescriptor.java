package org.memehazard.wheel.query.view;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.memehazard.wheel.query.model.QueryParameter;

public class QueryParameterDescriptor
{
    public String       label;
    public String       variable;
    private Set<String> tags = new HashSet<String>();


    public QueryParameterDescriptor(QueryParameter qp)
    {
        this.setLabel(qp.getLabel());
        this.setVariable(qp.getVariable());
        this.setTags(qp.getTags());
    }


    public QueryParameterDescriptor(String label, String variable, Set<? extends String> tags)
    {
        this.label = label;
        this.variable = variable;
        this.setTags(tags);
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

        sb.append("QueryParamDescriptor: " + variable + " | " + label);

        return sb.toString();
    }
}
