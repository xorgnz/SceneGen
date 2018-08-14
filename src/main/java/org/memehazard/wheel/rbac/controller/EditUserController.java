package org.memehazard.wheel.rbac.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.memehazard.exceptions.XMLException;
import org.memehazard.wheel.core.validator.StrongPassword;
import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.rbac.validator.PasswordConfirmMatches;
import org.memehazard.wheel.rbac.validator.UniqueUserEmail;
import org.memehazard.wheel.rbac.validator.UniqueUsername;
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
@RequestMapping("/rbac/user/{id}/edit")
@SessionAttributes("co")
public class EditUserController
{
    private static final String PAGE_FILE  = "rbac/user_form";
    private static final String PAGE_TITLE = "Edit User";

    @Autowired
    private RbacFacade          facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable int id, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        User u = facade.getUser(id);
        CommandObject co = new CommandObject(u);

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
            User u = co.getUser();
            if (!co.getPasswords().getPassword().equals(""))
                u.encodeAndSetPassword(co.getPasswords().getPassword());
            facade.updateUser(u);

            // Redirect
            flash.addFlashAttribute("msg_good", "Success!!");
            flash.addFlashAttribute("refresh", "frame-side");
            status.setComplete();
            return "redirect:/rbac/user/" + u.getId() + "/edit";
        }
    }


    public static class CommandObject
    {
        @Valid
        private PasswordCommandObject passwords;
        @Valid
        @UniqueUsername
        @UniqueUserEmail
        private User                  user;


        public CommandObject(User u)
        {
            this.user = u;
        }


        public PasswordCommandObject getPasswords()
        {
            return passwords;
        }


        public User getUser()
        {
            return user;
        }


        public void setPasswords(PasswordCommandObject passwords)
        {
            this.passwords = passwords;
        }


        public void setUser(User user)
        {
            this.user = user;
        }
    }


    @PasswordConfirmMatches
    public static class PasswordCommandObject
    {
        @NotNull
        @StrongPassword
        private String password;

        @NotNull
        private String passwordConfirm;


        public String getPassword()
        {
            return password;
        }


        public String getPasswordConfirm()
        {
            return passwordConfirm;
        }


        public void setPassword(String password)
        {
            this.password = password;
        }


        public void setPasswordConfirm(String passwordConfirm)
        {
            this.passwordConfirm = passwordConfirm;
        }
    }
}