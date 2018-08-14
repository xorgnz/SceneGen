package org.memehazard.wheel.style.view;

import org.memehazard.wheel.style.model.Style;

public class StyleDescriptor
{
    private double alpha;
    private String ambient;
    private String diffuse;
    private String emissive;
    private int    shininess;
    private String specular;
    private String tag;


    public StyleDescriptor()
    {
        this(new Style());
    }


    public StyleDescriptor(Style mtl)
    {
        this.alpha = mtl.getAlpha();
        this.ambient = mtl.getAmbient();
        this.diffuse = mtl.getDiffuse();
        this.emissive = mtl.getEmissive();
        this.shininess = mtl.getShininess();
        this.specular = mtl.getSpecular();
        this.tag = mtl.getTag();
    }


    public double getAlpha()
    {
        return alpha;
    }


    public String getAmbient()
    {
        return ambient;
    }


    public String getDiffuse()
    {
        return diffuse;
    }


    public String getEmissive()
    {
        return emissive;
    }


    public int getShininess()
    {
        return shininess;
    }


    public String getSpecular()
    {
        return specular;
    }


    public String getTag()
    {
        return tag;
    }


    @Override
    public String toString()
    {
        return String.format("Style \"%s\" - alpha=%s, amb=%s, dif=%s, sp=%s, em=%s, shn=%s", tag, alpha, ambient, diffuse, specular,
                emissive, shininess);
    }


}
