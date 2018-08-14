package org.memehazard.wheel.asset.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.asset.facade.AssetFacade;
import org.memehazard.wheel.asset.view.AssetDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/asset")
public class ViewAssetController
{
    @Autowired
    private AssetFacade facade;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping("/asset/{id}")
    @Transactional(readOnly = true)
    public String getAsset(
            @PathVariable int id,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Respond
        model.addAttribute("obj", new AssetDescriptor(facade.getAsset(id)));
        model.addAttribute("pageTitle", "View Asset");
        model.addAttribute("pageFile", "asset/asset_view");
        return "admin/base";
    }
}
