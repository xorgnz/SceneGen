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
public class Point3F
{
    public float x;
    public float y;
    public float z;


    public Point3F(float x, float y, float z)
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
        Point3F p = (Point3F) o;
        return this.x == p.x && this.y == p.y && this.z == p.z;
    }


    @Override
    public int hashCode()
    {
        return Math.round((x * y * z) % Integer.MAX_VALUE);
    }


    @Override
    public String toString()
    {
        return String.format("P(%.3f, .3f, .3f", x, y, z);
    }
}
