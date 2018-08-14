package org.memehazard.wheel.asset.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.asset.facade.AssetFacade;
import org.memehazard.wheel.asset.model.AssetSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/asset/set/{setId}/delete")
public class DeleteAssetSetController
{
    private static final String PAGE_FILE  = "asset/set_delete";
    private static final String PAGE_TITLE = "Delete Asset Set";

    @Autowired
    private AssetFacade      facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable int setId, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Respond
        model.addAttribute("set", facade.getAssetSet(setId));
        model.addAttribute("assets", facade.listAssetsBySet(setId));
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @PathVariable int setId,
            SessionStatus status,
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        // Act
        AssetSet obj = facade.getAssetSet(setId);
        facade.deleteAssetSet(setId);

        // Redirect
        flash.addFlashAttribute("msg_good", "Successfully deleted asset set " + obj.toString());
        flash.addFlashAttribute("refresh", "frame-side");
        status.setComplete();
        return "redirect:/admin/blank-main";
    }
}