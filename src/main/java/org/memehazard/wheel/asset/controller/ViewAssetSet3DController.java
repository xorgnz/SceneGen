/**
 *
 */
package org.memehazard.wheel.asset.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.memehazard.exceptions.XMLException;
import org.memehazard.wheel.asset.facade.AssetFacade;
import org.memehazard.wheel.asset.model.Asset;
import org.memehazard.wheel.viewer.controller.AVAControllerHelper;
import org.memehazard.wheel.viewer.view.SceneContentDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/asset/set/{assetSetId}/view3D")
public class ViewAssetSet3DController
{
    private static final String PAGE_FILE_CHOOSE_ASSETS = "asset/set_view3d";
    private static final String PAGE_TITLE              = "Anatomy Viewer Application";

    @Autowired
    private AssetFacade         facade;
    private Logger              log                     = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String ep_form(@PathVariable int assetSetId, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        List<Asset> assets = facade.listAssetsBySet(assetSetId);

        // Respond
        model.addAttribute("assets", assets);
        model.addAttribute("co", new CommandObject());
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE_CHOOSE_ASSETS);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional(readOnly = true)
    public String ep_scene(
            @PathVariable int assetSetId,
            @ModelAttribute("co") @Valid CommandObject co,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
            throws
            IOException,
            XMLException
    {
        log.trace("Generating scene - " + request.getServletPath());

        SceneContentDescriptor scnd = facade.generateSceneContentFromCustomAssetSet(co.getAssetIds());

        return AVAControllerHelper.generateControllerResponse(scnd, model);
    }


    public static class CommandObject
    {
        int[] assetIds;


        public int[] getAssetIds()
        {
            return assetIds;
        }


        public void setAssetIds(int[] assetIds)
        {
            this.assetIds = assetIds;
        }
    }

}
