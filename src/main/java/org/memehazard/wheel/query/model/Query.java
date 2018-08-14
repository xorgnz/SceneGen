package org.memehazard.wheel.query.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;


public class Query implements Serializable
{
    private static final long    serialVersionUID = 1L;
    private List<QueryCacheLine> cacheLines       = new ArrayList<QueryCacheLine>();
    private String               description;
    private int                  id;
    private String               name;
    private List<QueryParameter> parameters       = new ArrayList<QueryParameter>();
    private int                  queryId;
    private String[]             tags;


    public Query()
    {
    }


    public Query(String name, String description, int queryId, String tags)
    {
        this.name = name;
        this.description = description;
        this.queryId = queryId;
        this.setTags(tags);
    }


    public void addParameter(QueryParameter qp)
    {
        this.parameters.add(qp);
    }


    public void clearParameters()
    {
        this.parameters.clear();
    }


    public List<QueryCacheLine> getCacheLines()
    {
        return cacheLines;
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


    public List<QueryParameter> getParameters()
    {
        return parameters;
    }


    public int getQueryId()
    {
        return queryId;
    }


    public String getTags()
    {
        return StringUtils.join(tags, ":");
    }


    public String[] getTagsAsArray()
    {
        return tags;
    }


    public void setCacheLines(List<QueryCacheLine> cacheLines)
    {
        this.cacheLines.clear();
        this.cacheLines.addAll(cacheLines);
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


    public void setParameters(List<QueryParameter> params)
    {
        this.parameters.clear();
        this.parameters.addAll(params);
    }


    public void setParameters(QueryParameter... params)
    {
        this.parameters.clear();
        for (QueryParameter qp : params)
            this.parameters.add(qp);
    }


    public void setQueryId(int queryId)
    {
        this.queryId = queryId;
    }


    public void setTags(String tagString)
    {
        tags = tagString.split(":");
    }


    @Override
    public String toString()
    {
        StrBuilder sb = new StrBuilder();

        sb.append(String.format("Query: %s (%d), QI ID %d", name, id, queryId));

        if (parameters.size() + cacheLines.size() > 0)
            sb.appendln("");

        for (QueryParameter qp : parameters)
            sb.appendln(qp.toString());

        for (QueryCacheLine qcl : cacheLines)
            sb.appendln(qcl.toString());

        return sb.toString();
    }
}
