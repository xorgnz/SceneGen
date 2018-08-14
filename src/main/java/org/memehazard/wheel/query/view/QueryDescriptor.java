package org.memehazard.wheel.query.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.text.StrBuilder;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.model.QueryCacheLine;
import org.memehazard.wheel.query.model.QueryParameter;

public class QueryDescriptor
{
    private List<QueryCacheLineDescriptor> cacheLines = new ArrayList<QueryCacheLineDescriptor>();
    private String                         description;
    private Integer                        id;
    private String                         name;
    private List<QueryParameterDescriptor> parameters = new ArrayList<QueryParameterDescriptor>();
    private Integer                        queryId;
    private String                         tags;


    public QueryDescriptor(Query q)
    {
        this.id = q.getId();
        this.queryId = q.getQueryId();
        this.name = q.getName();
        this.description = q.getDescription();
        this.tags = q.getTags();

        for (QueryCacheLine qcl : q.getCacheLines())
            this.cacheLines.add(new QueryCacheLineDescriptor(qcl));

        for (QueryParameter qp : q.getParameters())
            this.parameters.add(new QueryParameterDescriptor(qp));
    }


    public List<QueryCacheLineDescriptor> getCacheLines()
    {
        return cacheLines;
    }


    public String getDescription()
    {
        return description;
    }


    public Integer getId()
    {
        return id;
    }


    public String getName()
    {
        return name;
    }


    public List<QueryParameterDescriptor> getParameters()
    {
        return parameters;
    }


    public Integer getQueryId()
    {
        return queryId;
    }


    public String getTags()
    {
        return tags;
    }


    public void setCacheLines(List<QueryCacheLineDescriptor> cacheLines)
    {
        this.cacheLines = cacheLines;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public void setId(Integer id)
    {
        this.id = id;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setParameters(List<QueryParameterDescriptor> parameters)
    {
        this.parameters = parameters;
    }


    public void setQueryId(Integer queryId)
    {
        this.queryId = queryId;
    }


    public void setTags(String tags)
    {
        this.tags = tags;
    }


    public String toString()
    {
        StrBuilder sb = new StrBuilder();

        sb.appendln(String.format("QueryDescriptor: %s (%d), QI ID: %d", name, id, queryId));

        for (QueryParameterDescriptor qpd : parameters)
            sb.appendln("    " + qpd.toString());

        for (QueryCacheLineDescriptor qcld : cacheLines)
            sb.appendln("    " + qcld.toString());

        return sb.toString();
    }
}
