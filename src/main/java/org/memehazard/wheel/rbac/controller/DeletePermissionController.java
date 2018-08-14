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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/rbac/permission/{id}/delete")
public class DeletePermissionController
{
    private static final String PAGE_FILE  = "rbac/perm_delete";
    private static final String PAGE_TITLE = "Delete Permission";

    @Autowired
    private RbacFacade          facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable int id, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        Permission permission = facade.getPermission(id);
        List<Role> roles = permission.getRoles();

        // Respond
        model.addAttribute("permission", permission);
        model.addAttribute("roles", roles);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @PathVariable int id,
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        // Act
        Permission p = facade.getPermission(id);
        facade.deletePermission(id);

        // Redirect
        flash.addFlashAttribute("msg_good", "Successfully deleted permission " + p.getName());
        flash.addFlashAttribute("refresh", "frame-side");
        return "redirect:/admin/blank-main";
    }
}