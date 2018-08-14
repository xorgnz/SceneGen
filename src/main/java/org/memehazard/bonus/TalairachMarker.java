package org.memehazard.bonus;

import org.apache.commons.lang.text.StrBuilder;

public class TalairachMarker
{
    private String label;
    private double x;
    private double y;
    private double z;
    private String color;


    public String getLabel()
    {
        return label;
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


    public void setLabel(String label)
    {
        this.label = label;
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
        return String.format("X,Y,Z: % 7.2f, % 7.2f, % 7.2f; Label: %s", x, y, z, label);
    }


    public String toJSString()
    {
        StrBuilder sb = new StrBuilder();

        sb.appendln("{");
        sb.appendln(String.format("    x: %f, y: %f, z: %f,", x, y, z));
        sb.appendln("    name: '" + label + "',");
        sb.appendln("    color: '" + color + "'");
        sb.append("}");

        return sb.toString();
    }


    public void setColor(String color)
    {
        System.err.println(color);
        
        if (color.equalsIgnoreCase("red"))
            this.color = "#FF0000";
        else if (color.equalsIgnoreCase("yellow"))
            this.color = "#FFFF00";            
    }
}
