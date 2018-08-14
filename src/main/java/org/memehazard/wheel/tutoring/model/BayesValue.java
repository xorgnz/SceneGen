package org.memehazard.wheel.tutoring.model;

import java.io.Serializable;
import java.util.Date;


public class BayesValue implements Serializable
{
    private static final long serialVersionUID = 1L;
    private long              domainId;
    private double            p;
    private long              studentId;
    private Date              timestamp;


    public BayesValue()
    {
    }


    public BayesValue(long domainId, long studentId, double p, Date timestamp)
    {
        this.domainId = domainId;
        this.p = p;
        this.studentId = studentId;
        this.timestamp = timestamp;
    }


    public long getDomainId()
    {
        return domainId;
    }


    public double getP()
    {
        return p;
    }


    public long getStudentId()
    {
        return studentId;
    }


    public Date getTimestamp()
    {
        return timestamp;
    }


    public void setDomainId(long domainId)
    {
        this.domainId = domainId;
    }


    public void setP(double p)
    {
        this.p = p;
    }


    public void setStudentId(long studentId)
    {
        this.studentId = studentId;
    }


    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }


    public String toString()
    {
        return "BayesValue: DM" + domainId + " student" + studentId + ". P=" + p + ". Time = " + timestamp;
    }
}