package org.memehazard.wheel.rbac.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.Permission;
import org.memehazard.wheel.rbac.model.Role;
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
@RequestMapping("/rbac/permission/{permId}")
public class ViewPermissionController
{
    private static final String PAGE_FILE  = "rbac/perm_view";
    private static final String PAGE_TITLE = "View Permission";

    @Autowired
    private RbacFacade          facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String ep_view(
            @PathVariable int permId,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        Permission permission = facade.getPermission(permId);
        List<Role> roles = facade.listRolesByPermission(permId);
        List<Role> allRoles = facade.listRoles();
        allRoles.removeAll(roles);

        // Respond
        model.addAttribute("permission", permission);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("roles", roles);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }
}
