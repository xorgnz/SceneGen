package org.memehazard.wheel.asset.model;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author xorgnz
 * 
 */

public class Asset3DStatistics implements Serializable
{
    private static final long serialVersionUID = -3504259864016549370L;

    private static Logger     log              = LoggerFactory.getLogger(Asset3DStatistics.class);
    public double             centroid_x       = 0;
    public double             centroid_y       = 0;
    public double             centroid_z       = 0;
    public double             max_x            = -Double.MAX_VALUE;
    public double             max_y            = -Double.MAX_VALUE;
    public double             max_z            = -Double.MAX_VALUE;
    public double             min_x            = Double.MAX_VALUE;
    public double             min_y            = Double.MAX_VALUE;
    public double             min_z            = Double.MAX_VALUE;


    public Asset3DStatistics()
    {
    }


    public Asset3DStatistics(double[] centroid, double[] max, double[] min)
    {
        centroid_x = centroid[0];
        centroid_y = centroid[1];
        centroid_z = centroid[2];

        max_x = max[0];
        max_y = max[1];
        max_z = max[2];

        min_x = min[0];
        min_y = min[1];
        min_z = min[2];
    }


    @Override
    public boolean equals(Object o)
    {
        try
        {
            Asset3DStatistics a = (Asset3DStatistics) o;

            if (this.centroid_x != a.centroid_x)
                return false;
            if (this.centroid_y != a.centroid_y)
                return false;
            if (this.centroid_z != a.centroid_z)
                return false;

            if (this.max_x != a.max_x)
                return false;
            if (this.max_y != a.max_y)
                return false;
            if (this.max_z != a.max_z)
                return false;

            if (this.min_x != a.min_x)
                return false;
            if (this.min_y != a.min_y)
                return false;
            if (this.min_z != a.min_z)
                return false;
        }
        catch (Exception e)
        {
            log.trace("Error while comparing Asset3DStatistics. Rescued.", e);
        }

        return true;
    }


    public double[] getCentroid()
    {
        return new double[] { centroid_x, centroid_y, centroid_z };
    }


    public double getCentroid_x()
    {
        return centroid_x;
    }


    public double getCentroid_y()
    {
        return centroid_y;
    }


    public double getCentroid_z()
    {
        return centroid_z;
    }


    public double[] getMax()
    {
        return new double[] { max_x, max_y, max_z };
    }


    public double getMax_x()
    {
        return max_x;
    }


    public double getMax_y()
    {
        return max_y;
    }


    public double getMax_z()
    {
        return max_z;
    }


    public double[] getMin()
    {
        return new double[] { min_x, min_y, min_z };
    }


    public double getMin_x()
    {
        return min_x;
    }


    public double getMin_y()
    {
        return min_y;
    }


    public double getMin_z()
    {
        return min_z;
    }


    public void setCentroid_x(double d)
    {
        centroid_x = d;
    }


    public void setCentroid_y(double d)
    {
        centroid_y = d;
    }


    public void setCentroid_z(double d)
    {
        centroid_z = d;
    }


    public void setMax_x(double d)
    {
        max_x = d;
    }


    public void setMax_y(double d)
    {
        max_y = d;
    }


    public void setMax_z(double d)
    {
        max_z = d;
    }


    public void setMin_x(double d)
    {
        min_x = d;
    }


    public void setMin_y(double d)
    {
        min_y = d;
    }


    public void setMin_z(double d)
    {
        min_z = d;
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("Asset3DStatistics: ");
        sb.append("\t(" + this.centroid_x + ", " + this.centroid_y + ", " + this.centroid_z + ")");
        sb.append("\t(" + this.min_x + " " + this.min_y + " " + this.min_z + ")");
        sb.append("\t(" + this.max_x + " " + this.max_y + " " + this.max_z + ")");

        return sb.toString();
    }
}
