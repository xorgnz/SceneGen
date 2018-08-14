package org.memehazard.wheel.scene.facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.query.facade.AbstractMockQueryDispatchFacade;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.parser.ParserException;
import org.memehazard.wheel.scene.dao.SceneFragmentDAO;
import org.memehazard.wheel.scene.dao.SceneFragmentMemberDAO;
import org.memehazard.wheel.scene.model.SceneFragment;
import org.memehazard.wheel.scene.model.SceneFragmentMember;
import org.memehazard.wheel.scene.test.TestDAO_Scene;
import org.memehazard.wheel.scene.test.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class SceneFacadeTest_updateSceneFragmentWithParams
{
    @Autowired
    private SceneFragmentDAO       dao_scnf;
    @Autowired
    private SceneFragmentMemberDAO dao_scnfm;
    @Autowired
    private TestDAO_Scene          dao_test;
    @Autowired
    private SceneFacade            facade;
    private static final Entity[]  SCNFM_ENTITY = new Entity[] {
                                                new Entity("fma:testEntity1", 1, "Test Entity 1"),
                                                new Entity("fma:testEntity2", 2, "Test Entity 2"),
                                                new Entity("fma:testEntity3", 3, "Test Entity 3") };


    @Before
    public void before()
    {
        dao_test.prep_SceneFacade_updateSceneFragmentWithParams();
    }


    @Test
    public void test_updateSceneFragmentWithParams()
            throws IOException, ParserException
    {
        // PREP - Configure facade with mock QueryDispatchService
        ReflectionTestUtils.setField(facade, "facade_queryDispatch", new AbstractMockQueryDispatchFacade()
        {
            @Override
            public List<Entity> retrieveEntitiesByQuery(Query q, Map<String, String> params)
            {
                List<Entity> entities = new ArrayList<Entity>();
                entities.add(SCNFM_ENTITY[0]);
                entities.add(SCNFM_ENTITY[1]);
                entities.add(SCNFM_ENTITY[2]);
                return entities;
            }
        });

        // ACT - Create new Scene Fragment
        Map<String, String> params = new HashMap<String, String>();
        params.put("arg1", "foo");
        params.put("arg2", "bar");
        facade.updateSceneFragmentFromQueryParams(0, TestData.SCN_NAME[4], 124, 124, 124, params);

        // TODO - Test that assets are assigned to members correctly
        // TODO - Test that styles are assigned to members correctly

        // TEST - Retrieved fragment has correct members
        SceneFragment fragment = dao_scnf.get(0);
        Assert.assertEquals(TestData.SCN_NAME[4], fragment.getName());
        Assert.assertTrue("arg1=foo&arg2=bar".equals(fragment.getQueryParamString())
                          || "arg2=bar&arg1=foo".equals(fragment.getQueryParamString()));
        Assert.assertNotNull(fragment.getAssetSet());
        Assert.assertEquals(124, fragment.getAssetSet().getId());
        Assert.assertNotNull(fragment.getQuery());
        Assert.assertEquals(124, fragment.getQuery().getId());
        Assert.assertNotNull(fragment.getStylesheet());
        Assert.assertEquals(124, fragment.getStylesheet().getId());

        // TEST - Retrieved fragment has correct members
        List<SceneFragmentMember> members_copy = dao_scnfm.listByFragment(fragment.getId());
        Assert.assertEquals(3, members_copy.size());
        Assert.assertTrue("SceneFragmentMember missing", existInList_SceneFragmentMember(fragment, SCNFM_ENTITY[0], members_copy));
        Assert.assertTrue("SceneFragmentMember missing", existInList_SceneFragmentMember(fragment, SCNFM_ENTITY[1], members_copy));
        Assert.assertTrue("SceneFragmentMember missing", existInList_SceneFragmentMember(fragment, SCNFM_ENTITY[2], members_copy));
    }


    private boolean existInList_SceneFragmentMember(SceneFragment sf, Entity entity, List<SceneFragmentMember> members)
    {
        for (SceneFragmentMember scnfm : members)
            if (entity.getName().equals(scnfm.getEntity().getName()) &&
                entity.getId().equals(scnfm.getEntity().getId()) &&
                scnfm.getFragmentId() == sf.getId())
                return true;
        return false;
    }
}
