/**
 *
 */
package org.memehazard.wheel.asset.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.asset.facade.AssetFacade;
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

/**
 * @author xorgnz
 *
 */

@Controller
@RequestMapping("/asset/{assetId}/view3D")
public class ViewAsset3DController
{
    @Autowired
    private AssetFacade facade;
    private Logger                log                  = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String ep_scene(@PathVariable int assetId, Model model, HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());
        
        // Create scene
        SceneContentDescriptor scnd = facade.generateSceneContentFromAsset(assetId);

        // Respond
        return AVAControllerHelper.generateControllerResponse(scnd, model);
    }
}
