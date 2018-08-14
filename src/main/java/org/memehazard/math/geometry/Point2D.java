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
public class Point2D
{
    public double x;
    public double y;


    public Point2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }


    public Point2D(Point2I p)
    {
        this.x = p.x;
        this.y = p.y;
    }


    @Override
    public boolean equals(Object o)
    {
        Point2D p = (Point2D) o;
        return this.x == p.x && this.y == p.y;
    }


    @Override
    public int hashCode()
    {
        return (int) Math.round(((float) x * y) % Integer.MAX_VALUE);
    }


    public void plus(Vector2D v)
    {
        x += v.x;
        y += v.y;
    }


    @Override
    public String toString()
    {
        return "Point(" + x + "," + y + ")";
    }
}
