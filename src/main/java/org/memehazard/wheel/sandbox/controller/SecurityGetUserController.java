package org.memehazard.wheel.sandbox.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/sandbox/securityGetUser")
public class SecurityGetUserController
{
    @RequestMapping(method = RequestMethod.GET)
    public String form(@RequestBody String body,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails)
            model.addAttribute("output",
                    ((UserDetails) principal).getUsername() + " \\ " + ((UserDetails) principal).getPassword());
        else
            model.addAttribute("output", "Not a UserDetails obj - toString() = " + principal.toString());

        // Respond
        model.addAttribute("pageTitle", "Sandbox - HTTP Request Dump");
        model.addAttribute("pageFile", "sandbox/output_dump");
        return "admin/base";
    }
}
