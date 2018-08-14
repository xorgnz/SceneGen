/**
 * 
 */
package org.memehazard.wheel.style.model;


import java.io.Serializable;

import org.apache.commons.lang.text.StrBuilder;
import org.memehazard.wheel.style.model.worker.StyleWorker;
import org.memehazard.wheel.style.model.worker.StyleWorkerFactory;

/**
 * @author xorgnz
 * 
 */

public class Style implements Serializable
{
    private static final long     serialVersionUID = 1L;
    private double                alpha;
    private String                ambient;
    private String                diffuse;
    private String                emissive;
    private int                   id;
    private int                   priority;
    private int                   shininess;
    private String                specular;
    private Stylesheet            stylesheet;
    private String                tag;
    private transient StyleWorker worker;


    public Style()
    {
        this.ambient   = "#808080";
        this.diffuse   = "#808080";
        this.emissive  = "#000000";
        this.specular  = "#ffffff";
        this.shininess = 20;
        this.alpha     = 1;
    }


    public Style(double alpha, String ambient, String diffuse, String emissive, String specular, int shininess)
    {
        this.alpha = alpha;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.emissive = emissive;
        this.specular = specular;
        this.shininess = shininess;
    }


    public Style(Stylesheet stylesheet, String tag, double alpha, String ambient, String diffuse, String emissive,
            String specular, int shininess, int priority)
    {
        super();
        this.stylesheet = stylesheet;
        this.setTag(tag);
        this.alpha = alpha;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.emissive = emissive;
        this.specular = specular;
        this.shininess = shininess;
        this.priority = priority;
    }


    /**
     * Attempt to apply this style to the given <code>Stylable</code> object. Relies on worker to do this.
     * 
     * @param s
     */
    public boolean apply(Stylable s)
    {
        // Catch uninitialized workers (possible due to Hibernate's use of reflection)
        if (this.worker == null)
            this.setTag(tag);

        // Use the worker to apply the style
        return this.worker.apply(s);
    }


    /**
     * Copy the values of the given style over this style. Does not affect stylesheet affiliation. Updates worker class.
     * 
     * @param style
     */
    public void copy(Style style)
    {
        this.setTag(tag);
        this.alpha = style.getAlpha();
        this.ambient = style.getAmbient();
        this.diffuse = style.getDiffuse();
        this.emissive = style.getEmissive();
        this.specular = style.getSpecular();
        this.shininess = style.getShininess();
        this.priority = style.getPriority();
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


    public int getId()
    {
        return id;
    }


    public int getPriority()
    {
        return priority;
    }


    public int getShininess()
    {
        return shininess;
    }


    public String getSpecular()
    {
        return specular;
    }


    public Stylesheet getStylesheet()
    {
        return stylesheet;
    }


    public String getTag()
    {
        return tag;
    }


    public void setAlpha(double alpha)
    {
        this.alpha = alpha;
    }


    public void setAmbient(String ambient)
    {
        this.ambient = ambient;
    }


    public void setDiffuse(String diffuse)
    {
        this.diffuse = diffuse;
    }


    public void setEmissive(String emissive)
    {
        this.emissive = emissive;
    }


    public void setId(int id)
    {
        this.id = id;
    }


    public void setPriority(int priority)
    {
        this.priority = priority;
    }


    public void setShininess(int shininess)
    {
        this.shininess = shininess;
    }


    public void setSpecular(String specular)
    {
        this.specular = specular;
    }


    public void setStylesheet(Stylesheet sheet)
    {
        this.stylesheet = sheet;
    }


    public void setTag(String tag)
    {
        this.tag = tag;
        this.worker = StyleWorkerFactory.getInstance(this);
    }


    @Override
    public String toString()
    {
        StrBuilder sb = new StrBuilder();

        sb.append(tag);
        sb.append("- alpha" + alpha);
        sb.append(", ambient:" + ambient);
        sb.append(", diffuse:" + diffuse);
        sb.append(", specular:" + specular);
        sb.append(", emissive:" + emissive);
        sb.append(", shininess:" + shininess);

        return sb.toString();

    }
}
