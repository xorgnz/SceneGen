package org.memehazard.wheel.rbac.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.Permission;
import org.memehazard.wheel.rbac.model.Role;
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
@RequestMapping("/rbac/role_perm_link/assign")
public class AssignRolePermLinkController
{
    @Autowired
    private RbacFacade facade;
    private Logger     log = LoggerFactory.getLogger(getClass());


    @RequestMapping(value = "/{roleId}/{permId}", method = RequestMethod.GET)
    @Transactional
    public String performGet(
            @PathVariable int roleId,
            @PathVariable int permId,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());
        return process(roleId, permId, flash, request);
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String performPost(
            @ModelAttribute("roleId") int roleId,
            @ModelAttribute("permId") int permId,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        return process(roleId, permId, flash, request);
    }


    public String process(int roleId, int permId, RedirectAttributes flash, HttpServletRequest request)
    {
        // Act
        facade.assignPermissionToRole(permId, roleId);

        // Response message
        Permission p = facade.getPermission(permId);
        Role r = facade.getRole(roleId);
        flash.addFlashAttribute("msg_good", String.format("Assigned permission \"%s\" to role \"%s\"", p.getName(), r.getName()));

        // Redirect
        return "redirect:" + request.getHeader("referer");
    }


}