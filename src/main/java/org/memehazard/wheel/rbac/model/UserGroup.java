package org.memehazard.wheel.rbac.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;


public class UserGroup implements Serializable
{
    private static final long serialVersionUID = 1L;

    @NotNull
    @NotEmpty(message = "{UserGroup.description.empty}")
    private String            description;
    private int               id;
    @NotNull
    @NotEmpty(message = "{UserGroup.name.empty}")
    private String            name;
    private List<User>        users            = new ArrayList<User>();


    public UserGroup()
    {

    }


    public UserGroup(String name, String description)
    {
        this.name = name;
        this.description = description;
    }


    public void addUser(User u)
    {
        this.users.add(u);
    }


    @Override
    public boolean equals(Object o)
    {
        return (o instanceof UserGroup && o != null && ((UserGroup) o).getId() == id);
    }


    public String getDescription()
    {
        return description;
    }


    public int getId()
    {
        return id;
    }


    public String getName()
    {
        return name;
    }


    public List<User> getUsers()
    {
        return users;
    }


    public void removeUser(User u)
    {
        this.users.remove(u);
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public void setId(int id)
    {
        this.id = id;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setUsers(List<User> users)
    {
        this.users.clear();
        this.users.addAll(users);
    }
}
