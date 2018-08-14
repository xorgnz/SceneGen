package org.memehazard.wheel.scene.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.asset.facade.AssetFacade;
import org.memehazard.wheel.query.facade.QueryFacade;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.query.view.QueryDescriptor;
import org.memehazard.wheel.scene.facade.SceneFacade;
import org.memehazard.wheel.style.facade.StyleFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/scene/{id}/build")
public class SceneBuilderController
{
    private static final String PAGE_FILE = "scene/builder";

    @Autowired
    private SceneFacade         facade;

    @Autowired
    private StyleFacade         facade_style;

    @Autowired
    private QueryFacade         facade_query;

    @Autowired
    private AssetFacade         facade_asset;

    private Logger              log       = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public String performGet(
            @PathVariable int id,
            Model model,
            HttpServletRequest request)
            throws ParserException, IOException
    {
        log.trace("Generating page - " + request.getServletPath());

        // Prepare attributes
        List<QueryDescriptor> queryDescriptors = new ArrayList<QueryDescriptor>();
        for (Query q : facade_query.listQueries())
            queryDescriptors.add(new QueryDescriptor(q));

        // Respond
        model.addAttribute("queries", queryDescriptors);
        model.addAttribute("sceneId", id);
        model.addAttribute("stylesheets", facade_style.listStylesheets());
        model.addAttribute("assetSets", facade_asset.listAssetSets());

        return PAGE_FILE;
    }
}
