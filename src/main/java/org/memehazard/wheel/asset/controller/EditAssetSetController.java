package org.memehazard.wheel.asset.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.memehazard.wheel.asset.facade.AssetFacade;
import org.memehazard.wheel.asset.model.AssetSet;
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


@Controller
@RequestMapping("/asset/set/{setId}/edit")
@SessionAttributes("assetSet")
public class EditAssetSetController
{
    private static final String PAGE_FILE  = "asset/set_form";
    private static final String PAGE_TITLE = "Edit Asset Set";

    @Autowired
    private AssetFacade         facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable int setId, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        AssetSet obj = facade.getAssetSet(setId);

        // Respond
        model.addAttribute("assetSet", obj);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @ModelAttribute("assetSet") @Valid AssetSet obj,
            BindingResult result,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        // Passed binding & validation
        if (!result.hasErrors())
        {
            // Act
            facade.updateAssetSet(obj);
            model.addAttribute("msg_good", "Success!!");
            model.addAttribute("refresh", "frame-side");
        }

        // Respond
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);

        return "admin/base";
    }
}