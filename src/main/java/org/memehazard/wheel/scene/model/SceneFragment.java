package org.memehazard.wheel.scene.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.memehazard.wheel.asset.model.AssetSet;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.style.model.Stylesheet;


public class SceneFragment implements Serializable
{
    private static final long         serialVersionUID = -775680350673309531L;
    private AssetSet                  assetSet         = new AssetSet();
    private int                       id;
    private List<SceneFragmentMember> members          = new ArrayList<SceneFragmentMember>();
    private String                    name;
    private Query                     query            = new Query();
    private String                    queryParamString;
    private Scene                     scene            = new Scene();
    private Stylesheet                stylesheet       = new Stylesheet();
    private int                       type;


    public SceneFragment()
    {
    }


    public SceneFragment(Scene scene, String name, AssetSet assetSet, Stylesheet stylesheet, int type, Query query,
            String queryParams)
    {
        super();
        this.scene = scene;
        this.name = name;
        this.assetSet = assetSet;
        this.stylesheet = stylesheet;
        this.query = query;
        this.queryParamString = queryParams;
        this.type = type;
    }


    public AssetSet getAssetSet()
    {
        return assetSet;
    }


    public int getId()
    {
        return id;
    }


    public List<SceneFragmentMember> getMembers()
    {
        return members;
    }


    public String getName()
    {
        return name;
    }


    public Query getQuery()
    {
        return query;
    }


    public String getQueryParamString()
    {
        return queryParamString;
    }


    public Scene getScene()
    {
        return scene;
    }


    public Stylesheet getStylesheet()
    {
        return stylesheet;
    }


    public int getType()
    {
        return type;
    }


    public Map<String, String> parseParameterValues()
    {
        Map<String, String> map = new TreeMap<String, String>();

        if (queryParamString != null)
        {
            String[] parameters = StringUtils.split(queryParamString, "&");

            for (String parameter : parameters)
            {
                String[] parts = StringUtils.split(parameter, "=", 2);

                if (parts.length == 2)
                    map.put(parts[0], parts[1]);
                else
                    map.put(parts[0], "");
            }
        }
        return map;
    }


    public void setAssetSet(AssetSet assetSet)
    {
        this.assetSet = assetSet;
    }


    public void setAssetSetId(int id)
    {
        this.assetSet.setId(id);
    }


    public void setId(int id)
    {
        this.id = id;
    }


    public void setMembers(List<SceneFragmentMember> members)
    {
        this.members = members;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setQuery(Query query)
    {
        this.query = query;
    }


    public void setQueryId(int id)
    {
        this.query.setId(id);
    }


    public void setQueryParamString(String queryParams)
    {
        this.queryParamString = queryParams;
    }


    public void setQueryParamValues(Map<String, String> paramValueMap)
    {
        String[] parameters = new String[paramValueMap.size()];

        int i = 0;
        for (String key : paramValueMap.keySet())
        {
            parameters[i] = key + "=" + paramValueMap.get(key);
            i++;
        }

        this.queryParamString = StringUtils.join(parameters, "&");
    }


    public void setScene(Scene scene)
    {
        this.scene = scene;
    }


    public void setSceneId(int id)
    {
        this.scene.setId(id);
    }


    public void setStylesheet(Stylesheet stylesheet)
    {
        this.stylesheet = stylesheet;
    }


    public void setStylesheetId(int id)
    {
        this.stylesheet.setId(id);
    }


    public void setType(int type)
    {
        this.type = type;
    }


    @Override
    public String toString()
    {
        StrBuilder sb = new StrBuilder();

        sb.appendln("ScenePart: " + name + " [" + type + "]");
        sb.appendln("  scene: " + (scene != null ? scene.getName() : "null"));
        sb.appendln("  assetSet: " + (assetSet != null ? assetSet.getName() : "null"));
        sb.appendln("  stylesheet: " + (stylesheet != null ? stylesheet.getName() : "null"));
        sb.appendln(query != null ? "  query:" + (query.getName() + " -- " + queryParamString) : "none");

        return sb.toString();
    }
}
