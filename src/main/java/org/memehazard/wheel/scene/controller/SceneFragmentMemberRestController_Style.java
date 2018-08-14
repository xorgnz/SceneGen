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
@RequestMapping("/REST/sceneFragmentMember/{id}/style")
public class SceneFragmentMemberRestController_Style
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

        try
        {
            facade.updateSceneFragmentMemberStyle(id, co.getAmbient(), co.getDiffuse(), co.getEmissive(), co.getSpecular(), co.getShininess(),
                    co.getAlpha());

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
        private double alpha;
        private String ambient;
        private String diffuse;
        private String emissive;
        private int    shininess;
        private String specular;


        public double getAlpha()
        {
            return alpha;
        }


        public String getAmbient()
        {
            return ambient;
        }


        public String getDiffuse()
        {
            return diffuse;
        }


        public String getEmissive()
        {
            return emissive;
        }


        public int getShininess()
        {
            return shininess;
        }


        public String getSpecular()
        {
            return specular;
        }


        public void setAlpha(double alpha)
        {
            this.alpha = alpha;
        }


        public void setAmbient(String ambient)
        {
            this.ambient = ambient;
        }


        public void setDiffuse(String diffuse)
        {
            this.diffuse = diffuse;
        }


        public void setEmissive(String emissive)
        {
            this.emissive = emissive;
        }


        public void setShininess(int shininess)
        {
            this.shininess = shininess;
        }


        public void setSpecular(String specular)
        {
            this.specular = specular;
        }
    }
}
