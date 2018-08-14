package org.memehazard.wheel.rbac.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.Role;
import org.memehazard.wheel.rbac.model.User;
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
@RequestMapping("/rbac/user_role_link/assign")
public class AssignUserRoleLinkController
{
    @Autowired
    private RbacFacade facade;
    private Logger     log = LoggerFactory.getLogger(getClass());


    @RequestMapping(value = "/{userId}/{roleId}", method = RequestMethod.GET)
    @Transactional
    public String performGet(
            @PathVariable int userId,
            @PathVariable int roleId,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());
        return process(userId, roleId, flash, request);
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String performPost(
            @ModelAttribute("userId") int userId,
            @ModelAttribute("roleId") int roleId,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        return process(userId, roleId, flash, request);
    }


    public String process(int userId, int roleId, RedirectAttributes flash, HttpServletRequest request)
    {
        // Act
        facade.assignRoleToUser(roleId, userId);

        // Response message
        User u = facade.getUser(userId);
        Role r = facade.getRole(roleId);
        flash.addFlashAttribute("msg_good", String.format("Assigned role \"%s\" to user \"%s\"", r.getName(), u.toNameString()));

        // Redirect
        return "redirect:" + request.getHeader("referer");
    }
}