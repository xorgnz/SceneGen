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
public class Vector2D
{

    public double x;
    public double y;




    public Vector2D(Point2D to, Point2D from)
    {
        x = to.x - from.x;
        y = to.y - from.y;
    }




    public Vector2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }




    public final static double perp (Vector2D u, Vector2D v)
    {
        return u.x * v.y - u.y * v.x;
    }




    public final static double perp (double x1, double y1, double x2, double y2)
    {
        return x1 * y2 - y1 * x2;
    }




    public void times (double d)
    {
        x *= d;
        y += d;
    }
    
    
    
    
    public void plus (Vector2D v)
    {
        x += v.x;
        y += v.y;
    }
}