package org.memehazard.wheel.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class AccessDeniedController
{
    private Logger log = LoggerFactory.getLogger(getClass());


    @RequestMapping(value = "/access-denied", method = RequestMethod.GET)
    public String home(Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Respond
        return "access-denied";
    }

}
