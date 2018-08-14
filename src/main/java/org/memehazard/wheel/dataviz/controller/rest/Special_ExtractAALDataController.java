package org.memehazard.wheel.dataviz.controller.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.asset.dao.AssetDAO;
import org.memehazard.wheel.asset.dao.AssetSetDAO;
import org.memehazard.wheel.asset.model.Asset;
import org.memehazard.wheel.asset.model.AssetSet;
import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.query.dao.QueryDAO;
import org.memehazard.wheel.query.facade.QueryDispatchFacade;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.parser.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/REST/visualizer/special/query188extract")
public class Special_ExtractAALDataController
{
    @Autowired
    public AssetDAO     dao_asset;
    @Autowired
    public AssetSetDAO  dao_assetSet;
    @Autowired
    public QueryDAO     dao_query;
    @Autowired
    QueryDispatchFacade facade_queryDispatch;


    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    BasicRestResponseObject performGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ParserException, IOException
    {
        AssetSet set_aal = dao_assetSet.getByName("AAL");
        List<Asset> assets = dao_asset.listByAssetSet(set_aal.getId());
        Query q = dao_query.get(122);

        Map<String, String> parameters = new HashMap<String, String>();
        List<Entity> entities = new ArrayList<Entity>();

        for (Asset a : assets)
        {
            parameters.put("args", "" + a.getEntityId());
            
            List<Entity> entities_qd = facade_queryDispatch.retrieveEntitiesByQuery(q, parameters);
            
            if (entities_qd.size() > 0)
            {
                Entity e = entities_qd.get(0);
                e.setName(a.getName());
                entities.add(e);
            }
        }
        
        System.err.println("<structures>");
        for (Entity e : entities)
        {
            System.err.println("  <structure>");
            System.err.println("    <fma:FMAID>" + e.getId() + "</fma:FMAID>");
            System.err.println("    <rdfs:label>" + e.getName() + "</rdfs:label>");
            System.err.println("    <healthy>" + e.getData().get("healthy") + "</healthy>");
            System.err.println("    <schizo>" + e.getData().get("schizo") + "</schizo>");
            System.err.println("  </structure>");
        }
        System.err.println("</structures>");


        return new BasicRestResponseObject(true, "success");
    }
}
