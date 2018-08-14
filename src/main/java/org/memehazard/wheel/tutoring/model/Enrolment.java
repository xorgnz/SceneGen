package org.memehazard.wheel.tutoring.model;

import java.io.Serializable;

import org.apache.commons.lang.text.StrBuilder;
import org.memehazard.wheel.rbac.model.User;

public class Enrolment implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long              curriculumId;
    private boolean           dirty;
    private int               eventCount;
    private User              student;


    public Enrolment()
    {
    }


    public Enrolment(long curriculumId, long studentId)
    {
        this.curriculumId = curriculumId;
        this.student = new User(studentId);
    }


    public long getCurriculumId()
    {
        return curriculumId;
    }


    public int getEventCount()
    {
        return eventCount;
    }


    public User getStudent()
    {
        return student;
    }


    public boolean isDirty()
    {
        return dirty;
    }


    public void setCurriculumId(long curriculumId)
    {
        this.curriculumId = curriculumId;
    }


    public void setDirty(boolean dirty)
    {
        this.dirty = dirty;
    }


    public void setEventCount(int eventCount)
    {
        this.eventCount = eventCount;
    }


    public void setStudent(User student)
    {
        this.student = student;
    }


    public void setStudentId(long studentId)
    {
        this.student = new User(studentId);
    }


    public String toString()
    {
        StrBuilder sb = new StrBuilder();

        sb.append("Enrolment: ");
        sb.append("Curriculum " + curriculumId);
        sb.append(", student " + student == null ? "null" : student.toNameString());
        sb.append(dirty ? ")" : ") -- is dirty");

        return sb.toString();
    }
}
