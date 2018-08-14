package org.memehazard.wheel.scene.view;

import org.memehazard.wheel.scene.model.Transform;

public class TransformDescriptor
{
    public double positionX = 0.0;
    public double positionY = 0.0;
    public double positionZ = 0.0;
    public double rotationX = 0.0;
    public double rotationY = 0.0;
    public double rotationZ = 0.0;


    public TransformDescriptor()
    {
    }


    public TransformDescriptor(Transform t)
    {
        this.positionX = t.getPositionX();
        this.positionY = t.getPositionY();
        this.positionZ = t.getPositionZ();
        this.rotationX = t.getRotationX();
        this.rotationY = t.getRotationY();
        this.rotationZ = t.getRotationZ();
    }

}
