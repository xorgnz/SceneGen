package org.memehazard.wheel.scene.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.scene.facade.SceneFacade;
import org.memehazard.wheel.scene.model.SceneFragment;
import org.memehazard.wheel.scene.model.SimpleSceneFragmentMember;
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
@RequestMapping("/REST/sceneFragment/listType/fromMembers")
public class SceneFragmentRestController_PUT_ListType_FromSimpleMembers
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

        System.err.println("finish!");

        try
        {
            SceneFragment scnf = facade.addSceneFragment(co.sceneId, co.name, null, null);

            facade.addSceneFragmentMemberFromSimpleMember(scnf, co.members);

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
        public List<SimpleSceneFragmentMember> members;
        public String                          name;
        public int                             sceneId;


        public void setMembers(List<SimpleSceneFragmentMember> members)
        {
            this.members = members;
        }


        public void setName(String name)
        {
            this.name = name;
        }


        public void setSceneId(int sceneId)
        {
            this.sceneId = sceneId;
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
