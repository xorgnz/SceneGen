package org.memehazard.wheel.asset.controller;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.asset.facade.AssetFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/asset/set/{setId}/updateStyleTags")
@SessionAttributes("co")
public class UpdateStyleTagsController
{
    @Autowired
    private AssetFacade facade;
    private Logger         log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public String form(

            @PathVariable int setId,
            Model model,
            SessionStatus status,
            RedirectAttributes flash,
            HttpServletRequest request)
    {
        log.trace("Performing controller action - " + request.getServletPath());

        // Act
        try
        {
            facade.updateStyleTags(setId);

            // Redirect
            flash.addFlashAttribute("msg_good", "Style tag update process begun. Styles will update in the background");
            status.setComplete();
            return "redirect:/asset/set/" + setId;

        }
        catch (Exception e)
        {
            log.trace("Class List Update failed", e);

            // Redirect
            flash.addFlashAttribute("msg_bad",
                    "Style tag update failed for set " + setId + " with error:\n" + e.getMessage());
            status.setComplete();
            return "redirect:/asset/set/" + setId;
        }
    }
}