package org.memehazard.wheel.scene.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.memehazard.wheel.asset.model.AssetSet;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.view.QueryParameterValueDescriptor;
import org.memehazard.wheel.scene.model.SceneFragment;
import org.memehazard.wheel.scene.model.SceneFragmentMember;
import org.memehazard.wheel.style.model.Stylesheet;


public class SceneFragmentDescriptor
{
    private AssetSet                            assetSet;
    private int                                 id;
    private List<SceneFragmentMemberDescriptor> members          = new ArrayList<SceneFragmentMemberDescriptor>();
    private String                              name;
    private Query                               query;
    private List<QueryParameterValueDescriptor> queryParamValues = new ArrayList<QueryParameterValueDescriptor>();
    private Stylesheet                          stylesheet;
    private int                                 type;


    public SceneFragmentDescriptor(SceneFragment fragment)
    {
        this.id = fragment.getId();
        this.name = fragment.getName();
        this.assetSet = fragment.getAssetSet();
        this.stylesheet = fragment.getStylesheet();
        this.type = fragment.getType();
        this.query = fragment.getQuery();

        for (SceneFragmentMember member : fragment.getMembers())
            this.members.add(new SceneFragmentMemberDescriptor(member));

        // Parse the Parameter and Parameter Value strings to create a list of QPVDescriptors
        Map<String, String> parameterValueMap = fragment.parseParameterValues();
        for (String key : parameterValueMap.keySet())
            this.queryParamValues.add(new QueryParameterValueDescriptor(key, parameterValueMap.get(key)));
    }


    public AssetSet getAssetSet()
    {
        return assetSet;
    }


    public int getId()
    {
        return id;
    }


    public List<SceneFragmentMemberDescriptor> getMembers()
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


    public List<QueryParameterValueDescriptor> getQueryParamValues()
    {
        return queryParamValues;
    }


    public Stylesheet getStylesheet()
    {
        return stylesheet;
    }


    public int getType()
    {
        return type;
    }
}
