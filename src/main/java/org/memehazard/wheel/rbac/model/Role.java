package org.memehazard.wheel.rbac.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class Role implements Serializable
{
    private static final long serialVersionUID = 1L;

    @NotNull
    @NotEmpty(message = "{Role.description.empty}")
    private String            description;
    private int               id;
    @NotNull
    @NotEmpty(message = "{Role.name.empty}")
    private String            name;
    private List<Permission>  permissions      = new ArrayList<Permission>();
    private List<User>        users            = new ArrayList<User>();


    public Role()
    {

    }


    public Role(String name, String description)
    {
        this.name = name;
        this.description = description;
    }


    public void addPermission(Permission p)
    {
        this.permissions.add(p);
    }


    public void addUser(User u)
    {
        this.users.add(u);
    }


    @Override
    public boolean equals(Object o)
    {
        return (o instanceof Role && o != null && ((Role) o).getId() == id);
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


    public List<Permission> getPermissions()
    {
        return permissions;
    }


    public List<User> getUsers()
    {
        return users;
    }


    public void removePermission(Permission p)
    {
        permissions.remove(p);
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


    public void setPermissions(List<Permission> permissions)
    {
        this.permissions.clear();
        this.permissions.addAll(permissions);
    }


    public void setUsers(List<User> users)
    {
        this.users.clear();
        this.users.addAll(users);
    }
}
