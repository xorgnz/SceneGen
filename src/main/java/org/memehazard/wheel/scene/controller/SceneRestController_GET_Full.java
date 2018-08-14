package org.memehazard.wheel.scene.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.scene.facade.SceneFacade;
import org.memehazard.wheel.scene.view.SceneDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/REST/scene/{id}/full")
public class SceneRestController_GET_Full
{
    @Autowired
    private SceneFacade facade;
    private Logger      log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject performGet(
            @PathVariable int id,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ParserException, IOException

    {
        log.trace("Handling REST request - " + request.getServletPath() + " - " + request.getMethod());

        // Act
        SceneDescriptor scnd = facade.generateSceneDescriptor(id);

        return new RestResponseObject(true, "Success", scnd);
    }


    public static class RestResponseObject extends BasicRestResponseObject
    {
        private SceneDescriptor descriptor = null;


        public RestResponseObject(boolean success, String message, SceneDescriptor descriptor)
        {
            super(success, message);
            this.descriptor = descriptor;
        }


        public SceneDescriptor getDescriptor()
        {
            return descriptor;
        }
    }
}
