package org.memehazard.wheel.asset.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.memehazard.exceptions.XMLException;
import org.memehazard.wheel.asset.facade.AssetFacade;
import org.memehazard.wheel.asset.model.Asset;
import org.memehazard.wheel.core.validator.MultipartFileHasExtension;
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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/asset/asset/{assetId}/edit")
@SessionAttributes("co")
public class EditAssetController
{
    private static final String PAGE_FILE  = "asset/asset_form";
    private static final String PAGE_TITLE = "Edit Asset";

    @Autowired
    private AssetFacade      facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable int assetId, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        CommandObject co = new CommandObject(facade.getAsset(assetId));

        // Respond
        model.addAttribute("co", co);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @ModelAttribute("co") @Valid CommandObject co,
            BindingResult result,
            Model model,
            SessionStatus status,
            RedirectAttributes flash,
            HttpServletRequest request)
            throws IOException, XMLException
    {
        log.trace("Processing form - " + request.getServletPath());

        // Failed binding & validation
        if (result.hasErrors())
        {
            log.trace("Validation failed - redisplaying form");

            // Respond
            model.addAttribute("pageTitle", PAGE_TITLE);
            model.addAttribute("pageFile", PAGE_FILE);
            return "admin/base";
        }

        // Passed binding & validation
        else
        {
            log.trace("Validation succeeded - saving object");

            // Act
            try
            {
                facade.updateAsset(co.getAsset(), co.getFile());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                log.trace("Asset import failed - redisplaying form");

                model.addAttribute("msg_bad", "Unable to process asset file. Please re-upload");
                model.addAttribute("pageTitle", PAGE_TITLE);
                model.addAttribute("pageFile", PAGE_FILE);
                return "admin/base";
            }

            // Redirect
            flash.addFlashAttribute("msg_good", "Success!!");
            flash.addFlashAttribute("refresh", "frame-side");
            status.setComplete();
            return "redirect:/asset/set/" + co.getAsset().getAssetSet().getId();
        }
    }


    public static class CommandObject
    {
        @Valid
        private Asset         asset;

        @MultipartFileHasExtension(extensions = "obj,x3d", message = "{EditAssetForm.fileType}")
        private MultipartFile file;


        public CommandObject(Asset asset)
        {
            this.asset = asset;
        }


        public Asset getAsset()
        {
            return asset;
        }


        public MultipartFile getFile()
        {
            return file;
        }


        public void setAsset(Asset asset)
        {
            this.asset = asset;
        }


        public void setFile(MultipartFile file)
        {
            this.file = file;
        }
    }
}