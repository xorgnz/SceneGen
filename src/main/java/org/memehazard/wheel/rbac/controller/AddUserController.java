package org.memehazard.wheel.rbac.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/rbac/user/add")
public class AddUserController
{
    private static final String PAGE_FILE  = "rbac/user_form";
    private static final String PAGE_TITLE = "Add User";


    @Autowired
    private RbacFacade          facade;
    private Logger              log        = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String performGet(Model model, HttpServletRequest request)
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
    public String performPost(
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
            log.trace("Validation succeeded - saving User");

            // Act
            User u = co.getUser();
            u.encodeAndSetPassword(co.getPasswords().getPassword());
            facade.addUser(u);

            // Redirect
            flash.addFlashAttribute("msg_good", "Success!!");
            flash.addFlashAttribute("refresh", "frame-side");
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
        @NotEmpty(message = "{User.clearPassword.empty}")
        private String password;

        @NotNull
        @NotEmpty(message = "{User.clearpasswordConfirm.empty}")
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