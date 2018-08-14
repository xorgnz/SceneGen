package org.memehazard.wheel.asset.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.memehazard.wheel.asset.facade.AssetFacade;
import org.memehazard.wheel.asset.model.Asset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/asset")
public class ViewAssetSetAsXmlController
{
    @Autowired
    private AssetFacade facade;
    private Logger      log = LoggerFactory.getLogger(this.getClass());


    @RequestMapping("/set/{setId}/xml")
    @Transactional(readOnly = true)
    public String getSetAsXml(
            @PathVariable int setId,
            Model model,
            HttpServletRequest request)
    {
        log.trace("Generating page - " + request.getServletPath());

        // Act
        List<Asset> assets = facade.listAssetsBySet(setId);

        StringBuilder sb_urlPrefix = new StringBuilder();
        sb_urlPrefix.append(request.getScheme()+ "://");
        sb_urlPrefix.append(request.getServerName() + ":" + request.getServerPort());
        sb_urlPrefix.append(request.getContextPath());
        sb_urlPrefix.append("/asset/file/");

        // Respond
        model.addAttribute("url_prefix", sb_urlPrefix.toString());
        model.addAttribute("assets", assets);
        return "asset/set_xml";
    }
}
