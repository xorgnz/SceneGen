package org.memehazard.wheel.rbac.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.Permission;
import org.memehazard.wheel.rbac.validator.UniquePermissionName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/rbac/permission/add")
public class AddPermissionController
{
    private static final String PAGE_FILE  = "rbac/perm_form";
    private static final String PAGE_TITLE = "Add Permission";


    @Autowired
    private RbacFacade          facade;
    private Logger              log        = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String ep_form(Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Respond
        model.addAttribute("co", new CommandObject());
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String ep_submit(
            @ModelAttribute("co") @Valid CommandObject co,
            BindingResult result,
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        // Failed binding & validation
        if (result.hasErrors())
        {
            log.trace("Validation failed - redisplaying form");

            // Respond
            model.addAttribute("pageTitle", PAGE_TITLE);
            model.addAttribute("pageFile", PAGE_FILE);
            return "admin/base";
        }

        // Passed binding & validation
        else
        {
            log.trace("Validation succeeded - saving Permission");

            // Act
            facade.addPermission(co.getPermission());

            // Redirect
            flash.addFlashAttribute("msg_good", "Success!!");
            flash.addFlashAttribute("refresh", "frame-side");
            return "redirect:/rbac/permission/" + co.getPermission().getId() + "/edit";
        }
    }


    public static class CommandObject
    {
        @Valid
        @UniquePermissionName
        private Permission permission;


        public Permission getPermission()
        {
            return permission;
        }


        public void setPermission(Permission permission)
        {
            this.permission = permission;
        }
    }
}