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
@RequestMapping("/rbac/permission/list")
public class ListPermissionsController
{
    @Autowired
    private RbacFacade facade;
    private Logger log = LoggerFactory.getLogger(this.getClass());



    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String ep(Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        model.addAttribute("permissions", facade.listPermissions());
        model.addAttribute("pageTitle", "List Permissions");
        model.addAttribute("pageFile", "rbac/perm_list");
        return "admin/base";
    }
}