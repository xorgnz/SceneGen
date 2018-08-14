package org.memehazard.wheel.sandbox.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.security.WheelPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Get the current user object and display.
 * 
 * @author xorgnz
 */
@Controller
@RequestMapping("/sandbox/securityPasswordEncoder")
public class SecurityPasswordEncodingController
{
    @Autowired
    WheelPasswordEncoder encoder_aw; 
    
    @RequestMapping(method = RequestMethod.GET)
    public String form(@RequestBody String body,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        PasswordEncoder encoder = new StandardPasswordEncoder("wheel");
        String result = "wheel --> " + encoder.encode("pw");
        
        // Respond
        model.addAttribute("output", result);
        model.addAttribute("pageTitle", "Password encoding demo");
        model.addAttribute("pageFile", "sandbox/output_dump");
        return "admin/base";
    }
}
