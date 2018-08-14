package org.memehazard.wheel.scene.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.scene.facade.SceneFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/REST/sceneFragment/{id}/listType/fromAssetIds")
public class SceneFragmentRestController_POST_ListType_FromAssetIds
{
    @Autowired
    private SceneFacade facade;
    private Logger      log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject performPost(
            @PathVariable int id,
            @RequestBody CommandObject co,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        log.trace("Handling REST request - " + request.getServletPath() + " - " + request.getMethod());

        System.err.println(co.name);
        for (int i : co.assetIds)
            System.err.println("" + i);
        
        try
        {
            facade.updateSceneFragmentFromAssetList(id, co.name, co.stylesheetId, co.assetSetId,
                    co.assetIds);

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
        public int    assetSetId;
        public String name;
        public int[]  assetIds;
        public int    stylesheetId;


        public int getAssetSetId()
        {
            return assetSetId;
        }


        public String getName()
        {
            return name;
        }


        public int getStylesheetId()
        {
            return stylesheetId;
        }


        public void setAssetIds(int[] assetIds)
        {
            this.assetIds = assetIds;
        }


        public void setAssetSetId(int assetSetId)
        {
            this.assetSetId = assetSetId;
        }


        public void setName(String name)
        {
            this.name = name;
        }


        public void setStylesheetId(int stylesheetId)
        {
            this.stylesheetId = stylesheetId;
        }
    }
}
