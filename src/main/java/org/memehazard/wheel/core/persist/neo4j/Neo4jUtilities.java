package org.memehazard.wheel.core.persist.neo4j;

import java.util.Map;

import javax.transaction.TransactionManager;

import org.apache.commons.lang.text.StrBuilder;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;

@Component
public class Neo4jUtilities
{
    @Autowired
    private Neo4jTemplate template;

    private Logger        log = LoggerFactory.getLogger(this.getClass());


    public Transaction beginTx()
    {
        return template.getGraphDatabaseService().beginTx();
    }


    public void clearDatabase()
    {
        log.debug("Clearing Neo4J database");

        // Verify if we're in the middle of a transaction
        if (template.transactionIsRunning())
            throw new InTransactionException("clearDatabase() cannot be called within transaction");

        // Delete all nodes and relationships
        Transaction tx1 = beginTx();
        template.query("START r=rel(*) DELETE r;", null);
        template.query("START n=node(*) DELETE n;", null);
        tx1.success();
        tx1.finish();

        // Delete all indices
        Transaction tx2 = beginTx();
        IndexManager idxMgr = template.getGraphDatabaseService().index();
        for (String indexName : idxMgr.nodeIndexNames())
        {
            log.trace("Deleting node index " + indexName);
            idxMgr.forNodes(indexName).delete();
        }
        for (String indexName : idxMgr.relationshipIndexNames())
        {
            log.trace("Deleting relationship index " + indexName);
            idxMgr.forNodes(indexName).delete();
        }
        tx2.success();
        tx2.finish();
    }


    public long countAllNodes()
    {
        if (template.transactionIsRunning())
            log.warn("countAllNodes called within prior transaction - may yield out of date result");

        Transaction tx = beginTx();
        try
        {
            EndResult<Map<String, Object>> result = template.query("START n=node(*) RETURN count(n);", null);
            for (Map<String, Object> m : result)
            {
                if (m.containsKey("count(n)"))
                    return (Long) m.get("count(n)");
            }
        }
        finally
        {
            tx.finish();
        }

        return 0;
    }


    public void finishRunningTransaction()
    {
        if (template.transactionIsRunning())
        {
            try
            {
                log.trace("Rolling back hanging transaction");
                TransactionManager tm = ((EmbeddedGraphDatabase) template.getGraphDatabaseService()).getTxManager();
                tm.rollback();
            }
            catch (Exception e)
            {
                log.error("Transaction could not be rolled back", e);
            }
        }
    }


    public Neo4jTemplate getTemplate()
    {
        return template;
    }


    public void dumpAllNodes()
    {
        if (template.transactionIsRunning())
            log.warn("countAllNodes called within prior transaction - may yield out of date result");

        log.trace("Dumping all nodes");

        Transaction tx = beginTx();
        try
        {
            EndResult<Map<String, Object>> result = template.query("START n=node(*) RETURN n;", null);
            for (Map<String, Object> m : result)
            {
                if (m.containsKey("n"))
                {
                    log.trace("\n" + nodeToString((Node) m.get("n")));
                }
            }
        }
        finally
        {
            tx.finish();
        }
    }


    private static String nodeToString(Node n)
    {
        StrBuilder sb = new StrBuilder();

        sb.appendln("Neo4j Node [" + n.getId() + "]");
        for (String key : n.getPropertyKeys())
            sb.appendln("  - " + key + " " + n.getProperty(key));

        for (Relationship r : n.getRelationships())
        {
            sb.appendln("  + " + r.getStartNode().getId() + " -" + r.getType() + "-> " + r.getEndNode().getId());
        }

        return sb.toString();
    }
}
