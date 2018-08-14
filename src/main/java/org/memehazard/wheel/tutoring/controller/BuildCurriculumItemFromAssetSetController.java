package org.memehazard.wheel.tutoring.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.memehazard.wheel.asset.facade.AssetFacade;
import org.memehazard.wheel.asset.model.Asset;
import org.memehazard.wheel.asset.model.AssetSet;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.CurriculumItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tutoring/curriculumItem/{ciId}/build/fromAssetSet")
public class BuildCurriculumItemFromAssetSetController
{
    private static final String PAGE_FILE_1  = "tutoring/ci_build_from_asset_set_1";
    private static final String PAGE_FILE_2  = "tutoring/ci_build_from_asset_set_2";
    private static final String PAGE_TITLE_1 = "Build Curriculum Item From Asset Set - Select Set";
    private static final String PAGE_TITLE_2 = "Build Curriculum Item From Asset Set - Select Assets";

    @Autowired
    private TutoringFacade      facade;
    @Autowired
    private AssetFacade      facade_asset;
    private Logger              log          = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String ep_form(
            @PathVariable Long ciId,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        log.trace("Generating page - " + request.getServletPath());

        List<AssetSet> assetSets = facade_asset.listAssetSets();
        CurriculumItem ci = facade.getCurriculumItem(ciId);

        // Respond
        model.addAttribute("curriculumItem", ci);
        model.addAttribute("assetSets", assetSets);
        model.addAttribute("pageTitle", PAGE_TITLE_1);
        model.addAttribute("pageFile", PAGE_FILE_1);
        return "admin/base";
    }


    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public String ep_submit(
            @PathVariable Long ciId,
            @ModelAttribute("co") @Valid CommandObject co,
            RedirectAttributes flash,
            Model model,
            SessionStatus status,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        System.err.println(co.getStage());


        if (co.getStage() == 1)
        {
            CurriculumItem ci = facade.getCurriculumItem(ciId);
            AssetSet assetSet = facade_asset.getAssetSet(co.getAssetSetId());
            List<Asset> assets = facade_asset.listAssetsBySet(co.getAssetSetId());

            model.addAttribute("curriculumItem", ci);
            model.addAttribute("assetSet", assetSet);
            model.addAttribute("assets", assets);
            model.addAttribute("pageTitle", PAGE_TITLE_2);
            model.addAttribute("pageFile", PAGE_FILE_2);
            return "admin/base";
        }
        else
        {
            try
            {
                AssetSet assetSet = facade_asset.getAssetSet(co.getAssetSetId());

                facade.addEntityKnowledgeFromAssets(ciId, co.getAssetSetId(), co.getAssetIds());

                // Redirect
                flash.addFlashAttribute("msg_good", "Successfully added entity knowledge from assets in " + assetSet.getName());
                flash.addFlashAttribute("refresh", "frame-side");
                status.setComplete();
                return "redirect:/tutoring/curriculumItem/" + ciId + "/build";
            }
            catch (IOException e)
            {
                model.addAttribute("msg_bad", "Unable to parse response from Query Integrator. " +
                                              "This could be caused by a mis-formed query, a problem in query execution, " +
                                              "or a failure within the Query Integrator. See the query log for more information.");
            }
            catch (ParserException fpe)
            {
                model.addAttribute("msg_bad", "A problem occured while communication with the Query Integrator. " +
                                              "Either the Query Integrator is down or a network problem has occurred. " +
                                              "Consider trying again as this might be a short term problem");
            }
            catch (Exception e)
            {
                model.addAttribute("msg_bad", "A problem occurred: " + e.getMessage());
                model.addAttribute("exception", ExceptionUtils.getFullStackTrace(e));
            }

            model.addAttribute("pageTitle", PAGE_TITLE_2);
            model.addAttribute("pageFile", PAGE_FILE_2);
            return "admin/base";
        }
    }


    public static class CommandObject
    {
        private int stage;
        private int assetSetId;
        int[]       assetIds;


        public int[] getAssetIds()
        {
            return assetIds;
        }


        public int getStage()
        {
            return stage;
        }


        public void setStage(int stage)
        {
            this.stage = stage;
        }


        public int getAssetSetId()
        {
            return assetSetId;
        }


        public void setAssetSetId(int assetSetId)
        {
            this.assetSetId = assetSetId;
        }


        public void setAssetIds(int[] assetIds)
        {
            this.assetIds = assetIds;
        }

    }
}
