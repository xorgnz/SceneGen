package org.memehazard.wheel.scene.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.scene.facade.SceneFacade;
import org.memehazard.wheel.scene.model.Scene;
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
@RequestMapping("/scene/{id}/delete")
public class DeleteSceneController
{
    private static final String PAGE_FILE  = "scene/scene_delete";
    private static final String PAGE_TITLE = "Delete Scene";

    @Autowired
    private SceneFacade         facade;
    private Logger              log        = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String form(@PathVariable int id, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        Scene scene = facade.getScene(id);

        // Respond
        model.addAttribute("scene", scene);
        model.addAttribute("sceneParts", scene.getFragments());
        model.addAttribute("pageTitle", PAGE_TITLE);
        model.addAttribute("pageFile", PAGE_FILE);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String submit(
            @PathVariable int id,
            SessionStatus status,
            Model model,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Processing form - " + request.getServletPath());

        // Act
        Scene obj = facade.getScene(id);
        facade.deleteScene(id);

        // Redirect
        flash.addFlashAttribute("msg_good", "Successfully deleted scene " + obj.toString());
        flash.addFlashAttribute("refresh", "frame-side");
        status.setComplete();
        return "redirect:/admin/blank-main";
    }
}