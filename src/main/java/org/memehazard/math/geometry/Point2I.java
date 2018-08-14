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
public class Point2I implements Comparable<Point2I>
{
    public int x;
    public int y;


    public Point2I(int x, int y)
    {
        this.x = x;
        this.y = y;
    }


    @Override
    public boolean equals(Object o)
    {
        Point2I p = (Point2I) o;
        return this.x == p.x && this.y == p.y;
    }


    @Override
    public int compareTo(Point2I p)
    {
        if (x < p.x)
            return -1;
        else if (x > p.x)
            return 1;
        else if (y < p.y)
            return -1;
        else if (y > p.y)
            return 1;
        return 0;
    }


    @Override
    public int hashCode()
    {
        return x * y;
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
