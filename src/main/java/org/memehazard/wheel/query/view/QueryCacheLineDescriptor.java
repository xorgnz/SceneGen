package org.memehazard.wheel.query.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.text.StrBuilder;
import org.memehazard.wheel.query.model.QueryCacheLine;


public class QueryCacheLineDescriptor
{
    private Integer id;
    private String  parameterValueString = "";
    private String  result               = "";
    private Date    retrieved;


    public QueryCacheLineDescriptor(QueryCacheLine qcl)
    {
        this.id = qcl.getId();
        this.parameterValueString = qcl.getParameterValueString();
        this.result = qcl.getResult();
        this.retrieved = qcl.getRetrieved();
    }


    public Integer getId()
    {
        return id;
    }


    public String getParameterValueString()
    {
        return parameterValueString;
    }


    public String getResult()
    {
        return result;
    }


    public Date getRetrieved()
    {
        return retrieved;
    }


    public void setId(Integer id)
    {
        this.id = id;
    }


    public void setParameterValueString(String parameterValueString)
    {
        this.parameterValueString = parameterValueString;
    }


    public void setResult(String result)
    {
        this.result = result;
    }


    public void setRetrieved(Date retrieved)
    {
        this.retrieved = retrieved;
    }


    public String toString()
    {
        StrBuilder sb = new StrBuilder();

        SimpleDateFormat sdf = new SimpleDateFormat();
        sb.append("QueryCacheLineDescriptor: " + parameterValueString + ", retrieved " + sdf.format(retrieved));

        return sb.toString();
    }
}
