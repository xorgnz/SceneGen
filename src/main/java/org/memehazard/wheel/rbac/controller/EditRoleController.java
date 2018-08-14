package org.memehazard.wheel.rbac.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.memehazard.exceptions.XMLException;
import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.Role;
import org.memehazard.wheel.rbac.validator.UniqueRoleName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/rbac/role/{id}/edit")
@SessionAttributes("co")
public class EditRoleController
{
    private static final String PAGE_FILE  = "rbac/role_form";
    private static final String PAGE_TITLE = "Edit Role";

    @Autowired
    private RbacFacade          facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable int id, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        CommandObject co = new CommandObject(facade.getRole(id));

        // Respond
        model.addAttribute("co", co);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @ModelAttribute("co") @Valid CommandObject co,
            BindingResult result,
            Model model,
            SessionStatus status,
            RedirectAttributes flash,
            HttpServletRequest request)
            throws IOException, XMLException
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
            log.trace("Validation succeeded - saving object");

            // Act
            facade.updateRole(co.getRole());

            // Redirect
            flash.addFlashAttribute("msg_good", "Success!!");
            flash.addFlashAttribute("refresh", "frame-side");
            status.setComplete();
            return "redirect:/rbac/role/" + co.getRole().getId() + "/edit";
        }
    }


    public static class CommandObject
    {
        @Valid
        @UniqueRoleName
        private Role role;


        public CommandObject(Role role)
        {
            this.role = role;
        }


        public Role getRole()
        {
            return role;
        }


        public void setRole(Role role)
        {
            this.role = role;
        }
    }
}