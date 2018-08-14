package org.memehazard.wheel.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class WheelUserDetailsImpl implements UserDetails, CredentialsContainer
{
    private static final long                  serialVersionUID = 1L;


    private Collection<SimpleGrantedAuthority> authorities      = new ArrayList<SimpleGrantedAuthority>();
    private String                             password;
    private String                             username;


    public WheelUserDetailsImpl(String username, String password, List<String> authorityStrings)
    {
        this.username = username;
        this.password = password;
        
        for (String s : authorityStrings)
            this.authorities.add(new SimpleGrantedAuthority(s));
    }


    @Override
    public void eraseCredentials()
    {
        this.password = null;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }


    @Override
    public String getPassword()
    {
        return password;
    }


    @Override
    public String getUsername()
    {
        return username;
    }


    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }


    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }


    @Override
    public boolean isEnabled()
    {
        return true;
    }

}
