package org.memehazard.wheel.asset.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.asset.facade.AssetFacade;
import org.memehazard.wheel.asset.model.Asset;
import org.memehazard.wheel.asset.view.AssetDescriptor;
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
@RequestMapping("/asset/asset/{assetId}/delete")
public class DeleteAssetController
{
    private static final String PAGE_FILE  = "asset/asset_delete";
    private static final String PAGE_TITLE = "Delete Asset";

    @Autowired
    private AssetFacade      facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable int assetId, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Respond
        model.addAttribute("obj", new AssetDescriptor(facade.getAsset(assetId)));
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @PathVariable int assetId,
            SessionStatus status,
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        // Act
        Asset obj = facade.getAsset(assetId);
        facade.deleteAsset(assetId);

        // Redirect
        flash.addFlashAttribute("msg_good", "Successfully deleted asset " + obj.toString());
        flash.addFlashAttribute("refresh", "frame-side");
        status.setComplete();
        return "redirect:/asset/set/" + obj.getAssetSet().getId();
    }
}