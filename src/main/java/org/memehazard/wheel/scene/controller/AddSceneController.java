package org.memehazard.wheel.scene.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.memehazard.wheel.scene.facade.SceneFacade;
import org.memehazard.wheel.scene.model.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/scene/add")
public class AddSceneController
{
    private static final String PAGE_FILE          = "scene/scene_add";
    private static final String PAGE_FILE_RESPONSE = "scene/scene_add_response";
    private static final String PAGE_TITLE         = "Create New Scene";


    @Autowired
    private SceneFacade         facade;

    private Logger              log                = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String performGet(Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Respond
        model.addAttribute("co", new CommandObject());
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String performPost(
            @ModelAttribute("co") @Valid CommandObject co,
            BindingResult result,
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
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
            Scene scn = facade.addScene(co.getName());

            ReflectionToStringBuilder.toString(scn);

            model.addAttribute("scene", scn);
            model.addAttribute("msg_good", "Success!!");
            model.addAttribute("refresh", "frame-side");
            model.addAttribute("pageTitle", PAGE_TITLE);
            model.addAttribute("pageFile", PAGE_FILE_RESPONSE);
            // status.setComplete(); - Eliminated. Not sure why it's needed.
            return "admin/base";
        }
    }


    public static class CommandObject
    {
        @NotEmpty(message = "{Scene.name.empty}")
        private String name;


        public String getName()
        {
            return name;
        }


        public void setName(String name)
        {
            this.name = name;
        }

    }
}