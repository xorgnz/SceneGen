package org.memehazard.wheel.asset.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.asset.facade.AssetFacade;
import org.memehazard.wheel.asset.model.Asset;
import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.query.parser.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/REST/asset/set/{id}/assets")
public class RestController_Asset_GET_byAssetSet
{
    @Autowired
    private AssetFacade facade;
    private Logger      log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject performGet(
            @PathVariable int id,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ParserException, IOException

    {
        log.trace("Handling REST request - " + request.getServletPath() + " - " + request.getMethod());

        // Act
        List<Asset> assets = facade.listAssetsBySet(id);

        return new RestResponseObject(true, "Success", assets);
    }


    public static class RestResponseObject extends BasicRestResponseObject
    {
        private List<Asset> assets = null;


        public RestResponseObject(boolean success, String message, List<Asset> assets)
        {
            super(success, message);
            this.assets = assets;
        }


        public List<Asset> getAssets()
        {
            return assets;
        }
    }
}
