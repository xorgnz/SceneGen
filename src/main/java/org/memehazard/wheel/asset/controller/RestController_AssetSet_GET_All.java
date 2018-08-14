package org.memehazard.wheel.asset.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.asset.facade.AssetFacade;
import org.memehazard.wheel.asset.model.AssetSet;
import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.query.parser.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/REST/asset/set/")
public class RestController_AssetSet_GET_All
{
    @Autowired
    private AssetFacade facade;
    private Logger      log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject performGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ParserException, IOException

    {
        log.trace("Handling REST request - " + request.getServletPath() + " - " + request.getMethod());

        // Act
        List<AssetSet> sets = facade.listAssetSets();

        return new RestResponseObject(true, "Success", sets);
    }


    public static class RestResponseObject extends BasicRestResponseObject
    {
        private List<AssetSet> sets = null;


        public RestResponseObject(boolean success, String message, List<AssetSet> sets)
        {
            super(success, message);
            this.sets = sets;
        }


        public List<AssetSet> getAssetSets()
        {
            return sets;
        }
    }
}
