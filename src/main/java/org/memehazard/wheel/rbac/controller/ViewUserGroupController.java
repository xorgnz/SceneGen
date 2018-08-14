package org.memehazard.wheel.rbac.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.rbac.model.UserGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/rbac/usergrp/{userGroupId}")
public class ViewUserGroupController
{
    private static final String PAGE_FILE  = "rbac/usergrp_view";
    private static final String PAGE_TITLE = "View User Group";

    @Autowired
    private RbacFacade          facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String ep_view(
            @PathVariable int userGroupId,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        UserGroup usergrp = facade.getUserGroup(userGroupId);
        List<User> users = facade.listUsersByUserGroup(userGroupId);

        // Retrieve unassigned roles
        List<User> allUsers = facade.listUsers();
        allUsers.removeAll(users);

        // Respond
        model.addAttribute("usergrp", usergrp);
        model.addAttribute("users", users);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }
}
