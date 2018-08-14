package org.memehazard.wheel.tutoring.facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.memehazard.wheel.asset.dao.AssetDAO;
import org.memehazard.wheel.asset.model.Asset;
import org.memehazard.wheel.core.persist.neo4j.Neo4jUtilities;
import org.memehazard.wheel.query.dao.QueryDAO;
import org.memehazard.wheel.query.facade.QueryDispatchFacade;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.model.Relationship;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.tutoring.dao.BayesDAO;
import org.memehazard.wheel.tutoring.dao.CurriculumDAO;
import org.memehazard.wheel.tutoring.dao.CurriculumItemDAO;
import org.memehazard.wheel.tutoring.dao.EnrolmentDAO;
import org.memehazard.wheel.tutoring.dao.EntityKnowledgeDAO;
import org.memehazard.wheel.tutoring.dao.EventDAO;
import org.memehazard.wheel.tutoring.dao.LabelFactDAO;
import org.memehazard.wheel.tutoring.dao.LabelKnowledgeDAO;
import org.memehazard.wheel.tutoring.dao.RelationFactDAO;
import org.memehazard.wheel.tutoring.dao.RelationKnowledgeDAO;
import org.memehazard.wheel.tutoring.dao.StudentDAO;
import org.memehazard.wheel.tutoring.model.BayesValue;
import org.memehazard.wheel.tutoring.model.Curriculum;
import org.memehazard.wheel.tutoring.model.CurriculumItem;
import org.memehazard.wheel.tutoring.model.DomainModelNode;
import org.memehazard.wheel.tutoring.model.Enrolment;
import org.memehazard.wheel.tutoring.model.EntityKnowledge;
import org.memehazard.wheel.tutoring.model.Event;
import org.memehazard.wheel.tutoring.model.LabelFact;
import org.memehazard.wheel.tutoring.model.LabelKnowledge;
import org.memehazard.wheel.tutoring.model.RelationFact;
import org.memehazard.wheel.tutoring.model.RelationKnowledge;
import org.memehazard.wheel.tutoring.utils.TutoringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Performs actions on the tutoring model.
 * 
 * Open issues:
 * - When a orphaned hanging EKs are deleted, no care is taken to ensure no facts exists between pairs of hanging EKs.
 * It should be impossible for
 * this to occur, but it's worth noting.
 * - SUGG - Method for locating completely orphaned nodes (that is, nodes that cannot trace any link to a curriculum.
 * Nodes of this sort should not
 * occur if everything's working properly, but their presence if they can be detected is a good canary indicated a
 * problem in some of the algorithms
 * in this facade.
 * 
 * 
 * @author xorgnz
 */
@Component
public class TutoringFacade
{
    public static final float    P_DEFAULT_CURRICULUM      = 0.20f;
    public static final float    P_DEFAULT_CURRICULUM_ITEM = 0.20f;
    private static final float   P_DEFAULT_EK              = 0.20f;
    private static final float   P_DEFAULT_LFACT           = 0.20f;
    private static final float   P_DEFAULT_LK              = 0.20f;
    private static final float   P_DEFAULT_RFACT           = 0.20f;
    private static final float   P_DEFAULT_RK              = 0.20f;
    @Autowired
    public QueryDispatchFacade   facade_queryDispatch;
    @Autowired
    private AssetDAO             dao_asset;
    @Autowired
    private BayesDAO             dao_bayes;
    @Autowired
    private CurriculumDAO        dao_c;
    @Autowired
    private CurriculumItemDAO    dao_ci;
    @Autowired
    private EntityKnowledgeDAO   dao_ek;
    @Autowired
    private EnrolmentDAO         dao_enrolment;
    @Autowired
    private EventDAO             dao_event;
    @Autowired
    private LabelFactDAO         dao_lFact;
    @Autowired
    private LabelKnowledgeDAO    dao_lk;
    @Autowired
    private QueryDAO             dao_query;
    @Autowired
    private RelationFactDAO      dao_rFact;
    @Autowired
    private RelationKnowledgeDAO dao_rk;
    @Autowired
    private StudentDAO           dao_student;
    private Logger               log                       = LoggerFactory.getLogger(this.getClass());
    @Autowired
    Neo4jUtilities               utils;


    /**
     * Create a new node in the domain model representing a curriculum.
     * 
     * @param curriculum
     * @return the curriculum passed, now with an assigned node ID
     */
    public Curriculum addCurriculum(Curriculum curriculum)
    {
        curriculum = dao_c.add(curriculum);

        return curriculum;
    }


    /**
     * Create a new node in the domain model representing a curriculum item.
     * 
     * @param ci
     * @return the curriculum item passed, now with an assigned node ID
     */
    public CurriculumItem addCurriculumItem(CurriculumItem ci)
    {
        ci = dao_ci.add(ci);

        return ci;
    }


    /**
     * Construct a new entity knowledge (EK) chunk around a given FMA ID for a given curriculum item
     * - If an existing EK chunk exists, use it.
     * - If a hanging EK chunk exists, hook it to the given curriculum item.
     * 
     * Call rebuld of facts that extend from the given FMA entity.
     * 
     * @param ciId ID of the curriculum item to create an EK chunk on
     * @param fmaId ID of the FMA entity create a knowledge chunk about
     * @param fmaLabel Label of the FMA entity
     * @return the created entity knowledge chunk
     */
    public EntityKnowledge addEntityKnowledge(long ciId, int fmaId, String fmaLabel)
            throws ParserException, IOException
    {
        log.trace(".addEntityKnowledge");

        // Look for pre-existing EntityKnowledge
        EntityKnowledge ek = dao_ek.findLinkedByCurriculumItemAndEntity(ciId, fmaId);
        if (ek == null)
        {
            // Look for hanging EntityKnowledge
            ek = dao_ek.findHangingByCurriculumItemAndEntity(ciId, fmaId);

            // Obtain necessary curriculum item
            CurriculumItem ci = dao_ci.get(ciId);
            if (ci == null)
                throw new IllegalArgumentException("Cannot add EntityKnowledge to curriculum with ID " + ciId + ". Does not exists.");

            // Hook hanging EntityKnowledge to CI
            if (ek != null)
            {
                ek.setCurriculumItem(ci);
                dao_ek.update(ek);
            }

            // Create new EntityKnowledge
            else
            {
                ek = new EntityKnowledge(fmaId, fmaLabel, ci);
                dao_ek.add(ek);
            }

            // Trigger rebuild of facts extending from this entity
            rebuildEntityKnowledge(ek);

            // Create Bayes values for all students enrolled in this EK's CI.
            List<Long> enrolledStudents = dao_enrolment.listStudentIdsByCurriculum(ek.getCurriculumItem().getCurriculum().getNodeId());
            if (enrolledStudents.size() > 0)
                dao_bayes.setForMultipleStudents(ek.getNodeId(), enrolledStudents, P_DEFAULT_EK, new Date());
        }

        return ek;
    }


    public void addEntityKnowledgeFromAssets(long ciId, int assetSetId, int[] assetIds)
            throws ParserException, IOException
    {
        log.trace(".addEntityKnowledgeFromAssets " + ciId + ", " + assetIds);

        List<Asset> assets = dao_asset.listByAssetSet(assetSetId);
        Map<Integer, Asset> assetMap = new HashMap<Integer, Asset>();
        for (Asset a : assets)
            assetMap.put(a.getId(), a);

        for (int id : assetIds)
        {
            Asset a = assetMap.get(id);
            if (a.getEntityId() > 0)
                addEntityKnowledge(ciId, a.getEntityId(), a.getName());
        }
    }


    /**
     * Generate <code>EntityKnowledge</code> from the results of a given query using given parameters. Add this entity
     * knowledge
     * to the given curriculum item.
     */
    public void addEntityKnowledgeFromQueryResults(long ciId, int queryId, Map<String, String> params)
            throws ParserException, IOException
    {
        log.trace(".addEntityKnowledgeFromQueryResults " + ciId + ", " + queryId + ", " + params.toString());

        // Execute query
        Query q = dao_query.get(queryId);
        List<Entity> entities = facade_queryDispatch.retrieveEntitiesByQuery(q, params);

        // Add new entity knowledge
        for (Entity e : entities)
        {
            addEntityKnowledge(ciId, e.getId(), e.getName());
        }
    }


    public void addLabelFactUpdateEvent(long studentId, long factId, String source, String description, double p)
    {
        LabelFact f = dao_lFact.get(factId);
        EntityKnowledge ek = f.getEntityKnowledge();
        CurriculumItem ci = ek.getCurriculumItem();
        Curriculum c = ci.getCurriculum();
        long curriculumId = c.getNodeId();

        Event e = new Event(curriculumId, studentId, source, description);
        e.setDeltaP(p);
        e.setRelation("fma:label");
        e.setSubjectEntityId(ek.getFmaId());
        dao_event.add(e);
    }


    /**
     * Construct a new label knowledge (LK) node for a given curriculum item and return it
     * - If an existing LK node exists, return it instead.
     * 
     * Call rebuld of label facts associated with LK node's CI.
     * 
     * @param ciId ID of the curriculum item to create an LK node
     * @return the created label knowledge chunk
     */
    public LabelKnowledge addLabelKnowledge(Long ciId)
    {
        log.trace(".addLabelKnowledge");

        // Look for pre-existing LabelKnowledge
        LabelKnowledge lk = dao_lk.findByCurriculumItem(ciId);

        // If pre-existing LK does not exist, create a new one.
        if (lk == null)
        {
            // Obtain necessary curriculum item
            CurriculumItem ci = dao_ci.get(ciId);
            if (ci == null)
                throw new IllegalArgumentException("Cannot add RelationKnowledge to curriculum with ID " + ciId + ". Does not exists.");

            // Create and assign LK node
            lk = new LabelKnowledge(ci);
            lk = dao_lk.add(lk);

            // Trigger rebuild of facts using this LK node
            rebuildLabelKnowledge(lk);

            // Create Bayes values for all students enrolled in this LK's CI.
            List<Long> enrolledStudents = dao_enrolment.listStudentIdsByCurriculum(lk.getCurriculumItem().getCurriculum().getNodeId());
            if (enrolledStudents.size() > 0)
                dao_bayes.setForMultipleStudents(lk.getNodeId(), enrolledStudents, P_DEFAULT_EK, new Date());
        }

        return lk;
    }


    public void addRelationFactUpdateEvent(long studentId, long factId, String source, String description, double p)
    {
        RelationFact f = dao_rFact.get(factId);
        RelationKnowledge rk = f.getRelation();
        EntityKnowledge ek_subj = f.getSubject();
        EntityKnowledge ek_obj = f.getSubject();
        CurriculumItem ci = rk.getCurriculumItem();
        Curriculum c = ci.getCurriculum();
        long curriculumId = c.getNodeId();

        Event e = new Event(curriculumId, studentId, source, description);
        e.setDeltaP(p);
        e.setRelation(rk.toRelationString());
        e.setObjectEntityId(ek_obj.getFmaId());
        e.setSubjectEntityId(ek_subj.getFmaId());
        dao_event.add(e);
    }


    /**
     * Construct a new relation knowledge (RK) chunk around a given FMA relation for a given curriculum item
     * - If an existing RK chunk exists, use it.
     * 
     * Call rebuld of facts that use the given FMA relation.
     * 
     * @param ciId ID of the curriculum item to create an EK chunk on
     * @param name Name of the FMA relation to create a knowledge chunk about
     * @param namespace Namespace of the FMA relation to create knowledge chunk about
     * @return the created relation knowledge chunk
     */
    public RelationKnowledge addRelationKnowledge(Long ciId, String name, String namespace)
            throws ParserException, IOException
    {
        log.trace(".addRelationKnowledge");

        // Look for pre-existing RelationKnowledge
        RelationKnowledge rk = dao_rk.find(ciId, name, namespace);

        // If pre-existing RK chunk does not exist, create a new one.
        if (rk == null)
        {
            // Obtain necessary curriculum item
            CurriculumItem ci = dao_ci.get(ciId);
            if (ci == null)
                throw new IllegalArgumentException("Cannot add RelationKnowledge to curriculum with ID " + ciId + ". Does not exists.");

            // Create and assign RK chunk
            rk = new RelationKnowledge(name, namespace, ci);
            rk = dao_rk.add(rk);

            // Trigger rebuild of facts using this relation
            rebuildRelationKnowledge(rk);

            // Create Bayes values for all students enrolled in this RK's CI.
            List<Long> enrolledStudents = dao_enrolment.listStudentIdsByCurriculum(rk.getCurriculumItem().getCurriculum().getNodeId());
            if (enrolledStudents.size() > 0)
                dao_bayes.setForMultipleStudents(rk.getNodeId(), enrolledStudents, P_DEFAULT_EK, new Date());
        }

        return rk;
    }


    public Curriculum buildFullDomainModel(long curriculumId)
    {
        Curriculum c = dao_c.get(curriculumId);
        c.setCurriculumItems(dao_ci.listByCurriculum(curriculumId));
        for (CurriculumItem ci : c.getCurriculumItems())
        {
            // Attach knowledge nodes to CI
            ci.setRelationKnowledge(dao_rk.listByCurriculumItem(ci.getNodeId()));
            ci.setEntityKnowledge(dao_ek.listLinkedByCurriculumItem(ci.getNodeId()));
            ci.setLabelKnowledge(dao_lk.findByCurriculumItem(ci.getNodeId()));

            // Prep - Attach fact nodes to knowledge nodes
            Map<Long, EntityKnowledge> ekMap = TutoringUtils.mapifyDomainModelNodes(ci.getEntityKnowledge());
            Map<Long, RelationKnowledge> rkMap = TutoringUtils.mapifyDomainModelNodes(ci.getRelationKnowledge());

            // Attach relation fact nodes to knowledge nodes
            for (RelationFact rf : dao_rFact.listByCurriculumItem(ci.getNodeId()))
            {
                // Attach fact to subject entity node
                if (rf.getSubject().isLinked())
                    ekMap.get(rf.getSubject().getNodeId()).addRelationFact(rf);

                // Attach fact to relation
                rkMap.get(rf.getRelation().getNodeId()).addRelationFact(rf);

                // Attach fact to object entity node
                if (rf.getObject().isLinked())
                    ekMap.get(rf.getObject().getNodeId()).addRelationFact(rf);
            }

            // Attach label fact nodes to knowledge nodes
            for (LabelFact lf : dao_lFact.listByCurriculumItem(ci.getNodeId()))
            {
                ekMap.get(lf.getEntityKnowledge().getNodeId()).setLabelFact(lf);
                ci.getLabelKnowledge().addLabelFact(lf);
            }
        }

        return c;
    }


    /**
     * Retrieve a slice of the domain model including all nodes that are required to update the model in
     * response to changes in a Label Fact.
     * 
     * This involves creating a modified Markov blanket of the LabelFact node and its descendants. Modifications
     * include:
     * - Listing parent RelationFacts for all RelationKnowledge in the Markov Blanket. These are required for
     * calculation of the RK
     * - Exclude EntityKnowledge that does not descend from the LF. It is not considered a parent of the CI for
     * purposes of calculation, and thus is not part of the Markov blanket
     * 
     * @param lf
     * @return
     */
    public Curriculum buildPartialDomainModel(LabelFact lf)
    {
        log.trace(".buildPartialDomainModel(LabelFact)");

        // Retreive descendant nodes of given LF
        EntityKnowledge ek = lf.getEntityKnowledge();
        LabelKnowledge lk = lf.getLabelKnowledge();
        CurriculumItem ci = lk.getCurriculumItem();
        Curriculum c = ci.getCurriculum();

        // Populate C
        c.setCurriculumItems(dao_ci.listByCurriculum(c.getNodeId()));

        // Populate CI
        ci = c.findCurriculumItem(ci.getNodeId());
        ci.setEntityKnowledge(dao_ek.listHangingByCurriculumItem(ci.getNodeId()));
        ci.setEntityKnowledge(dao_ek.listLinkedByCurriculumItem(ci.getNodeId()));
        ci.setLabelKnowledge(dao_lk.findByCurriculumItem(ci.getNodeId()));
        ci.setRelationKnowledge(dao_rk.listByCurriculumItem(ci.getNodeId()));

        // Populate EK
        ek = ci.findEntityKnowledge(ek.getNodeId());
        ek.setRelationFacts(dao_rFact.listByAnyEntity(ek.getNodeId()));
        ek.setLabelFact(lf);

        // Populate all RKs (all RKs are required for P calculation
        for (RelationKnowledge rk : ci.getRelationKnowledge())
            rk.setRelationFacts(dao_rFact.listByRelation(rk.getNodeId()));

        // Populate LK
        ci.getLabelKnowledge().setLabelFacts(dao_lFact.listByLabelKnowledge(lk.getNodeId()));

        return c;
    }


    /**
     * Retrieve a slice of the domain model including all nodes that are required to update the model in
     * response to changes in a Relation Fact.
     * 
     * This involves creating a modified Markov blanket of the Relation Fact node and its descendants. Modifications
     * include:
     * - Inclusion of RFs associated with all RKs. These are required for calculation of the RK
     * - Inclusion of LFs associated with the LK associated with the CI in the descendant chain. These are required for
     * calculation of the LK
     * - Exclude EntityKnowledge that does not descend from the RF. It is not considered a parent of the CI for
     * purposes of calculation, and thus is not part of the Markov blanket
     * 
     * @param lf
     * @return
     */
    public Curriculum buildPartialDomainModel(RelationFact rf)
    {
        log.trace(".buildPartialDomainModel(RelationFact)");

        // Retrieve descendant nodes of given RF
        EntityKnowledge ek_subj = rf.getSubject();
        EntityKnowledge ek_obj = rf.getObject();
        RelationKnowledge rk = rf.getRelation();
        CurriculumItem ci = rk.getCurriculumItem();
        Curriculum c = ci.getCurriculum();

        // Populate C
        c.setCurriculumItems(dao_ci.listByCurriculum(c.getNodeId()));

        // Populate CI
        ci = c.findCurriculumItem(ci.getNodeId());
        ci.setEntityKnowledge(dao_ek.listHangingByCurriculumItem(ci.getNodeId()));
        ci.setEntityKnowledge(dao_ek.listLinkedByCurriculumItem(ci.getNodeId()));
        ci.setLabelKnowledge(dao_lk.findByCurriculumItem(ci.getNodeId()));
        ci.setRelationKnowledge(dao_rk.listByCurriculumItem(ci.getNodeId()));

        // Populate EK subject (if attached)
        if (ek_subj.getCurriculumItem() != null)
        {
            ek_subj = ci.findEntityKnowledge(ek_subj.getNodeId());
            ek_subj.setRelationFacts(dao_rFact.listByAnyEntity(ek_subj.getNodeId()));
            ek_subj.setLabelFact(dao_lFact.findByEntityKnowledge(ek_subj.getNodeId()));
        }

        // Populate EK subject (if attached)
        if (ek_obj.getCurriculumItem() != null)
        {
            ek_obj = ci.findEntityKnowledge(ek_obj.getNodeId());
            ek_obj.setRelationFacts(dao_rFact.listByAnyEntity(ek_obj.getNodeId()));
            ek_obj.setLabelFact(dao_lFact.findByEntityKnowledge(ek_obj.getNodeId()));
        }

        // Populate all RKs (all RKs are required for P calculation
        for (RelationKnowledge rk0 : ci.getRelationKnowledge())
            rk0.setRelationFacts(dao_rFact.listByRelation(rk0.getNodeId()));

        // Populate LK
        LabelKnowledge lk = ci.getLabelKnowledge();
        lk.setLabelFacts(dao_lFact.listByLabelKnowledge(lk.getNodeId()));

        return c;
    }


    public void computePartialStudentModel(long studentId, LabelFact lf)
    {
        log.trace(".computePartialStudentModel(long, LabelFact)");

        // Build partial domain model containing Markov blanket of LF and its descendants
        Curriculum c = buildPartialDomainModel(lf);
        List<DomainModelNode> allNodes = c.getAllDomainNodes();
        Map<Long, DomainModelNode> allNodes_map = TutoringUtils.mapifyDomainModelNodes(allNodes);

        // Load Bayes Values
        List<BayesValue> bvs = listBayesValues(allNodes, studentId);
        Map<Long, BayesValue> bvMap = TutoringUtils.mapifyBayesValuesByDomainId(bvs);

        // Update Bayes Values
        allNodes_map.get(lf.getLabelKnowledge().getNodeId()).computeBayesValue(bvMap);
        allNodes_map.get(lf.getEntityKnowledge().getNodeId()).computeBayesValue(bvMap);
        allNodes_map.get(lf.getLabelKnowledge().getCurriculumItem().getNodeId()).computeBayesValue(bvMap);
        allNodes_map.get(lf.getLabelKnowledge().getCurriculumItem().getCurriculum().getNodeId()).computeBayesValue(bvMap);

        // Save Bayes Values
        List<BayesValue> toUpdate = new ArrayList<BayesValue>();
        toUpdate.add(bvMap.get(lf.getNodeId()));
        toUpdate.add(bvMap.get(lf.getEntityKnowledge().getNodeId()));
        toUpdate.add(bvMap.get(lf.getLabelKnowledge().getNodeId()));
        toUpdate.add(bvMap.get(lf.getLabelKnowledge().getCurriculumItem().getNodeId()));
        toUpdate.add(bvMap.get(lf.getLabelKnowledge().getCurriculumItem().getCurriculum().getNodeId()));
        dao_bayes.setMultiple(new ArrayList<BayesValue>(toUpdate));
    }


    public void computePartialStudentModel(long studentId, RelationFact rf)
    {
        log.trace(".computePartialStudentModel(long, RelationFact)");

        // Build partial domain model containing Markov blanket of RF and its descendants
        Curriculum c = buildPartialDomainModel(rf);
        List<DomainModelNode> allNodes = c.getAllDomainNodes();
        Map<Long, DomainModelNode> allNodes_map = TutoringUtils.mapifyDomainModelNodes(allNodes);

        // Load Bayes Values
        List<BayesValue> bvs = listBayesValues(allNodes, studentId);
        Map<Long, BayesValue> bvMap = TutoringUtils.mapifyBayesValuesByDomainId(bvs);

        List<Long> keys = new ArrayList<Long>(bvMap.keySet());
        Collections.sort(keys);
        for (Long key : keys)
            System.err.println(key + " -> " + bvMap.get(key));

        // Update Bayes Values
        allNodes_map.get(rf.getRelation().getNodeId()).computeBayesValue(bvMap);
        if (rf.getObject().getCurriculumItem() != null)
            allNodes_map.get(rf.getObject().getNodeId()).computeBayesValue(bvMap);
        if (rf.getSubject().getCurriculumItem() != null)
            allNodes_map.get(rf.getSubject().getNodeId()).computeBayesValue(bvMap);
        allNodes_map.get(rf.getRelation().getCurriculumItem().getNodeId()).computeBayesValue(bvMap);
        allNodes_map.get(rf.getRelation().getCurriculumItem().getCurriculum().getNodeId()).computeBayesValue(bvMap);

        // Save Bayes Values
        List<BayesValue> toUpdate = new ArrayList<BayesValue>();
        toUpdate.add(bvMap.get(rf.getNodeId()));
        if (rf.getObject().getCurriculumItem() != null)
            toUpdate.add(bvMap.get(rf.getObject().getNodeId()));
        if (rf.getSubject().getCurriculumItem() != null)
            toUpdate.add(bvMap.get(rf.getSubject().getNodeId()));
        toUpdate.add(bvMap.get(rf.getRelation().getNodeId()));
        toUpdate.add(bvMap.get(rf.getRelation().getCurriculumItem().getNodeId()));
        toUpdate.add(bvMap.get(rf.getRelation().getCurriculumItem().getCurriculum().getNodeId()));
        dao_bayes.setMultiple(new ArrayList<BayesValue>(toUpdate));
    }


    public void computeStudentModel(long studentId, long curriculumId)
    {
        // Build student model network
        Curriculum c = buildFullDomainModel(curriculumId);
        List<DomainModelNode> allNodes = c.getAllDomainNodes();

        // Load Bayes Values
        List<BayesValue> bvs = listBayesValues(allNodes, studentId);
        Map<Long, BayesValue> bvMap = TutoringUtils.mapifyBayesValuesByDomainId(bvs);

        // Update Bayes Values
        for (CurriculumItem ci : c.getCurriculumItems())
        {
            for (EntityKnowledge ek : ci.getEntityKnowledge())
                ek.computeBayesValue(bvMap);

            for (RelationKnowledge rk : ci.getRelationKnowledge())
                rk.computeBayesValue(bvMap);

            if (ci.getLabelKnowledge() != null)
                ci.getLabelKnowledge().computeBayesValue(bvMap);

            ci.computeBayesValue(bvMap);
        }
        c.computeBayesValue(bvMap);

        // Save Bayes Values
        dao_bayes.setMultiple(new ArrayList<BayesValue>(bvMap.values()));
    }


    /**
     * Delete a curriculum from the domain model.
     * - Deletes all associated curriculum items before deleting curriculum itself.
     * - Note that calls to this method may result in a large number of database calls, and may thus take some time to
     * complete.
     * 
     * @param c Curriculum to delete
     */
    public void deleteCurriculum(Curriculum c)
    {
        log.trace(".deleteCurriculum");

        // Delete all curriculum items
        List<CurriculumItem> curriculumItems = dao_ci.listByCurriculum(c.getNodeId());
        for (CurriculumItem ci : curriculumItems)
            deleteCurriculumItem(ci);

        // Delete curriculum
        dao_c.delete(c);

        // Delete all associated Bayes values
        dao_bayes.deleteAllByDomainId(c.getNodeId());
    }


    /**
     * Delete a curriculum item from the domain model.
     * - Deletes all associated entity knowledge and then all relation knowledge before deleting the curriculum item
     * itself.
     * 
     * @param ci Curriculum item to delete
     */
    public void deleteCurriculumItem(CurriculumItem ci)
    {
        log.trace(".deleteCurriculumItem");

        // Delete associated entity knowledge
        for (EntityKnowledge ek : dao_ek.listLinkedByCurriculumItem(ci.getNodeId()))
            deleteEntityKnowledge(ek);

        // Delete associated relation knowledge
        for (RelationKnowledge rk : dao_rk.listByCurriculumItem(ci.getNodeId()))
            deleteRelationKnowledge(rk);

        LabelKnowledge lk = dao_lk.findByCurriculumItem(ci.getNodeId());
        if (lk != null)
            deleteLabelKnowledge(lk);

        // Delete curriculum item
        dao_ci.delete(ci);

        // Delete all associated Bayes values
        dao_bayes.deleteAllByDomainId(ci.getNodeId());
    }


    /**
     * Attempts deletion of an entity knowledge chunk from the domain model
     * - Deletes associated facts
     * - Deletes any hanging EK chunks now orphaned
     * - Does not necessarily delete given EK chunk. If it is necessaary to keep another EK consistent, it is instead
     * unhooked.
     * 
     * @param ek
     */
    public void deleteEntityKnowledge(EntityKnowledge ek)
    {
        log.trace(".deleteEntityKnowledge");

        // Create data structures
        Set<EntityKnowledge> hangingEKs = new TreeSet<EntityKnowledge>();
        Set<RelationFact> factsToDelete = new TreeSet<RelationFact>();

        // Detach EK from curriculum item
        ek.setCurriculumItem(null);
        dao_ek.update(ek);

        // Add EK to hanging EKs (for potential clean up later if orphaned)
        hangingEKs.add(ek);

        // Sort through facts.
        // - Collect set of hanging EKs at other end of Facts
        // - Collect set of facts for deletion
        for (RelationFact f : dao_rFact.listByAnyEntity(ek.getNodeId()))
        {
            if (ek.equals(f.getObject()) && f.getSubject().getCurriculumItem() == null)
            {
                hangingEKs.add(f.getSubject());
                factsToDelete.add(f);
            }
            else if (ek.equals(f.getSubject()) && f.getObject().getCurriculumItem() == null)
            {
                hangingEKs.add(f.getObject());
                factsToDelete.add(f);
            }
        }

        // Delete facts attached to this EK chunk
        if (factsToDelete.size() > 0)
        {
            dao_rFact.deleteMultiple(factsToDelete);
            dao_bayes.deleteAllByMultipleDomainModelNodes(factsToDelete.toArray(new DomainModelNode[0]));
        }

        // Delete Bayes values associated with any hanging EKs
        if (hangingEKs.size() > 0)
            dao_bayes.deleteAllByMultipleDomainModelNodes(hangingEKs.toArray(new DomainModelNode[0]));

        // Delete label facts associated with any hanging EKs
        for (EntityKnowledge hangingEK : hangingEKs)
        {
            LabelFact lf = dao_lFact.findByEntityKnowledge(hangingEK.getNodeId());
            if (lf != null)
            {
                dao_lFact.delete(lf);
                dao_bayes.deleteAllByDomainId(lf.getNodeId());
            }
        }

        // Delete orphaned EKs and associated bayes values
        List<EntityKnowledge> orphanedEKs = dao_ek.findOrphanedInList(hangingEKs);
        if (orphanedEKs.size() > 0)
        {
            dao_ek.deleteMultiple(orphanedEKs);
        }
    }


    public void deleteLabelKnowledge(LabelKnowledge lk)
    {
        log.trace(".deleteLabelKnowledge");

        // Delete label facts first
        Collection<LabelFact> labelFacts = dao_lFact.listByLabelKnowledge(lk.getNodeId());
        if (labelFacts.size() > 0)
        {
            dao_lFact.deleteMultiple(labelFacts);
            dao_bayes.deleteAllByMultipleDomainModelNodes(labelFacts.toArray(new DomainModelNode[0]));
        }

        // Delete Label Knowledge
        dao_lk.delete(lk);
        dao_bayes.deleteAllByDomainId(lk.getNodeId());
    }


    /**
     * Attempts deletion of a relation knowledge chunk from the domain model
     * - Deletes associated facts
     * - Deletes any hanging EK chunks now orphaned
     * 
     * @param rk
     */
    public void deleteRelationKnowledge(RelationKnowledge rk)
    {
        log.trace(".deleteRelationKnowledge");

        // Get list of facts
        List<RelationFact> facts = dao_rFact.listByRelation(rk.getNodeId());

        // Sort through facts
        // - If fact is attached to a hanging EK, mark for possible deletion
        Set<EntityKnowledge> hangingEKs = new HashSet<EntityKnowledge>();
        for (RelationFact f : facts)
        {
            if (f.getObject().getCurriculumItem() == null)
                hangingEKs.add(f.getObject());
            if (f.getSubject().getCurriculumItem() == null)
                hangingEKs.add(f.getSubject());
        }

        // Delete facts attached to this RK chunk
        if (facts.size() > 0)
        {
            dao_rFact.deleteMultiple(facts);
            dao_bayes.deleteAllByMultipleDomainModelNodes(facts.toArray(new DomainModelNode[0]));
        }

        // Determine which hanging EKs are orphaned
        List<EntityKnowledge> orphanedEKs = dao_ek.findOrphanedInList(hangingEKs);

        // Delete orphaned EKs and associated bayes values
        if (orphanedEKs.size() > 0)
        {
            dao_ek.deleteMultiple(orphanedEKs);
            dao_bayes.deleteAllByMultipleDomainModelNodes(orphanedEKs.toArray(new DomainModelNode[0]));
        }

        // Delete relation
        dao_rk.delete(rk);
        dao_bayes.deleteAllByDomainId(rk.getNodeId());
    }


    public void enrolStudentInCurriculum(long studentId, long curriculumId)
    {
        log.trace(".enrolStudentInCurriculum");

        // Clear old enrolment
        unenrolStudentFromCurriculum(studentId, curriculumId);

        // Prepare list of BVs to add
        List<BayesValue> bvals = new ArrayList<BayesValue>();

        // Create BV for curriculum node
        Curriculum c = getCurriculum(curriculumId);
        bvals.add(new BayesValue(c.getNodeId(), studentId, P_DEFAULT_CURRICULUM, new Date(0)));

        // Create BVs for each curriculum item node
        for (CurriculumItem ci : dao_ci.listByCurriculum(curriculumId))
        {
            // Create BV for curriculum item
            bvals.add(new BayesValue(ci.getNodeId(), studentId, P_DEFAULT_CURRICULUM_ITEM, new Date(0)));

            // Create BV for LabelKnowledge
            LabelKnowledge lk = dao_lk.findByCurriculumItem(ci.getNodeId());
            if (lk != null)
            {
                bvals.add(new BayesValue(lk.getNodeId(), studentId, P_DEFAULT_LK, new Date(0)));
                for (LabelFact lf : dao_lFact.listByLabelKnowledge(lk.getNodeId()))
                    bvals.add(new BayesValue(lf.getNodeId(), studentId, P_DEFAULT_LFACT, new Date(0)));
            }

            // Create BVs for each entity knowledge node
            for (EntityKnowledge ek : dao_ek.listLinkedByCurriculumItem(ci.getNodeId()))
            {
                bvals.add(new BayesValue(ek.getNodeId(), studentId, P_DEFAULT_EK, new Date(0)));
            }

            // Create BVs for each relation knowledge node
            for (RelationKnowledge rk : dao_rk.listByCurriculumItem(ci.getNodeId()))
                bvals.add(new BayesValue(rk.getNodeId(), studentId, P_DEFAULT_RK, new Date(0)));

            // Create BVs for all facts
            for (RelationFact f : dao_rFact.listByCurriculumItem(ci.getNodeId()))
            {
                bvals.add(new BayesValue(f.getNodeId(), studentId, P_DEFAULT_RFACT, new Date(0)));
            }
        }

        // Add BVs to database.
        dao_bayes.setMultiple(bvals);

        // Add enrolment to database
        dao_enrolment.add(new Enrolment(curriculumId, studentId));

        // COmpute network
        this.computeStudentModel(studentId, curriculumId);
    }


    public Curriculum getCurriculum(Long id)
    {
        return dao_c.get(id);
    }


    public CurriculumItem getCurriculumItem(Long id)
    {
        return dao_ci.get(id);
    }


    public EntityKnowledge getEntityKnowledge(Long id)
    {
        return dao_ek.get(id);
    }


    public LabelKnowledge getLabelKnowledge(Long nodeId)
    {
        return dao_lk.get(nodeId);
    }


    public LabelKnowledge getLabelKnowledgeByCurriculumItem(Long ciId)
    {
        return dao_lk.findByCurriculumItem(ciId);
    }


    public RelationKnowledge getRelationKnowledge(Long id)
    {
        return dao_rk.get(id);
    }


    public boolean isEnrolled(long curriculumId, long studentId)
    {
        return dao_enrolment.isEnrolled(curriculumId, studentId);
    }


    public List<BayesValue> listBayesValues(List<? extends DomainModelNode> nodes, long studentId)
    {
        return dao_bayes.listByMultipleDomainModelNodes(nodes.toArray(new DomainModelNode[0]), studentId);
    }


    public List<Curriculum> listCurricula()
    {
        return dao_c.listAll();
    }


    public List<CurriculumItem> listCurriculumItemsByCurriculum(long id)
    {
        return dao_ci.listByCurriculum(id);
    }


    public List<Enrolment> listEnrolmentsByCurriculum(long curriculumId)
    {
        return dao_enrolment.listByCurriculum(curriculumId);
    }


    public List<Enrolment> listEnrolmentsByStudent(long studentId)
    {
        return dao_enrolment.listByStudent(studentId);
    }


    public List<EntityKnowledge> listEntityKnowledgeByCurriculumItem(long id)
    {
        return dao_ek.listLinkedByCurriculumItem(id);
    }


    public List<Event> listEvents(long curriculumId, long studentId)
    {
        return dao_event.listByCurriculumAndStudent(curriculumId, studentId);
    }


    public List<RelationFact> listRelationFactsByEntityKnowledge(Long id)
    {
        return dao_rFact.listByAnyEntity(id);
    }


    public List<RelationFact> listRelationFactsByRelationKnowledge(long id)
    {
        return dao_rFact.listByRelation(id);
    }


    public List<RelationKnowledge> listRelationKnowledgeByCurriculumItem(Long id)
    {
        return dao_rk.listByCurriculumItem(id);
    }


    public Object listStudents()
    {
        return dao_student.listAll();
    }


    public void unenrolStudentFromCurriculum(long studentId, long curriculumId)
    {
        List<DomainModelNode> nodes = new ArrayList<DomainModelNode>();

        Curriculum c = dao_c.get(curriculumId);
        nodes.add(c);

        for (CurriculumItem ci : dao_ci.listByCurriculum(c.getNodeId()))
        {
            nodes.add(ci);
            nodes.add(ci.getLabelKnowledge());

            for (EntityKnowledge ek : dao_ek.listLinkedByCurriculumItem(ci.getNodeId()))
                nodes.add(ek);
            for (EntityKnowledge ek : dao_ek.listHangingByCurriculumItem(ci.getNodeId()))
                nodes.add(ek);

            for (RelationKnowledge ek : dao_rk.listByCurriculumItem(ci.getNodeId()))
                nodes.add(ek);

            for (RelationFact rf : dao_rFact.listByCurriculumItem(ci.getNodeId()))
                nodes.add(rf);

            LabelKnowledge lk = dao_lk.findByCurriculumItem(ci.getNodeId());
            if (lk != null)
                nodes.add(lk);

            for (LabelFact lf : dao_lFact.listByCurriculumItem(ci.getNodeId()))
                nodes.add(lf);
        }

        dao_bayes.deleteByMultipleDomainModelNodes(nodes.toArray(new DomainModelNode[0]), studentId);

        // Remove enrolment to database
        dao_enrolment.delete(curriculumId, studentId);

        // Remove all events
        dao_event.deleteByCurriculumAndStudent(curriculumId, studentId);
    }


    public void updateCurriculum(Curriculum curriculum)
    {
        dao_c.update(curriculum);
    }


    public void updateCurriculumItem(CurriculumItem curriculumItem)
    {
        dao_ci.update(curriculumItem);
    }


    // TODO - Go through controllers / methods that update the domain model. Mark all enrolments dirty for that domain
    // model when changes are made.
    // Do this in the add / delete methods not in internal rebuild methods.


    public double updateLabelFactEstimate(long factId, long studentId, boolean value)
    {
        LabelFact lf = dao_lFact.get(factId);
        BayesValue bv = dao_bayes.get(factId, studentId);

        log.debug("Updating label fact " + lf + " with BV " + bv);

        if (bv != null)
        {
            double p_before = bv.getP();

            // Update Fact P
            lf.updateBoolean(bv, value);
            dao_bayes.set(bv.getDomainId(), bv.getStudentId(), bv.getP(), bv.getTimestamp());

            // Update student model
            this.computePartialStudentModel(studentId, lf);

            return bv.getP() - p_before;
        }
        else
            return 0;
    }


    public double updateRelationFactEstimate(long factId, long studentId, boolean value)
    {
        RelationFact rf = dao_rFact.get(factId);
        BayesValue bv = dao_bayes.get(factId, studentId);

        log.debug("Updating relation fact " + rf + " with BV " + bv);

        if (bv != null)
        {
            double p_before = bv.getP();

            // Update Fact P
            rf.updateBoolean(bv, value);
            dao_bayes.set(bv.getDomainId(), bv.getStudentId(), bv.getP(), bv.getTimestamp());

            // Update student model
            this.computePartialStudentModel(studentId, rf);

            return bv.getP() - p_before;
        }
        else
            return 0;
    }


    /**
     * Reconstruct the domain model around a given EK knowledge chunk
     * - 0) Grab current extended domain model for given EK's curriculum item
     * - 1) Retrieve a list of associated facts (relationships) from the FMA.
     * - 2) Filter facts to remove those with relations not used in EK's associated curriculum item.
     * - 3) Create facts for each filtered relationship. Create hanging EKs as necessary.
     * - 4) Compare list of new facts with list of extant facts to determine which must be added and deleted from model
     * 
     * @param ek
     * @throws ParserException If unable to parse FMA response
     * @throws IOException If an unexpected problem occurs in data transfer
     */
    private void rebuildEntityKnowledge(EntityKnowledge ek)
            throws ParserException, IOException
    {
        log.trace(".rebuildEntityKnowledge");

        // PREP - Retrieve EK nodes for associated CI
        List<EntityKnowledge> allEKs = new ArrayList<EntityKnowledge>();
        List<EntityKnowledge> linkedEKs = dao_ek.listLinkedByCurriculumItem(ek.getCurriculumItem().getNodeId());
        List<EntityKnowledge> hangingEKs = dao_ek.listHangingByCurriculumItem(ek.getCurriculumItem().getNodeId());
        allEKs.addAll(linkedEKs);
        allEKs.addAll(hangingEKs);
        Map<Integer, EntityKnowledge> allEKs_map = TutoringUtils.mapifyEntityKnowledge(allEKs);

        // PREP - Retrieve LK node for associated CI
        LabelKnowledge lk = dao_lk.findByCurriculumItem(ek.getCurriculumItem().getNodeId());

        // PREP - Retrieve RK nodes for associated CI
        List<RelationKnowledge> rkList = dao_rk.listByCurriculumItem(ek.getCurriculumItem().getNodeId());
        Map<String, RelationKnowledge> rkMap = TutoringUtils.mapifyRelationKnowledge(rkList);

        // PREP - Retrieve students enrolled in this curriculum
        List<Long> enrolledStudents = dao_enrolment.listStudentIdsByCurriculum(ek.getCurriculumItem().getCurriculum().getNodeId());

        // PREP - Retrieve facts for given EK
        List<RelationFact> extantRFacts = dao_rFact.listByAnyEntity(ek.getNodeId());
        log.trace("Extant Facts: ");
        for (RelationFact f : extantRFacts)
            log.trace(f.toString());
        LabelFact extantLFact = dao_lFact.findByEntityKnowledge(ek.getNodeId());

        // RFACTS - Retrieve relationships from FMA and filter to those that apply to this EK's CI
        List<Relationship> relationships = facade_queryDispatch.retrieveRelationshipsByEntity(ek.getFmaId());
        List<Relationship> filteredRelationships = new ArrayList<Relationship>();
        for (Relationship r : relationships)
        {
            for (RelationKnowledge rk : rkList)
            {
                log.trace(rk.toRelationString() + " -- " + r.getRelation().getRelation());
                if (rk.toRelationString().equals(r.getRelation().getRelation()))
                {
                    filteredRelationships.add(r);
                    break;
                }
            }
        }

        // RFACTS - Create facts from filtered relationships
        List<RelationFact> rFacts = new ArrayList<RelationFact>();
        for (Relationship r : filteredRelationships)
        {
            log.trace("Creating fact for " + r);

            EntityKnowledge subject = allEKs_map.get(r.getSubject().getId());
            RelationKnowledge relation = rkMap.get(r.getRelation().getRelation());
            EntityKnowledge object = allEKs_map.get(r.getObject().getId());

            // Create hanging EntityKnowledge if needed for subject
            if (subject == null)
            {
                subject = new EntityKnowledge(r.getSubject().getId(), r.getSubject().getName(), null);
                dao_ek.add(subject);
                allEKs_map.put(subject.getFmaId(), subject);
            }

            // Create hanging EntityKnowledge if needed for object
            if (object == null)
            {
                object = new EntityKnowledge(r.getObject().getId(), r.getObject().getName(), null);
                dao_ek.add(object);
                allEKs_map.put(object.getFmaId(), object);
            }

            rFacts.add(new RelationFact(subject, relation, object));
        }
        log.trace("Updated fact list:");
        for (RelationFact f : rFacts)
            log.trace(f.toString());

        // RFACTS - Store facts and create Bayes values if not already extant
        for (RelationFact f : rFacts)
        {
            if (!f.isRepresentedInList(extantRFacts))
            {
                log.trace("Adding fact " + f);
                f = dao_rFact.add(f);
                if (enrolledStudents.size() > 0)
                    dao_bayes.setForMultipleStudents(f.getNodeId(), enrolledStudents, P_DEFAULT_RFACT, new Date());
            }
        }

        // RFACTS - Delete extant facts not found in FMA from domain and student model
        for (RelationFact f : extantRFacts)
        {
            if (!f.isRepresentedInList(rFacts))
            {
                dao_rFact.delete(f);
                dao_bayes.deleteAllByDomainId(f.getNodeId());
                log.trace("Removing fact: " + f);
            }
        }

        // LFACTS - If no LFact extant and label knowledge required, create LFact and BayesValue
        if (lk != null && extantLFact == null)
        {
            LabelFact lf = new LabelFact(ek, lk);
            dao_lFact.add(lf);

            if (enrolledStudents.size() > 0)
                dao_bayes.setForMultipleStudents(lf.getNodeId(), enrolledStudents, P_DEFAULT_LFACT, new Date());
        }

        // LFACTS - If LFact extant and label knowledge not required, delete LFact and BayesValue
        if (lk == null && extantLFact != null)
        {
            dao_lFact.delete(extantLFact);
            dao_bayes.deleteAllByDomainId(extantLFact.getNodeId());
        }

        // CLEANUP - Delete orphaned EKs and associated bayes values
        List<EntityKnowledge> orphanedEKs = dao_ek.findOrphanedInList(hangingEKs);
        if (orphanedEKs.size() > 0)
        {
            dao_ek.deleteMultiple(orphanedEKs);
            dao_bayes.deleteAllByMultipleDomainModelNodes(orphanedEKs.toArray(new DomainModelNode[0]));
        }
    }


    private void rebuildLabelKnowledge(LabelKnowledge lk)
    {
        log.trace(".rebuildLabelKnowledge");

        // PREP - Retrieve linked entities for LK's CI
        Set<EntityKnowledge> linkedEKs = new TreeSet<EntityKnowledge>(dao_ek.listLinkedByCurriculumItem(lk.getCurriculumItem().getNodeId()));

        // PREP - Retrieve extant LFacts for LK
        List<LabelFact> extantLFacts = dao_lFact.listByLabelKnowledge(lk.getNodeId());
        Map<Long, LabelFact> extantLFacts_mapByEKId = TutoringUtils.mapifyLabelFactByEntityKnowledgeId(extantLFacts);

        // PREP - Retrieve students enrolled in this curriculum
        List<Long> enrolledStudents = dao_enrolment.listStudentIdsByCurriculum(lk.getCurriculumItem().getCurriculum().getNodeId());

        // Create LFs for all EKs
        for (EntityKnowledge ek : linkedEKs)
        {
            if (!extantLFacts_mapByEKId.containsKey(ek.getNodeId()))
            {
                LabelFact lf = new LabelFact(ek, lk);
                dao_lFact.add(lf);

                if (enrolledStudents.size() > 0)
                    dao_bayes.setForMultipleStudents(lf.getNodeId(), enrolledStudents, P_DEFAULT_LFACT, new Date());
            }
        }

        // Find LFs that rely on EKs that no longer exist
        List<LabelFact> lFactsToDelete = new ArrayList<LabelFact>();
        for (LabelFact lf : extantLFacts)
        {
            if (!linkedEKs.contains(lf.getEntityKnowledge()))
                lFactsToDelete.add(lf);
        }

        // Delete unnecessary LFacts
        if (lFactsToDelete.size() > 0)
        {
            dao_lFact.deleteMultiple(lFactsToDelete);
            dao_bayes.deleteAllByMultipleDomainModelNodes(lFactsToDelete.toArray(new LabelFact[0]));
        }
    }


    /**
     * Reconstruct the domain model around a given RK knowledge chunk
     * - 0) Grab list of attached and unattached EK chunks for given RK's curriculum item.
     * - 1) For each EK, retrieve all associated relationships from the FMA
     * - 2) Filter out relationships that do not use the given relation.
     * - 3) Create facts for each filtered relationship. Create hanging EKs as necessary.
     * - 4) Compare list of new facts with list of extant facts to determine which must be added and deleted from model
     * 
     * @param rk
     * @throws ParserException If unable to parse FMA response
     * @throws IOException If an unexpected problem occurs in data transfer
     */
    private void rebuildRelationKnowledge(RelationKnowledge rk)
            throws ParserException, IOException
    {
        log.trace(".rebuildRelationKnowledge " + rk);

        // Retrieve extant EK chunks
        List<EntityKnowledge> linkedEKs = dao_ek.listLinkedByCurriculumItem(rk.getCurriculumItem().getNodeId());
        List<EntityKnowledge> hangingEKs = dao_ek.listHangingByCurriculumItem(rk.getCurriculumItem().getNodeId());
        List<EntityKnowledge> allEKs = new ArrayList<EntityKnowledge>();
        allEKs.addAll(linkedEKs);
        allEKs.addAll(hangingEKs);
        Map<Integer, EntityKnowledge> allEKs_map = TutoringUtils.mapifyEntityKnowledge(allEKs);
        log.trace("Extant EKs: " + allEKs_map);

        // Retrieve facts
        List<RelationFact> extantFacts = dao_rFact.listByRelation(rk.getNodeId());
        log.trace("Extant Facts" + extantFacts.toString());

        // PERF - Write QI query that returns relationships of some type for a given set of FMAIDs
        // For each EK, retrieve all associated relationships from the FMA
        List<Relationship> relationships = new ArrayList<Relationship>();
        for (EntityKnowledge ek : linkedEKs)
        {
            relationships.addAll(facade_queryDispatch.retrieveRelationshipsByEntity(ek.getFmaId()));
        }
        log.trace("Relationships retrieved: " + relationships);

        // Filter out relationships that do not use the given relation.
        List<Relationship> filteredRelationships = new ArrayList<Relationship>();
        for (Relationship r : relationships)
        {
            if (rk.toRelationString().equals(r.getRelation().getRelation()))
                filteredRelationships.add(r);
        }
        log.trace("Filtered relationships " + filteredRelationships.toString());

        // Create facts from filtered relationships
        List<RelationFact> facts = new ArrayList<RelationFact>();
        for (Relationship r : filteredRelationships)
        {
            EntityKnowledge subject = allEKs_map.get(r.getSubject().getId());
            EntityKnowledge object = allEKs_map.get(r.getObject().getId());

            // Create hanging EntityKnowledge if needed for subject
            if (subject == null)
            {
                subject = new EntityKnowledge(r.getSubject().getId(), r.getSubject().getName(), null);
                dao_ek.add(subject);
                allEKs_map.put(subject.getFmaId(), subject);
            }

            // Create hanging EntityKnowledge if needed for object
            if (object == null)
            {
                object = new EntityKnowledge(r.getObject().getId(), r.getObject().getName(), null);
                dao_ek.add(object);
                allEKs_map.put(object.getFmaId(), object);
            }

            // Create new fact
            RelationFact f = new RelationFact(subject, rk, object);
            if (!f.isRepresentedInList(facts))
                facts.add(f);
        }

        for (Integer i : allEKs_map.keySet())
        {
            log.debug("Rebuild - EKs now present " + i + " -> " + allEKs_map.get(i));
        }

        // Add new facts and Bayes values
        List<Long> enrolledStudents = dao_enrolment.listStudentIdsByCurriculum(rk.getCurriculumItem().getCurriculum().getNodeId());
        for (RelationFact f : facts)
        {
            if (!f.isRepresentedInList(extantFacts))
            {
                log.trace("Adding fact " + f);
                dao_rFact.add(f);
                if (enrolledStudents.size() > 0)
                    dao_bayes.setForMultipleStudents(f.getNodeId(), enrolledStudents, 0.2, new Date());
            }
        }

        // Delete facts from domain and student model
        for (RelationFact f : extantFacts)
        {
            if (!f.isRepresentedInList(facts))
            {
                dao_rFact.delete(f);
                dao_bayes.deleteAllByDomainId(f.getNodeId());
                log.trace("Removing fact: " + f);
            }
        }

        // Delete orphaned EKs and associated bayes values
        List<EntityKnowledge> orphanedEKs = dao_ek.findOrphanedInList(hangingEKs);
        if (orphanedEKs.size() > 0)
        {
            dao_ek.deleteMultiple(orphanedEKs);
            dao_bayes.deleteAllByMultipleDomainModelNodes(orphanedEKs.toArray(new DomainModelNode[0]));
        }
    }
}
