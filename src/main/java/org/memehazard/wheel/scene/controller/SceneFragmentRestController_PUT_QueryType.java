package org.memehazard.wheel.scene.controller;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/REST/sceneFragment/queryType")
public class SceneFragmentRestController_PUT_QueryType
{
    @Autowired
    private SceneFacade facade;
    private Logger      log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject performPut(
            @ModelAttribute("co") CommandObject co,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        log.trace("Handling REST request - " + request.getServletPath() + " - " + request.getMethod());

        try
        {
            SceneFragment scnf = facade.addSceneFragmentFromQueryParams(co.sceneId, co.name, co.stylesheetId, co.assetSetId,
                    co.queryId, co.getQueryParamValueMap());

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
        private int                       assetSetId;
        private String                    name;
        private int                       queryId;
        private List<Map<String, String>> queryParams;
        private int                       sceneId;
        private int                       stylesheetId;


        public int getAssetSetId()
        {
            return assetSetId;
        }


        public String getName()
        {
            return name;
        }


        public int getQueryId()
        {
            return queryId;
        }


        public List<Map<String, String>> getQueryParams()
        {
            return queryParams;
        }


        public Map<String, String> getQueryParamValueMap()
        {
            Map<String, String> qpvMap = new TreeMap<String, String>();

            if (queryParams != null)
                for (Map<String, String> co : queryParams)
                    qpvMap.put(co.get("name"), co.get("value"));

            return qpvMap;
        }


        public int getSceneId()
        {
            return sceneId;
        }


        public int getStylesheetId()
        {
            return stylesheetId;
        }


        public void setAssetSetId(int assetSetId)
        {
            this.assetSetId = assetSetId;
        }


        public void setName(String name)
        {
            this.name = name;
        }


        public void setQueryId(int queryId)
        {
            this.queryId = queryId;
        }


        public void setQueryParams(List<Map<String, String>> queryParams)
        {
            this.queryParams = queryParams;
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
