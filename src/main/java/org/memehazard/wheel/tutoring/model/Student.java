package org.memehazard.wheel.tutoring.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.memehazard.wheel.rbac.model.User;

public class Student implements Serializable
{
    private static final long serialVersionUID = 1L;
    private User              user;
    private List<Enrolment>   enrolments       = new ArrayList<Enrolment>();


    public User getUser()
    {
        return user;
    }


    public void setUser(User user)
    {
        this.user = user;
    }


    public List<Enrolment> getEnrolments()
    {
        return enrolments;
    }


    public void setEnrolments(List<Enrolment> enrolments)
    {
        this.enrolments = enrolments;
    }
}
