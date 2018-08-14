package org.memehazard.wheel.tutoring.controller.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.memehazard.wheel.core.controller.BasicRestResponseObject;
import org.memehazard.wheel.tutoring.facade.TutoringFacade;
import org.memehazard.wheel.tutoring.model.EntityKnowledge;
import org.memehazard.wheel.tutoring.model.RelationFact;
import org.memehazard.wheel.tutoring.model.RelationKnowledge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/tutoring/REST/inspect")
public class RestInspectionDetailsController
{
    public final static int TYPE_CURRICULUM         = 0;
    public final static int TYPE_CURRICULUM_ITEM    = 1;
    public final static int TYPE_RELATION_KNOWLEDGE = 2;
    public final static int TYPE_ENTITY_KNOWLEDGE   = 3;
    public final static int TYPE_FACT               = 4;

    @Autowired
    private TutoringFacade  facade;
    private Logger          log                     = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Transactional(readOnly = true)
    public RestResponseObject request(
            @ModelAttribute CommandObject co,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        log.trace("Handling REST request - " + request.getServletPath());

        // Create list to hold response items
        List<ResponseItem> items = new ArrayList<ResponseItem>();

        // Handle request for children of a curriculum item
        if (co.getType() == TYPE_CURRICULUM_ITEM)
        {
            log.trace("Inspecting CI " + co.getId());

            List<EntityKnowledge> ekList = facade.listEntityKnowledgeByCurriculumItem(co.getId());
            for (EntityKnowledge ek : ekList)
                items.add(new ResponseItem(ek.getNodeId(), "Entity: " + ek.getFmaLabel() + " [" + ek.getFmaId() + "]", TYPE_ENTITY_KNOWLEDGE, 0.6f));

            List<RelationKnowledge> rkList = facade.listRelationKnowledgeByCurriculumItem(co.getId());
            for (RelationKnowledge rk : rkList)
                items.add(new ResponseItem(rk.getNodeId(), "Relation: " + rk.getNamespace() + ":" + rk.getName(), TYPE_RELATION_KNOWLEDGE, 0.6f));
        }

        // Handle request for children of an entity knowledge
        else if (co.getType() == TYPE_ENTITY_KNOWLEDGE)
        {
            log.trace("Inspecting EK " + co.getId());

            List<RelationFact> facts = facade.listRelationFactsByEntityKnowledge(co.getId());
            for (RelationFact f : facts)
            {
                String label = f.getSubject().getFmaLabel() + " -[" + f.getRelation().toRelationString() + "]-> " + f.getObject().getFmaLabel();
                items.add(new ResponseItem(f.getNodeId(), label, TYPE_FACT, 0.9f));
            }
        }

        // Handle request for children of a relation knowledge
        else if (co.getType() == TYPE_RELATION_KNOWLEDGE)
        {
            log.trace("Inspecting RK " + co.getId());

            List<RelationFact> facts = facade.listRelationFactsByRelationKnowledge(co.getId());
            for (RelationFact f : facts)
            {
                String label = f.getSubject().getFmaLabel() + " -[" + f.getRelation().toRelationString() + "]-> " + f.getObject().getFmaLabel();
                items.add(new ResponseItem(f.getNodeId(), label, TYPE_FACT, 0.9f));
            }
        }

        // Handle request for children of a fact
        // Throw error message
        else if (co.getType() == TYPE_FACT)
        {
            log.error("Inspecting Fact - disallowed " + co.getId());
        }

        // Handle request for children of unknown node
        else
        {
            log.error("Inspecting unknown object " + co.toString());
        }

        return new RestResponseObject(true, "", items);
    }


    public static class CommandObject
    {
        private long id;
        private int  type;


        public int getType()
        {
            return type;
        }


        public void setType(int type)
        {
            this.type = type;
        }


        public long getId()
        {
            return id;
        }


        public void setId(long id)
        {
            this.id = id;
        }


        @Override
        public String toString()
        {
            return "ID: " + id + ", type: " + type;
        }
    }


    class RestResponseObject extends BasicRestResponseObject
    {
        private List<ResponseItem> items = new ArrayList<ResponseItem>();


        public RestResponseObject(boolean success, String message, List<ResponseItem> items)
        {
            super(success, message);
            this.items = items;
        }


        public List<ResponseItem> getItems()
        {
            return items;
        }


        public void setItems(List<ResponseItem> items)
        {
            this.items = items;
        }
    }


    class ResponseItem
    {
        private long  id;
        public String name;
        public int    type;
        public float  p;


        public ResponseItem(long id, String name, int type, float p)
        {
            this.id = id;
            this.name = name;
            this.type = type;
            this.p = p;
        }


        public String getName()
        {
            return name;
        }


        public int getType()
        {
            return type;
        }


        public long getId()
        {
            return id;
        }


        public float getP()
        {
            return p;
        }
    }
}
