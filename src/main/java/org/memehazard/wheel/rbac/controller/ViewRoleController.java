package org.memehazard.wheel.rbac.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.Permission;
import org.memehazard.wheel.rbac.model.Role;
import org.memehazard.wheel.rbac.model.User;
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
@RequestMapping("/rbac/role/{roleId}")
public class ViewRoleController
{
    private static final String PAGE_FILE  = "rbac/role_view";
    private static final String PAGE_TITLE = "View Role";

    @Autowired
    private RbacFacade          facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String ep_view(
            @PathVariable int roleId,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Retrieve role
        Role role = facade.getRole(roleId);
        List<Permission> permissions = facade.listPermissionsByRole(roleId);
        List<User> users = facade.listUsersByRole(roleId);

        // Retrieve unassigned permissions
        List<Permission> allPermissions = facade.listPermissions();
        allPermissions.removeAll(permissions);

        // Retrieve unassigned permissions
        List<User> allUsers = facade.listUsers();
        allUsers.removeAll(users);

        // Respond
        model.addAttribute("role", role);
        model.addAttribute("permissions", permissions);
        model.addAttribute("allPermissions", allPermissions);
        model.addAttribute("users", users);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }
}
