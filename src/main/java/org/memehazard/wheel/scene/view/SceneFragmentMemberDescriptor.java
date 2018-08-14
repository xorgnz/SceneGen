package org.memehazard.wheel.scene.view;

import org.memehazard.wheel.asset.view.AssetDescriptor;
import org.memehazard.wheel.query.view.EntityDescriptor;
import org.memehazard.wheel.scene.model.SceneFragmentMember;
import org.memehazard.wheel.style.view.StyleDescriptor;
import org.memehazard.wheel.viewer.view.SceneObjectDescriptor;

public class SceneFragmentMemberDescriptor extends SceneObjectDescriptor
{
    private int                 fragmentId;
    private TransformDescriptor transform = new TransformDescriptor();


    public SceneFragmentMemberDescriptor(SceneFragmentMember member)
    {
        super();

        this.id = member.getId();
        this.asset = new AssetDescriptor(member.getAsset());
        this.style = new StyleDescriptor(member.getStyle());
        this.entity = new EntityDescriptor(member.getEntity());
        this.transform = new TransformDescriptor(member.getTransform());
        this.visible = member.isVisible();
    }


    public TransformDescriptor getTransform()
    {
        return transform;
    }


    public void setTransform(TransformDescriptor transform)
    {
        this.transform = transform;
    }


    public int getFragmentId()
    {
        return fragmentId;
    }


    public void setFragmentId(int fragmentId)
    {
        this.fragmentId = fragmentId;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
}
