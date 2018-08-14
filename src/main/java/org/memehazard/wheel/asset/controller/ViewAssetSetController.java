package org.memehazard.wheel.asset.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.asset.facade.AssetFacade;
import org.memehazard.wheel.asset.model.Asset;
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

@Controller
@RequestMapping("/asset/set/{id}")
public class ViewAssetSetController
{
    @Autowired
    private AssetFacade facade;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String getSet(
            @PathVariable int id,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        AssetSet set = facade.getAssetSet(id);
        List<Asset> assets = facade.listAssetsBySet(id);

        // Respond
        model.addAttribute("set", set);
        model.addAttribute("assets", assets);
        model.addAttribute("pageTitle", "View Asset Set");
        model.addAttribute("pageFile", "asset/set_view");
        return "admin/base";
    }
}
