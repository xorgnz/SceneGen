package org.memehazard.wheel.viewer.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.text.StrBuilder;

public class SceneContentDescriptor
{
    private String                      name                   = null;
    private List<SceneObjectDescriptor> sceneObjectDescriptors = new ArrayList<SceneObjectDescriptor>();
    private ViewpointDescriptor         viewpoint              = null;


    public SceneContentDescriptor()
    {
        this.name = "Unnamed Scene";
    }


    public SceneContentDescriptor(String name)
    {
        this.name = name;
    }


    public SceneContentDescriptor(String name, List<SceneObjectDescriptor> sceneObjectDescriptors)
    {
        this.name = name;
        this.sceneObjectDescriptors.addAll(sceneObjectDescriptors);
    }


    public SceneContentDescriptor(String name, List<SceneObjectDescriptor> sceneObjectDescriptors, ViewpointDescriptor viewpoint)
    {
        this.name = name;
        this.sceneObjectDescriptors.addAll(sceneObjectDescriptors);
        this.viewpoint = viewpoint;
    }


    public void addSceneObjectDescriptor(SceneObjectDescriptor scnod)
    {
        this.sceneObjectDescriptors.add(scnod);
    }


    public void addSceneObjectDescriptors(List<SceneObjectDescriptor> descriptors)
    {
        this.sceneObjectDescriptors.addAll(descriptors);
    }


    public String getName()
    {
        return name;
    }


    public List<SceneObjectDescriptor> getSceneObjectDescriptors()
    {
        return sceneObjectDescriptors;
    }


    public List<SceneObjectDescriptor> getValidSceneObjectDescriptors()
    {
        List<SceneObjectDescriptor> scnods = new ArrayList<SceneObjectDescriptor>();

        for (SceneObjectDescriptor scnod : sceneObjectDescriptors)
            if (scnod.isValid())
                scnods.add(scnod);

        return scnods;
    }


    public ViewpointDescriptor getViewpoint()
    {
        return viewpoint;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setViewpoint(ViewpointDescriptor viewpoint)
    {
        this.viewpoint = viewpoint;
    }


    @Override
    public String toString()
    {
        StrBuilder sb = new StrBuilder();

        sb.appendln("Scene \"" + name + "\":");

        for (SceneObjectDescriptor se : sceneObjectDescriptors)
            sb.append(se.toString());

        return sb.toString();
    }
}
