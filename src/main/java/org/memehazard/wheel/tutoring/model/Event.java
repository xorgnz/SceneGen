package org.memehazard.wheel.tutoring.model;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long              curriculumId;
    private String            description;
    private long              id;
    private long              objectEntityId;
    // Change in P for some fact accounted for by this event
    private double            pAssertion;
    private String            relation;
    private String            source;
    private long              studentId;
    private long              subjectEntityId;
    private Date              timestamp;
    private String            value;


    public Event()
    {

    }


    public Event(long curriculumId, long studentId, String description, String source)
    {
        this.studentId = studentId;
        this.curriculumId = curriculumId;
        this.timestamp = new Date();
        this.description = description;
        this.source = source;
    }


    public long getCurriculumId()
    {
        return curriculumId;
    }


    public String getDescription()
    {
        return description;
    }


    public long getId()
    {
        return id;
    }


    public long getObjectEntityId()
    {
        return objectEntityId;
    }


    public double getDeltaP()
    {
        return pAssertion;
    }


    public String getRelation()
    {
        return relation;
    }


    public String getSource()
    {
        return source;
    }


    public long getStudentId()
    {
        return studentId;
    }


    public long getSubjectEntityId()
    {
        return subjectEntityId;
    }


    public Date getTimestamp()
    {
        return timestamp;
    }


    public String getValue()
    {
        return value;
    }


    public void setCurriculumId(long curriculumId)
    {
        this.curriculumId = curriculumId;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public void setId(long id)
    {
        this.id = id;
    }


    public void setObjectEntityId(long objectEntityId)
    {
        this.objectEntityId = objectEntityId;
    }


    public void setDeltaP(double assertion)
    {
        this.pAssertion = assertion;
    }


    public void setRelation(String relation)
    {
        this.relation = relation;
    }


    public void setSource(String source)
    {
        this.source = source;
    }


    public void setStudentId(long studentId)
    {
        this.studentId = studentId;
    }


    public void setSubjectEntityId(long subjectEntityId)
    {
        this.subjectEntityId = subjectEntityId;
    }


    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }


    public void setValue(String value)
    {
        this.value = value;
    }
}
