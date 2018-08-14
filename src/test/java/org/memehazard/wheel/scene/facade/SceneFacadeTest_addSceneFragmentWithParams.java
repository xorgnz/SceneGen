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
public class SceneFacadeTest_addSceneFragmentWithParams
{
    @Autowired
    private TestDAO_Scene          dao_test;

    @Autowired
    private SceneFragmentDAO       dao_scnf;
    @Autowired
    private SceneFragmentMemberDAO dao_scnfm;

    @Autowired
    private SceneFacade            facade;

    private static final Entity[]  SCNFM_ENTITY = new Entity[] {
                                                new Entity(null, 1, "Test Entity 1"),
                                                new Entity(null, 2, "Test Entity 2"),
                                                new Entity(null, 3, "Test Entity 3") };


    @Before
    public void before()
    {
        dao_test.prep_SceneFacade_addSceneFragmentWithParams();
    }


    @Test
    public void test_addSceneFragmentWithParams()
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
        SceneFragment fragment = facade.addSceneFragmentFromQueryParams(0, TestData.SCN_NAME[4], 123, 123, 123, params);

        // TODO - Test that assets are assigned to members correctly
        // TODO - Test that styles are assigned to members correctly

        // TEST - Fragment created correctly
        Assert.assertNotNull(fragment);
        Assert.assertNotNull(fragment.getId());

        // TEST - Created fragment has correct values
        Assert.assertEquals(TestData.SCN_NAME[4], fragment.getName());
        Assert.assertTrue("arg1=foo&arg2=bar".equals(fragment.getQueryParamString()) || "arg2=bar&arg1=foo".equals(fragment.getQueryParamString()));
        Assert.assertNotNull(fragment.getAssetSet());
        Assert.assertEquals(123, fragment.getAssetSet().getId());
        Assert.assertNotNull(fragment.getQuery());
        Assert.assertEquals(123, fragment.getQuery().getId());
        Assert.assertNotNull(fragment.getStylesheet());
        Assert.assertEquals(123, fragment.getStylesheet().getId());

        // TEST - Created fragment has correct members
        List<SceneFragmentMember> members = dao_scnfm.listByFragment(fragment.getId());
        Assert.assertEquals(3, members.size());
        Assert.assertTrue("SceneFragmentMember missing", existInList_SceneFragmentMember(fragment, SCNFM_ENTITY[0], members));
        Assert.assertTrue("SceneFragmentMember missing", existInList_SceneFragmentMember(fragment, SCNFM_ENTITY[1], members));
        Assert.assertTrue("SceneFragmentMember missing", existInList_SceneFragmentMember(fragment, SCNFM_ENTITY[2], members));

        // TEST - Retrieved fragment has correct members
        SceneFragment fragment_copy = dao_scnf.get(fragment.getId());
        Assert.assertEquals(TestData.SCN_NAME[4], fragment_copy.getName());
        Assert.assertTrue("arg1=foo&arg2=bar".equals(fragment_copy.getQueryParamString())
                          || "arg2=bar&arg1=foo".equals(fragment_copy.getQueryParamString()));
        Assert.assertNotNull(fragment_copy.getAssetSet());
        Assert.assertEquals(123, fragment_copy.getAssetSet().getId());
        Assert.assertNotNull(fragment_copy.getQuery());
        Assert.assertEquals(123, fragment_copy.getQuery().getId());
        Assert.assertNotNull(fragment_copy.getStylesheet());
        Assert.assertEquals(123, fragment_copy.getStylesheet().getId());

        // TEST - Retrieved fragment has correct members
        List<SceneFragmentMember> members_copy = dao_scnfm.listByFragment(fragment_copy.getId());
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
