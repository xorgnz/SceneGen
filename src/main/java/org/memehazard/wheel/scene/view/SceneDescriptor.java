package org.memehazard.wheel.scene.view;

import java.util.ArrayList;
import java.util.List;

import org.memehazard.wheel.scene.model.Scene;
import org.memehazard.wheel.scene.model.SceneFragment;
import org.memehazard.wheel.viewer.view.ViewpointDescriptor;


public class SceneDescriptor
{
    private List<SceneFragmentDescriptor> fragments = new ArrayList<SceneFragmentDescriptor>();
    private int                           id;
    private String                        name;
    private ViewpointDescriptor           viewpoint = null;


    public SceneDescriptor(Scene s)
    {
        this.id = s.getId();
        this.name = s.getName();

        if (s.getViewpoint() != null)
            this.viewpoint = new ViewpointDescriptor(s.getViewpoint());

        if (s.getFragments() != null)
            for (SceneFragment fragment : s.getFragments())
                this.fragments.add(new SceneFragmentDescriptor(fragment));
    }


    public void addSceneFragmentDescriptor(SceneFragmentDescriptor scnpDescriptor)
    {
        this.fragments.add(scnpDescriptor);
    }


    public List<SceneFragmentDescriptor> getFragments()
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


    public ViewpointDescriptor getViewpoint()
    {
        return viewpoint;
    }


    public void setId(int id)
    {
        this.id = id;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setViewpoint(ViewpointDescriptor viewpoint)
    {
        this.viewpoint = viewpoint;
    }
}
