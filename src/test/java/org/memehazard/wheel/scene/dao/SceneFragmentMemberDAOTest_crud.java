package org.memehazard.wheel.scene.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.asset.dao.AssetDAO;
import org.memehazard.wheel.asset.model.Asset;
import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.scene.model.SceneFragment;
import org.memehazard.wheel.scene.model.SceneFragmentMember;
import org.memehazard.wheel.scene.model.Transform;
import org.memehazard.wheel.scene.test.TestDAO_Scene;
import org.memehazard.wheel.style.model.Style;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class SceneFragmentMemberDAOTest_crud
{
    @SuppressWarnings("unused")
    private Logger    log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SceneFragmentMemberDAO dao;
    @Autowired
    private AssetDAO               dao_asset;
    @Autowired
    private SceneFragmentDAO       dao_scnf;

    @Autowired
    private TestDAO_Scene          dao_test;


    @Before
    public void before()
    {
        dao_test.prep_SceneFragmentMemberDAO_crud();
    }


    @Test
    public void test()
    {
        // PREP - Obtain depended objects
        SceneFragment fragment0 = dao_scnf.get(0);
        Asset asset0 = dao_asset.get(0);

        // ACT - Add
        Transform transform = new Transform(1.0, 1.1, 1.2, 1.3, 1.4, 1.5);
        Style style = new Style(0.5, "ambient", "diffuse", "emissive", "specular", 10);
        Entity entity0 = new Entity("", 5, "Entity Z");
        SceneFragmentMember member0 = new SceneFragmentMember(fragment0, false, entity0, asset0, transform, style);
        dao.add(member0);

        // Test - Did we get an object ID?
        Assert.assertNotNull(member0.getId());

        // ACT - Get
        SceneFragmentMember member0_copy = dao.get(member0.getId());

        // TEST - Retrieve object has correct values
        Assert.assertEquals(member0.getEntity().getName(), member0_copy.getEntity().getName());
        Assert.assertEquals(member0.isVisible(), member0_copy.isVisible());
        Assert.assertEquals(member0.getTransform().getPositionX(), member0_copy.getTransform().getPositionX());
        Assert.assertEquals(member0.getTransform().getPositionY(), member0_copy.getTransform().getPositionY());
        Assert.assertEquals(member0.getTransform().getPositionZ(), member0_copy.getTransform().getPositionZ());
        Assert.assertEquals(member0.getTransform().getRotationX(), member0_copy.getTransform().getRotationX());
        Assert.assertEquals(member0.getTransform().getRotationY(), member0_copy.getTransform().getRotationY());
        Assert.assertEquals(member0.getTransform().getRotationZ(), member0_copy.getTransform().getRotationZ());
        Assert.assertEquals(member0.getStyle().getAlpha(), member0_copy.getStyle().getAlpha(), 3);
        Assert.assertEquals(member0.getStyle().getAmbient(), member0_copy.getStyle().getAmbient());
        Assert.assertEquals(member0.getStyle().getDiffuse(), member0_copy.getStyle().getDiffuse());
        Assert.assertEquals(member0.getStyle().getEmissive(), member0_copy.getStyle().getEmissive());
        Assert.assertEquals(member0.getStyle().getSpecular(), member0_copy.getStyle().getSpecular());
        Assert.assertEquals(member0.getStyle().getShininess(), member0_copy.getStyle().getShininess());
        Assert.assertEquals(asset0.getId(), member0_copy.getAsset().getId());
        Assert.assertEquals(fragment0.getId(), member0_copy.getFragmentId());

        // ACT - List All
        Entity entity1 = new Entity("", 6, "Entity Y");
        SceneFragmentMember member1 = new SceneFragmentMember(fragment0, false, entity1, asset0, transform, style);
        dao.add(member1);
        List<SceneFragmentMember> members = dao.listAll();

        // Test - Did list all return member correctly?
        Assert.assertEquals(2, dao.listAll().size());
        Assert.assertEquals(member1.getEntity().getName(), members.get(0).getEntity().getName());
        Assert.assertEquals(member0.getEntity().getName(), members.get(1).getEntity().getName());

        // ACT - Update
        member0.getEntity().setName(member0.getEntity().getName() + "alt");
        member0.setVisible(!member0.isVisible());
        member0.getTransform().setPositionX(member0.getTransform().getPositionX());
        member0.getTransform().setPositionY(member0.getTransform().getPositionY());
        member0.getTransform().setPositionZ(member0.getTransform().getPositionZ());
        member0.getTransform().setRotationX(member0.getTransform().getRotationX());
        member0.getTransform().setRotationY(member0.getTransform().getRotationY());
        member0.getTransform().setRotationZ(member0.getTransform().getRotationZ());
        member0.getStyle().setAlpha(member0.getStyle().getAlpha() + 0.2);
        member0.getStyle().setAmbient(member0.getStyle().getAmbient() + "alt");
        member0.getStyle().setDiffuse(member0.getStyle().getDiffuse() + "alt");
        member0.getStyle().setEmissive(member0.getStyle().getEmissive() + "alt");
        member0.getStyle().setSpecular(member0.getStyle().getSpecular() + "alt");
        member0.getStyle().setShininess(member0.getStyle().getShininess() + 2);
        dao.update(member0);

        // Test - is it correct?
        member0_copy = dao.get(member0.getId());
        Assert.assertEquals(member0.getEntity().getName(), member0_copy.getEntity().getName());
        Assert.assertEquals(member0.isVisible(), member0_copy.isVisible());
        Assert.assertEquals(member0.getTransform().getPositionX(), member0_copy.getTransform().getPositionX());
        Assert.assertEquals(member0.getTransform().getPositionY(), member0_copy.getTransform().getPositionY());
        Assert.assertEquals(member0.getTransform().getPositionZ(), member0_copy.getTransform().getPositionZ());
        Assert.assertEquals(member0.getTransform().getRotationX(), member0_copy.getTransform().getRotationX());
        Assert.assertEquals(member0.getTransform().getRotationY(), member0_copy.getTransform().getRotationY());
        Assert.assertEquals(member0.getTransform().getRotationZ(), member0_copy.getTransform().getRotationZ());
        Assert.assertEquals(member0.getStyle().getAlpha(), member0_copy.getStyle().getAlpha(), 3);
        Assert.assertEquals(member0.getStyle().getAmbient(), member0_copy.getStyle().getAmbient());
        Assert.assertEquals(member0.getStyle().getDiffuse(), member0_copy.getStyle().getDiffuse());
        Assert.assertEquals(member0.getStyle().getEmissive(), member0_copy.getStyle().getEmissive());
        Assert.assertEquals(member0.getStyle().getSpecular(), member0_copy.getStyle().getSpecular());
        Assert.assertEquals(member0.getStyle().getShininess(), member0_copy.getStyle().getShininess());

        // ACT - Delete
        dao.delete(member1.getId());

        // TEST - Deleted
        Assert.assertEquals(1, dao.listAll().size());
    }
}
