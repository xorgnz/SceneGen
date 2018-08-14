package org.memehazard.wheel.asset.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.memehazard.wheel.asset.facade.AssetFacade;
import org.memehazard.wheel.asset.model.AssetSet;
import org.memehazard.wheel.core.validator.MultipartFileHasExtension;
import org.memehazard.wheel.core.validator.MultipartFileUploaded;
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
@RequestMapping("/asset/set/{setId}/uploadZip")
@SessionAttributes("co")
public class UploadAssetZipController
{
    private static final String PAGE_FILE  = "asset/set_upload_zip";
    private static final String PAGE_TITLE = "Upload multiple assets";

    @Autowired
    private AssetFacade      facade;
    private Logger              log        = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable("setId") int setId, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        AssetSet set = facade.getAssetSet(setId);
        CommandObject co = new CommandObject();
        co.setAssetSet(set);

        // Respond
        model.addAttribute("co", co);
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @PathVariable("setId") int setId,
            @ModelAttribute("co") @Valid CommandObject co,
            BindingResult result,
            SessionStatus status,
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        // Failed validation
        if (result.hasErrors())
        {
            log.trace("Validation failed - redisplaying form");

            // Respond
            model.addAttribute("pageTitle", PAGE_TITLE);
            model.addAttribute("pageFile", PAGE_FILE);
            return "admin/base";
        }

        // Passed Validation
        else
        {
            log.trace("Validation succeeded - saving object");

            try
            {
                // Obtain file retrieval URL path
                String assetRetrievalPath = RetrieveFileController.getAssetRetrievalPath(request);

                // Act
                facade.addAssetZip(co.getAssetSet(), co.getFile(), assetRetrievalPath);
            }
            catch (Exception e)
            {
                log.trace("Zip import failed - redisplaying form", e);

                model.addAttribute("msg_bad",
                        "Exception occured while processing zip file. See log for details: " + e.getMessage());
                model.addAttribute("pageTitle", PAGE_TITLE);
                model.addAttribute("pageFile", PAGE_FILE);
                return "admin/base";
            }

            // Redirect
            flash.addFlashAttribute("msg_good", "Zip File successfully updated. Files will be processed and added in the background.");
            flash.addFlashAttribute("refresh", "frame-side");
            status.setComplete();
            return "redirect:/asset/set/" + co.getAssetSet().getId();
        }
    }


    public static class CommandObject
    {
        @MultipartFileUploaded(message = "{UploadAssetZip.fileMissing}")
        @MultipartFileHasExtension(extensions = "zip", message = "{UploadAssetZip.fileType}")
        private MultipartFile file;
        private AssetSet      assetSet;


        public AssetSet getAssetSet()
        {
            return assetSet;
        }


        public void setAssetSet(AssetSet assetSet)
        {
            this.assetSet = assetSet;
        }


        public MultipartFile getFile()
        {
            return file;
        }


        public void setFile(MultipartFile file)
        {
            this.file = file;
        }
    }
}