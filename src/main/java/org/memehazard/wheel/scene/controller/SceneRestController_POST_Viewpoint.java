package org.memehazard.wheel.scene.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.scene.facade.SceneFacade;
import org.memehazard.wheel.viewer.model.Viewpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/REST/scene/{id}/viewpoint")
public class SceneRestController_POST_Viewpoint
{
    @Autowired
    private SceneFacade facade;
    private Logger      log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseBody
    public BasicRestResponseObject performPost(
            @PathVariable int id,
            @RequestBody CommandObject co,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        log.trace("Handling REST request - " + request.getServletPath() + " - " + request.getMethod());
        log.info("Updating scene viewpoint: " + co.getViewpoint().toString(), "\n", "");
        
        try
        {
            // Act
            facade.updateSceneWithViewpoint(id, co.getViewpoint());

            return new BasicRestResponseObject(true, "");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new BasicRestResponseObject(false, "Exception: " + e.getMessage() + " - see server log");
        }
    }


    public static class CommandObject
    {
        public Viewpoint viewpoint;


        public Viewpoint getViewpoint()
        {
            return viewpoint;
        }


        public void setViewpoint(Viewpoint viewpoint)
        {
            this.viewpoint = viewpoint;
        }
    }
}
