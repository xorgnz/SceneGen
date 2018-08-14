package org.memehazard.wheel.rbac.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.rbac.model.UserGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/rbac/usergrp_user_link/deassign")
public class DeassignUserGroupUserLinkController
{
    @Autowired
    private RbacFacade facade;
    private Logger     log = LoggerFactory.getLogger(getClass());


    @RequestMapping(value = "/{userGroupId}/{userId}", method = RequestMethod.GET)
    @Transactional
    public String performGet(
            @PathVariable int userGroupId,
            @PathVariable int userId,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());
        return process(userGroupId, userId, flash, request);
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String performPost(
            @ModelAttribute("userGroupId") int userGroupId,
            @ModelAttribute("userId") int userId,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        return process(userGroupId, userId, flash, request);
    }


    public String process(int userGroupId, int userId, RedirectAttributes flash, HttpServletRequest request)
    {
        // Act
        facade.deassignUserFromUserGroup(userId, userGroupId);

        // Response message
        UserGroup ug = facade.getUserGroup(userGroupId);
        User u = facade.getUser(userId);
        flash.addFlashAttribute("msg_good", String.format("Removed user \"%s\" from group \"%s\"", u.toNameString(), ug.getName()));

        // Redirect
        return "redirect:" + request.getHeader("referer");
    }


}