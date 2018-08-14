package org.memehazard.wheel.core.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class WheelPasswordEncoder implements PasswordEncoder
{
    private static StandardPasswordEncoder encoder;
    private static final String            GLOBAL_SECRET = "wheel";


    public WheelPasswordEncoder()
    {
        encoder = new StandardPasswordEncoder(GLOBAL_SECRET);
    }


    @Override
    public String encode(CharSequence rawPassword)
    {
        return encoder.encode(rawPassword);
    }


    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword)
    {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
