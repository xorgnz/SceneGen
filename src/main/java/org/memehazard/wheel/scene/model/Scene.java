package org.memehazard.wheel.scene.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.text.StrBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.memehazard.wheel.viewer.model.Viewpoint;


public class Scene implements Serializable
{
    private static final long   serialVersionUID = 5678760863087887661L;
    private List<SceneFragment> fragments        = new ArrayList<SceneFragment>();
    private int                 id;
    @NotEmpty(message = "{Scene.name.empty}")
    private String              name;
    private Viewpoint           viewpoint        = null;


    public Scene()
    {
        this.name = "";
    }


    public Scene(String name)
    {
        super();
        this.name = name;
    }


    public Scene(String name, Viewpoint viewpoint)
    {
        super();
        this.name = name;
        this.viewpoint = viewpoint;
    }


    public Set<Integer> getAllMemberEntityIds()
    {
        TreeSet<Integer> entityIds = new TreeSet<Integer>();
        for (SceneFragmentMember sfm : this.getAllMembers())
            entityIds.add(sfm.getEntity().getId());

        return entityIds;
    }


    public List<SceneFragmentMember> getAllMembers()
    {
        List<SceneFragmentMember> members = new ArrayList<SceneFragmentMember>();

        for (SceneFragment f : fragments)
        {
            if (f != null)
                members.addAll(f.getMembers());
        }

        return members;
    }


    public Map<Integer, SceneFragmentMember> getAllMembersMapByEntityId()
    {
        Map<Integer, SceneFragmentMember> map = new HashMap<Integer, SceneFragmentMember>();

        for (SceneFragmentMember sfm : getAllMembers())
            map.put(sfm.getEntity().getId(), sfm);

        return map;
    }


    public List<SceneFragment> getFragments()
    {
        return fragments;
    }


    public int getId()
    {
        return id;
    }


    public String getName()
    {
        return name;
    }


    public Viewpoint getViewpoint()
    {
        return viewpoint;
    }


    public void setFragments(List<SceneFragment> fragments)
    {
        this.fragments.clear();
        this.fragments.addAll(fragments);
    }


    public void setId(int id)
    {
        this.id = id;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setViewpoint(Viewpoint viewpoint)
    {
        this.viewpoint = viewpoint;
    }


    @Override
    public String toString()
    {
        StrBuilder sb = new StrBuilder();

        sb.appendln("Scene: " + name);
        sb.appendln("  scene fragments: ");
        if (fragments != null)
            for (SceneFragment scnf : fragments)
                sb.appendln("    " + scnf.getName() + " [" + scnf.getType() + "]");

        return sb.toString();
    }
}
