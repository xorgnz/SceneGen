package org.memehazard.wheel.rbac.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.memehazard.wheel.core.security.WheelPasswordEncoder;
import org.memehazard.wheel.core.validator.ValidEmailAddress;


public class User implements Serializable
{
    private static final long  serialVersionUID = 1L;

    public static final String PASSWORD_SECRET  = "MemeHazard";

    @NotNull
    @NotEmpty(message = "{User.email.empty}")
    @ValidEmailAddress(message = "{User.email.invalid}")
    private String             email;
    @NotNull
    @NotEmpty(message = "{User.firstName.empty}")
    private String             firstName;
    private List<UserGroup>    groups           = new ArrayList<UserGroup>();
    private long               id;
    @NotNull
    @NotEmpty(message = "{User.lastName.empty}")
    private String             lastName;
    private String             password;
    private List<Role>         roles            = new ArrayList<Role>();
    @NotNull
    @NotEmpty(message = "{User.username.empty}")
    private String             username;


    public User()
    {
    }


    public User(String username, String passwordClear, String email, String firstName, String lastName)
    {
        this.username = username;
        encodeAndSetPassword(passwordClear);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public User(long id)
    {
        this.id = id;
    }


    public void addGroup(UserGroup ug)
    {
        this.groups.add(ug);
    }


    public void addRole(Role r)
    {
        this.roles.add(r);
    }


    public void encodeAndSetPassword(String password)
    {
        WheelPasswordEncoder encoder = new WheelPasswordEncoder();
        this.password = encoder.encode(password);
    }


    @Override
    public boolean equals(Object o)
    {
        return (o instanceof User && ((User) o).getId() == id);
    }


    public String getEmail()
    {
        return email;
    }


    public String getFirstName()
    {
        return firstName;
    }


    public String getFullName()
    {
        return this.firstName + " " + this.lastName;
    }


    public List<UserGroup> getGroups()
    {
        return groups;
    }


    public long getId()
    {
        return id;
    }


    public String getLastName()
    {
        return lastName;
    }


    public String getPassword()
    {
        return password;
    }


    public List<Role> getRoles()
    {
        return roles;
    }


    public String getUsername()
    {
        return username;
    }


    public void removeGroup(UserGroup ug)
    {
        this.groups.remove(ug);

    }


    public void removeRole(Role r)
    {
        this.roles.remove(r);
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    public void setFirstName(String firstname)
    {
        this.firstName = firstname;
    }


    public void setGroups(List<UserGroup> groups)
    {
        this.groups.clear();
        this.groups.addAll(groups);
    }


    public void setId(long id)
    {
        this.id = id;
    }


    public void setLastName(String lastname)
    {
        this.lastName = lastname;
    }


    public void setPassword(String password)
    {
        this.password = password;
    }


    public void setRoles(List<Role> roles)
    {
        this.roles.clear();
        this.roles.addAll(roles);
    }


    public void setUsername(String username)
    {
        this.username = username;
    }


    public String toNameString()
    {
        return this.username + " (" + this.firstName + " " + this.lastName + ")";
    }
}
