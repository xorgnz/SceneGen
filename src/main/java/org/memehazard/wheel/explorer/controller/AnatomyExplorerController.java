/**
 *
 */
package org.memehazard.wheel.explorer.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.memehazard.wheel.asset.facade.AssetFacade;
import org.memehazard.wheel.asset.model.Asset;
import org.memehazard.wheel.explorer.facade.ExplorerFacade;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.style.facade.StyleFacade;
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

/**
 * @author xorgnz
 * 
 */
@Controller
@RequestMapping(value = "/explorer")
public class AnatomyExplorerController
{
    private static final String PAGE_FILE_AE_FORM   = "explorer/ae_form";
    private static final String PAGE_FILE_AE_VIEWER = "explorer/ae_viewer";
    private static final String PAGE_TITLE          = "Anatomy Explorer";

    @Autowired
    private ExplorerFacade      facade;
    @Autowired
    private AssetFacade         facade_asset;
    @Autowired
    private StyleFacade         facade_style;

    private Logger              log                 = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String ep_form(Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Prepare form selects
        model.addAttribute("assetSets", facade_asset.listAssetSetsWithAssets());
        model.addAttribute("stylesheets", facade_style.listStylesheets());

        // Prepare form
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE_AE_FORM);
        model.addAttribute("co", new CommandObject());
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String ep_scene(
            @ModelAttribute("co") @Valid CommandObject co,
            BindingResult bResult,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ParserException
    {
        log.trace("Processing form - " + request.getServletPath());

        return out_scene(co.getAssetId(), co.getStylesheetId(), model);
    }


    @RequestMapping(value = "/{assetId}/{stylesheetId}", method = RequestMethod.GET)
    @Transactional
    public String ep_sceneUrl(
            @PathVariable int assetId,
            @PathVariable int stylesheetId,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ParserException
    {
        log.trace("Processing GET request - " + request.getServletPath());

        return out_scene(assetId, stylesheetId, model);
    }


    private String out_scene(int assetId, int stylesheetId, Model model)
            throws IOException, ParserException
    {
        // Retrieve initial scene object
        Asset a = facade_asset.getAsset(assetId);

        // Respond
        model.addAttribute("entityId", a.getEntityId());
        model.addAttribute("assetSetId", facade_asset.getAsset(assetId).getAssetSet().getId());
        model.addAttribute("stylesheetId", stylesheetId);
        return PAGE_FILE_AE_VIEWER;
    }


    public static class CommandObject
    {
        private Integer assetId;
        private Integer stylesheetId;


        public Integer getAssetId()
        {
            return assetId;
        }


        public Integer getStylesheetId()
        {
            return stylesheetId;
        }


        public void setAssetId(Integer assetId)
        {
            this.assetId = assetId;
        }


        public void setStylesheetId(Integer stylesheetId)
        {
            this.stylesheetId = stylesheetId;
        }
    }
}
