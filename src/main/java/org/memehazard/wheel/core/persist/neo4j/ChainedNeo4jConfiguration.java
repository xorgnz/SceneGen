package org.memehazard.wheel.core.persist.neo4j;

import org.apache.commons.dbcp.BasicDataSource;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.JtaTransactionManagerFactoryBean;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Neo4J configuration which replaces basic Neo4J transaction manager with ChainedTransactionManager. This allows Spring to handle transactions for 
 * both Neo4J and MyBatis using the same transaction manager, and allows the use of the normal Spring Transactional annotation.
 * 
 * If this is not used, and individual transaction managers (one for neo4j and one for mybatis) are used instead, it is difficult to handle situations
 * where resources from both sources must be used. Spring's automated transaction management is inadequate, meaning that manual transactions are
 * needed, which is a pain.
 * 
 * @author xorgnz
 */
@Configuration
@EnableTransactionManagement
public class ChainedNeo4jConfiguration extends Neo4jConfiguration
{

    @Autowired
    private BasicDataSource       dataSource;

    @Autowired
    private EmbeddedGraphDatabase embeddedGraphDatabase;


    @Override
    @Bean(name = "transactionManager")
    public PlatformTransactionManager neo4jTransactionManager() throws Exception
    {
        return new ChainedTransactionManager(
                new JtaTransactionManagerFactoryBean(embeddedGraphDatabase).getObject(),
                new DataSourceTransactionManager(dataSource));
    }
}
