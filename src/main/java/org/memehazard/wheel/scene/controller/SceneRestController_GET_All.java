package org.memehazard.wheel.scene.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.scene.facade.SceneFacade;
import org.memehazard.wheel.scene.model.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/REST/scene")
public class SceneRestController_GET_All
{
    @Autowired
    private SceneFacade facade;
    private Logger      log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject performGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ParserException, IOException

    {
        log.trace("Handling REST request - " + request.getServletPath() + " - " + request.getMethod());

        // Act
        List<Scene> scenes = facade.listScenes();

        return new RestResponseObject(true, "Success", scenes);
    }


    public static class RestResponseObject extends BasicRestResponseObject
    {
        private List<Scene> scenes = null;


        public RestResponseObject(boolean success, String message, List<Scene> scenes)
        {
            super(success, message);
            this.scenes = scenes;
        }


        public List<Scene> getScenes()
        {
            return scenes;
        }
    }
}
