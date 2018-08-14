package org.memehazard.wheel.scene.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.exceptions.XMLException;
import org.memehazard.wheel.scene.facade.SceneFacade;
import org.memehazard.wheel.viewer.controller.AVAControllerHelper;
import org.memehazard.wheel.viewer.view.SceneContentDescriptor;
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
public class ViewScene3DController
{
    @Autowired
    private SceneFacade facade;
    private Logger      log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(value = "/scene/{id}", method = RequestMethod.GET)
    @Transactional
    public String ep_sceneUrl(
            @PathVariable int id,
            Model model,
            HttpServletRequest request)
            throws IOException, XMLException
    {
        log.trace("Generating page - " + request.getServletPath());
        SceneContentDescriptor scnd = facade.generateSceneContentDescriptor(id);

        return AVAControllerHelper.generateControllerResponse(scnd, model);
    }
}
