package org.memehazard.wheel.core.security;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class WheelAccessDecisionVoter implements AccessDecisionVoter<Object>
{

    @Override
    public boolean supports(Class<?> clazz)
    {
        return true;
    }


    @Override
    public boolean supports(ConfigAttribute attribute)
    {
        return true;
    }


    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes)
    {
        // System.err.println("WheelAccessDecisionVoter.vote");
        // System.err.println("\tauth " + authentication.toString());
        // System.err.println("\tobj  " + object.toString());

        for (ConfigAttribute ca : attributes)
        {
            // System.err.println("\tattr " + ca.toString());

            if (ca.getAttribute().equals("WHEEL_LOGGED_IN") && authentication.getPrincipal().equals("anonymousUser"))
                return AccessDecisionVoter.ACCESS_DENIED;

            if (ca.getAttribute().startsWith("PERM_"))
            {
                // Extract the permission name from the given authority string
                String perm_name = ca.getAttribute().substring(5);

                // If the principal is not a logged in UserDetails object, deny access
                if (!(authentication.getPrincipal() instanceof UserDetails))
                    return AccessDecisionVoter.ACCESS_DENIED;

                // Search to determine if the authentication principal has a permission corresponding to the required
                // authority
                boolean hasAuthority = false;
                for (GrantedAuthority authority : ((UserDetails) authentication.getPrincipal()).getAuthorities())
                {
                    if (authority.getAuthority().equals(perm_name))
                        hasAuthority = true;
                }

                // If the authentication principal does not have authority, deny access
                if (!hasAuthority)
                    return AccessDecisionVoter.ACCESS_DENIED;
            }
        }

        // The principal has passed all tests. Vote to grant access
        return AccessDecisionVoter.ACCESS_GRANTED;
    }
}
