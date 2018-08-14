package org.memehazard.wheel.viewer.model;

import java.io.Serializable;

import org.apache.commons.lang.text.StrBuilder;
import org.memehazard.wheel.core.math.Vector3d;

public class Viewpoint implements Serializable
{
    private static final long serialVersionUID = 3837540227848386817L;

    private Vector3d          position         = new Vector3d(0.0, 0.0, 1.0);
    private Vector3d          rotation         = new Vector3d(0.0, 0.0, 0.0);
    private Vector3d          target           = new Vector3d(0.0, 0.0, 0.0);
    private Vector3d          up               = new Vector3d(0.0, 1.0, 0.0);


    public Viewpoint()
    {

    }


    public Viewpoint(double[][] vals)
    {
        position.set(vals[0][0], vals[0][1], vals[0][2]);
        rotation.set(vals[1][0], vals[1][1], vals[1][2]);
        target.set(vals[2][0], vals[2][1], vals[2][2]);
        up.set(vals[3][0], vals[3][1], vals[3][2]);
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || !(o instanceof Viewpoint))
            return false;

        Viewpoint vp = (Viewpoint) o;

        return vp.getPosition().equals(position) &&
               vp.getRotation().equals(rotation) &&
               vp.getTarget().equals(target) &&
               vp.getUp().equals(up);
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


    @Override
    public int hashCode()
    {
        // Multiply by primes. Integer overflow will occur, but we don't care.
        return position.hashCode() * 23 + rotation.hashCode() * 7907 + target.hashCode() * 24091 + up.hashCode() * 97021;
    }


    public void setPosition(Vector3d position)
    {
        this.position = position;
    }


    public void setRotation(Vector3d rotation)
    {
        this.rotation = rotation;
    }


    public void setTarget(Vector3d target)
    {
        this.target = target;
    }


    public void setUp(Vector3d up)
    {
        this.up = up;
    }


    public String toString()
    {
        StrBuilder sb = new StrBuilder();
        sb.append("position: " + position.toString());
        sb.append("; rotation: " + rotation.toString());
        sb.append("; target: " + target.toString());
        sb.append("; up: " + up.toString());
        return sb.toString();
    }
}
