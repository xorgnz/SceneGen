package org.memehazard.wheel.asset.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.asset.facade.AssetFacade;
import org.memehazard.wheel.asset.model.AssetSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/asset")
public class ListAssetSetsController
{
    @Autowired
    private AssetFacade facade;
    private Logger      log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping("/set/list")
    @Transactional(readOnly = true)
    public String listSets(
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        List<AssetSet> sets = facade.listAssetSets();

        // Respond
        model.addAttribute("assetSets", sets);
        model.addAttribute("pageTitle", "List Asset Sets");
        model.addAttribute("pageFile", "asset/set_list");
        return "admin/base";
    }
}
