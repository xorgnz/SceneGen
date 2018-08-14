package org.memehazard.wheel.scene.model;

import java.io.Serializable;

public class Transform implements Serializable
{
    private static final long serialVersionUID = -5755900365766083568L;
    private double            positionX        = 0.0;
    private double            positionY        = 0.0;
    private double            positionZ        = 0.0;
    private double            rotationX        = 0.0;
    private double            rotationY        = 0.0;
    private double            rotationZ        = 0.0;


    public Transform()
    {
    }


    public Transform(double pos_x, double pos_y, double pos_z, double rot_x, double rot_y, double rot_z)
    {
        this.positionX = pos_x;
        this.positionY = pos_y;
        this.positionZ = pos_z;
        this.rotationX = rot_x;
        this.rotationY = rot_y;
        this.rotationZ = rot_z;
    }


    public double getPositionX()
    {
        return positionX;
    }


    public double getPositionY()
    {
        return positionY;
    }


    public double getPositionZ()
    {
        return positionZ;
    }


    public double getRotationX()
    {
        return rotationX;
    }


    public double getRotationY()
    {
        return rotationY;
    }


    public double getRotationZ()
    {
        return rotationZ;
    }


    public void setPositionX(double positionX)
    {
        this.positionX = positionX;
    }


    public void setPositionY(double positionY)
    {
        this.positionY = positionY;
    }


    public void setPositionZ(double positionZ)
    {
        this.positionZ = positionZ;
    }


    public void setRotationX(double rotationX)
    {
        this.rotationX = rotationX;
    }


    public void setRotationY(double rotationY)
    {
        this.rotationY = rotationY;
    }


    public void setRotationZ(double rotationZ)
    {
        this.rotationZ = rotationZ;
    }
}
