package org.memehazard.wheel.dataviz.controller.web;
//package org.memehazard.wheel.dataviz.controller;
//
//import java.io.IOException;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//
//import org.memehazard.exceptions.XMLException;
//import org.memehazard.wheel.asset.facade.AssetFacade;
//import org.memehazard.wheel.core.validator.MultipartFileHasExtension;
//import org.memehazard.wheel.core.validator.MultipartFileUploaded;
//import org.memehazard.wheel.query.parser.ParserException;
//import org.memehazard.wheel.scene.facade.SceneGenerationFacade;
//import org.memehazard.wheel.style.facade.StyleFacade;
//import org.memehazard.wheel.viewer.controller.AVAControllerHelper;
//import org.memehazard.wheel.viewer.view.SceneContentDescriptor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.ui.Model;
//import org.springframework.util.StringUtils;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.SessionAttributes;
//import org.springframework.web.bind.support.SessionStatus;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//@Controller
//@RequestMapping("/scene/fromFile")
//@SessionAttributes("co")
//public class GenerateFromFileController
//{
//    private static final String   PAGE_FILE_UPLOAD_DATA  = "scene/generate_from_file";
//    private static final String   PAGE_TITLE_UPLOAD_DATA = "Generate Scene From File";
//
//    // Components
//    @Autowired
//    private SceneGenerationFacade facade;
//    @Autowired
//    private AssetFacade           facade_asset;
//    @Autowired
//    private StyleFacade           facade_style;
//    private Logger                log                    = LoggerFactory.getLogger(this.getClass());
//
//
//    @RequestMapping(method = RequestMethod.GET)
//    @Transactional(readOnly = true)
//    public String form(
//            Model model,
//            HttpServletRequest request)
//            throws IOException, XMLException
//    {
//        log.trace("Generating page - " + request.getServletPath());
//
//        // Prepare form selects
//        model.addAttribute("assetSets", facade_asset.listAssetSets());
//        model.addAttribute("stylesheets", facade_style.listStylesheets());
//
//        // Respond
//        model.addAttribute("co", new CommandObject());
//        model.addAttribute("pageTitle", PAGE_TITLE_UPLOAD_DATA);
//        model.addAttribute("pageFile", PAGE_FILE_UPLOAD_DATA);
//        return "admin/base";
//    }
//
//
//    @RequestMapping(method = RequestMethod.POST)
//    @Transactional
//    public String submit(
//            @ModelAttribute("co") @Valid CommandObject co,
//            BindingResult result,
//            SessionStatus status,
//            Model model,
//            RedirectAttributes flash,
//            HttpServletRequest request)
//            throws ParserException, IOException
//    {
//        log.trace("Processing form - " + request.getServletPath());
//
//        // Failed validation
//        if (result.hasErrors())
//        {
//            log.trace("Validation failed - redisplaying form");
//
//            // Respond
//            model.addAttribute("pageTitle", PAGE_TITLE_UPLOAD_DATA);
//            model.addAttribute("pageFile", PAGE_FILE_UPLOAD_DATA);
//            model.addAttribute("assetSets", facade_asset.listAssetSets());
//            model.addAttribute("stylesheets", facade_style.listStylesheets());
//            return "admin/base";
//        }
//        else
//        {
//            log.trace("Validation succeeded - generating scene");
//
//            // Compose scene
//            String fileContents = new String(co.getFile().getBytes(), "UTF-8");
//            SceneContentDescriptor scnd = null;
//            if (StringUtils.endsWithIgnoreCase(co.getFile().getOriginalFilename(), ".csv"))
//                scnd = facade.generateSceneContentFromDataFile(fileContents, "csv", co.getAssetSetId(), co.getStylesheetId());
//            else
//                scnd = facade.generateSceneContentFromDataFile(fileContents, "xml", co.getAssetSetId(), co.getStylesheetId());
//
//            status.setComplete();
//            return AVAControllerHelper.generateControllerResponse(scnd, null, model);
//        }
//    }
//
//
//    public static class CommandObject
//    {
//        private Integer       assetSetId;
//        @MultipartFileUploaded(message = "{UploadSceneFile.fileMissing}")
//        @MultipartFileHasExtension(extensions = "csv,xml", message = "{UploadSceneFile.fileType}")
//        private MultipartFile file;
//        private Integer       stylesheetId;
//
//
//        public Integer getAssetSetId()
//        {
//            return assetSetId;
//        }
//
//
//        public MultipartFile getFile()
//        {
//            return file;
//        }
//
//
//        public Integer getStylesheetId()
//        {
//            return stylesheetId;
//        }
//
//
//        public void setAssetSetId(Integer assetSetId)
//        {
//            this.assetSetId = assetSetId;
//        }
//
//
//        public void setFile(MultipartFile file)
//        {
//            this.file = file;
//        }
//
//
//        public void setStylesheetId(Integer stylesheetId)
//        {
//            this.stylesheetId = stylesheetId;
//        }
//    }
//}
