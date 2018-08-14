package org.memehazard.wheel.scene.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.scene.facade.SceneFacade;
import org.memehazard.wheel.scene.model.SceneFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/REST/sceneFragment/listType/fromEntityIds")
public class SceneFragmentRestController_PUT_ListType_FromEntityIds
{
    @Autowired
    private SceneFacade facade;
    private Logger      log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject performPut(
            @RequestBody CommandObject co,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        log.trace("Handling REST request - " + request.getServletPath() + " - " + request.getMethod());

        System.err.println(co.name);
        for (int i : co.entityIds)
            System.err.println("" + i);
        
        try
        {
            SceneFragment scnf = facade.addSceneFragmentFromEntityList(co.sceneId, co.name, co.stylesheetId, co.assetSetId,
                    co.entityIds);

            return new RestResponseObject(true, "", scnf.getId());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new BasicRestResponseObject(false, "Exception: " + e.getMessage() + " - see server log");
        }
    }


    public static class CommandObject
    {
        public int[]  entityIds;
        public int    assetSetId;
        public String name;
        public int    sceneId;
        public int    stylesheetId;




        public void setEntityIds(int[] entityIds)
        {
            this.entityIds = entityIds;
        }


        public void setAssetSetId(int assetSetId)
        {
            this.assetSetId = assetSetId;
        }


        public void setName(String name)
        {
            this.name = name;
        }


        public void setSceneId(int sceneId)
        {
            this.sceneId = sceneId;
        }


        public void setStylesheetId(int stylesheetId)
        {
            this.stylesheetId = stylesheetId;
        }
    }


    public static class RestResponseObject extends BasicRestResponseObject
    {
        private int fragmentId;


        public RestResponseObject(boolean success, String message, int fragmentId)
        {
            super(success, message);

            this.fragmentId = fragmentId;
        }


        public int getFragmentId()
        {
            return fragmentId;
        }
    }
}
