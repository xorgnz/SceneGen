package org.memehazard.wheel.rbac.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.rbac.facade.RbacFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/rbac/user/list")
public class ListUsersController
{
    @Autowired
    private RbacFacade facade;
    private Logger     log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String listUsers(Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        model.addAttribute("users", facade.listUsers());
        model.addAttribute("pageTitle", "List Users");
        model.addAttribute("pageFile", "rbac/user_list");
        return "admin/base";
    }
}