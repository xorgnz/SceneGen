package org.memehazard.wheel.viewer.view;

import org.apache.commons.lang.StringUtils;
import org.memehazard.wheel.core.math.Vector3d;
import org.memehazard.wheel.viewer.model.Viewpoint;

public class ViewpointDescriptor
{
    private Vector3d position = new Vector3d(0.0, 0.0, 1.0);
    private Vector3d rotation = new Vector3d(0.0, 0.0, 0.0);
    private Vector3d target   = new Vector3d(0.0, 0.0, 0.0);
    private Vector3d up       = new Vector3d(0.0, 1.0, 0.0);


    public ViewpointDescriptor()
    {
    }


    public ViewpointDescriptor(Viewpoint vp)
    {
        this.position = vp.getPosition().clone();
        this.rotation = vp.getRotation().clone();
        this.target = vp.getTarget().clone();
        this.up = vp.getUp().clone();
    }


    public Vector3d getPosition()
    {
        return position;
    }


    public Vector3d getRotation()
    {
        return rotation;
    }


    public Vector3d getTarget()
    {
        return target;
    }


    public Vector3d getUp()
    {
        return up;
    }


    public String toString()
    {
        String[] strings = new String[4];
        strings[0] = position.toString();
        strings[1] = rotation.toString();
        strings[2] = target.toString();
        strings[3] = up.toString();

        return StringUtils.join(strings, ";");
    }
}
