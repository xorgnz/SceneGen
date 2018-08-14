package org.memehazard.wheel.query.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

public class QueryCacheLine implements Serializable
{
    private static final long serialVersionUID     = 1L;
    private Integer           id;
    private String            parameterValueString = "";
    private Query             query                = null;
    private String            result               = "";
    private Date              retrieved            = new Date(0);


    public QueryCacheLine()
    {
    }


    public QueryCacheLine(Query query, String result, Date retrieved, String parameterValueString)
    {
        this.query = query;
        this.result = result;
        this.retrieved = retrieved;
        this.parameterValueString = parameterValueString;
    }


    public Integer getId()
    {
        return id;
    }


    public String getParameterValueString()
    {
        return parameterValueString;
    }


    public Query getQuery()
    {
        return query;
    }


    public String getResult()
    {
        return result;
    }


    public Date getRetrieved()
    {
        return retrieved;
    }


    protected void setId(Integer id)
    {
        this.id = id;
    }


    public void setParameterValueString(String parameterValueString)
    {
        this.parameterValueString = parameterValueString;
    }


    public void setParamValueMap(Map<String, String> paramValueMap)
    {
        this.parameterValueString = buildParameterValueString(paramValueMap);
    }


    public void setQuery(Query query)
    {
        this.query = query;
    }


    public void setResult(String result)
    {
        this.result = result;
    }


    public void setRetrieved(Date retrieved)
    {
        this.retrieved = retrieved;
    }


    @Override
    public String toString()
    {
        StrBuilder sb = new StrBuilder();

        SimpleDateFormat sdf = new SimpleDateFormat();
        sb.append(String.format("QueryCacheLine: %s (%d), retrieved %s. %s",
                parameterValueString, id, sdf.format(retrieved), (query != null ? " Attached" : " Not Attached")));

        return sb.toString();
    }


    public static String buildParameterValueString(Map<String, String> map)
    {
        String[] parameters = new String[map.size()];

        int i = 0;
        for (String key : map.keySet())
        {
            parameters[i] = key + "=" + map.get(key);
            i++;
        }

        return StringUtils.join(parameters, "&");
    }
}
