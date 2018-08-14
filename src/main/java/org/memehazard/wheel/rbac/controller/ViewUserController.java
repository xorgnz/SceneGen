package org.memehazard.wheel.rbac.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.Role;
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
@RequestMapping("/rbac/user/{userId}")
public class ViewUserController
{
    private static final String PAGE_FILE  = "rbac/user_view";
    private static final String PAGE_TITLE = "View User";

    @Autowired
    private RbacFacade          facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String ep_view(
            @PathVariable int userId,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        User user = facade.getUser(userId);
        List<Role> roles = facade.listRolesByUser(userId);
        List<UserGroup> groups = facade.listUserGroupsByUser(userId);

        // Retrieve unassigned roles
        List<Role> allRoles = facade.listRoles();
        allRoles.removeAll(roles);

        // Retrieve unassigned user groups
        List<UserGroup> allGroups = facade.listUserGroups();
        allGroups.removeAll(groups);

        // Respond
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("groups", groups);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("allGroups", allGroups);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }
}
