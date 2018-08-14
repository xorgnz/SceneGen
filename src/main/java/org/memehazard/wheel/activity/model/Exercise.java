package org.memehazard.wheel.activity.model;

import java.io.Serializable;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.springframework.util.Assert;

public class Exercise implements Serializable
{
    private static final long serialVersionUID = 1L;
    private ActivityInstance  activityInstance;
    private Curriculum        curriculum;
    private int               id;


    public Exercise()
    {
    }


    public Exercise(ActivityInstance activityInstance, Curriculum curriculum)
    {
        Assert.isTrue(activityInstance != null, "Cannot create exercise from null activity instance");
        Assert.isTrue(curriculum != null, "Cannot create exercise from null activity instance");

        this.activityInstance = activityInstance;
        this.curriculum = curriculum;
    }


    public ActivityInstance getActivityInstance()
    {
        return activityInstance;
    }


    public Curriculum getCurriculum()
    {
        return curriculum;
    }


    public int getId()
    {
        return id;
    }


    public String prepareFactsUrl(long studentId) throws URISyntaxException
    {
        URIBuilder factsUriBuilder = new URIBuilder(this.getActivityInstance().getTemplate().getFactsUrl());
        for (ParameterValue pv : this.getActivityInstance().getParameterValues())
            factsUriBuilder.addParameter(pv.getVariable(), pv.getValue());

        factsUriBuilder.addParameter("studentId", "" + studentId);

        return factsUriBuilder.toString();
    }


    public String preparePlayUrl(long studentId, String redirectUrl) throws URISyntaxException
    {
        URIBuilder playUriBuilder = new URIBuilder(this.getActivityInstance().getTemplate().getPlayUrl());
        for (ParameterValue pv : this.getActivityInstance().getParameterValues())
        {
            playUriBuilder.addParameter(pv.getVariable(), pv.getValue());
        }

        playUriBuilder.addParameter("studentId", "" + studentId);
        playUriBuilder.addParameter("redirectUrl", redirectUrl);

        return playUriBuilder.toString();
    }


    public void setActivityInstance(ActivityInstance activityInstance)
    {
        Assert.isTrue(activityInstance != null, "Cannot assign null activity instance to exercise");

        this.activityInstance = activityInstance;
    }


    public void setCurriculum(Curriculum curriculum)
    {
        Assert.isTrue(curriculum != null, "Cannot assign null curriculum to exercise");

        this.curriculum = curriculum;
    }


    public void setId(int id)
    {
        this.id = id;
    }


    public String toString()
    {
        return String.format("Exercise: ActivityInstance %s, Curriculum %s",
                activityInstance != null ? activityInstance.getName() : "missing",
                curriculum != null ? curriculum.getName() : "missing");
    }
}
