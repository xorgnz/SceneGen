package org.memehazard.wheel.core.math;

import java.io.Serializable;


public class Vector3d implements Serializable
{
    private static final long serialVersionUID = -6981815984688985470L;

    public double             x;
    public double             y;
    public double             z;


    public Vector3d()
    {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }


    public Vector3d(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public Vector3d clone()
    {
        return new Vector3d(this.x, this.y, this.z);
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || !(o instanceof Vector3d))
            return false;

        Vector3d v3 = (Vector3d) o;

        return v3.x == x && v3.y == y && v3.z == z;
    }


    public double getX()
    {
        return x;
    }


    public double getY()
    {
        return y;
    }


    public double getZ()
    {
        return z;
    }


    @Override
    public int hashCode()
    {
        // Multiply by primes, convert to int
        return (int) Math.round(x * 23 + y * 7907 + z * 95971) % Integer.MAX_VALUE;
    }


    public void set(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public void setX(double x)
    {
        this.x = x;
    }


    public void setY(double y)
    {
        this.y = y;
    }


    public void setZ(double z)
    {
        this.z = z;
    }


    public String toString()
    {
        return "(" + x + "," + y + "," + z + ")";
    }
}
