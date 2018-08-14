/*
 * Created on 28/09/2005
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.memehazard.math.geometry;

/**
 * 
 * @author ttn14
 */
public class Point3D
{
    public double x;
    public double y;
    public double z;


    public Point3D(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Point3D p = (Point3D) o;
        return this.x == p.x && this.y == p.y && this.z == p.z;
    }


    @Override
    public int hashCode()
    {
        return (int) Math.round(((float) x * y * z) % Integer.MAX_VALUE);
    }


    @Override
    public String toString()
    {
        return String.format("P(%.6f, %.6f, %.6f)", x, y, z);
    }
}
