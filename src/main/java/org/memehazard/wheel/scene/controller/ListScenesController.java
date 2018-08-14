package org.memehazard.wheel.scene.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.scene.facade.SceneFacade;
import org.memehazard.wheel.scene.model.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/scene/list")
public class ListScenesController
{
    @Autowired
    private SceneFacade facade;
    private Logger      log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String performGet(
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        List<Scene> objs = facade.listScenes();

        // Respond
        model.addAttribute("objs", objs);
        model.addAttribute("pageTitle", "List Scenes");
        model.addAttribute("pageFile", "scene/scene_list");
        return "admin/base";
    }
}
