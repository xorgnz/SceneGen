package org.memehazard.wheel.core.security;

import java.util.ArrayList;
import java.util.List;

import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class WheelUserDetailsService implements UserDetailsService
{
    @Autowired
    public RbacFacade facade;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User u = facade.getUserByUsername(username);
        List<String> authorityStrings = new ArrayList<String>();
        authorityStrings.add("edit_rbac");
        authorityStrings.add("god emperor");

        if (username.equals("bootstrap"))
            u = new User("bootstrap", "bootstrap", "xorgnz@gmail.com", "Bootstrap", "User");

        return new WheelUserDetailsImpl(username, u.getPassword(), authorityStrings);
    }
}
