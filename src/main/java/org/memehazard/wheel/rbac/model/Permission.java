package org.memehazard.wheel.rbac.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class Permission implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String            description;
    private int               id;
    @NotEmpty(message = "{Permission.name.empty}")
    private String            name;
    private List<Role>        roles            = new ArrayList<Role>();


    public Permission()
    {
    }


    public Permission(String name, String description)
    {
        this.name = name;
        this.description = description;
    }


    public void addRole(Role r)
    {
        this.roles.add(r);
    }


    @Override
    public boolean equals(Object o)
    {
        return (o instanceof Permission && o != null && ((Permission) o).getId() == id);
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


    public List<Role> getRoles()
    {
        return roles;
    }


    public void removeRole(Role r)
    {
        this.roles.remove(r);
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


    public void setRoles(List<Role> roles)
    {
        this.roles.clear();
        this.roles.addAll(roles);
    }
}
