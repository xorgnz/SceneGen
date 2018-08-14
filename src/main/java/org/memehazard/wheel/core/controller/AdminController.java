package org.memehazard.wheel.core.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.core.WheelConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminController
{
    private Logger             log = LoggerFactory.getLogger(getClass());

    @Autowired
    private WheelConfiguration wheelConfig;


    @RequestMapping(value = "/blank-main", method = RequestMethod.GET)
    @Secured("Fish")
    public String blank_main(Model model, HttpServletRequest request)
    {
        log.trace("Generating pasge - " + request.getServletPath());

        // Respond
        model.addAttribute("pageTitle", "");
        model.addAttribute("pageFile", "admin/blank-main");
        return "admin/base";
    }


    @Secured("Fish")
    @RequestMapping(value = "/blank-side", method = RequestMethod.GET)
    public String blank_side(Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Respond
        model.addAttribute("pageTitle", "");
        model.addAttribute("pageFile", "admin/blank-side");
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Respond
        model.addAttribute("siteTitle", wheelConfig.getSiteTitle());
        return "admin/index";
    }


    @Secured("Fish")
    @RequestMapping(value = "/nav", method = RequestMethod.GET)
    public String nav(Locale locale, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        String formattedDate = dateFormat.format(date);

        // Get current user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails)
            model.addAttribute("username", ((UserDetails) principal).getUsername());

        // Respond
        model.addAttribute("serverTime", formattedDate);
        model.addAttribute("siteTitle", wheelConfig.getSiteTitle());
        return "admin/nav";
    }

}
