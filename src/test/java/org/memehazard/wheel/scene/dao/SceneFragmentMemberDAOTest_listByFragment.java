package org.memehazard.wheel.scene.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.memehazard.wheel.scene.model.SceneFragmentMember;
import org.memehazard.wheel.scene.test.TestDAO_Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@Transactional
public class SceneFragmentMemberDAOTest_listByFragment
{
    @SuppressWarnings("unused")
    private Logger                 log             = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SceneFragmentMemberDAO dao;

    @Autowired
    private TestDAO_Scene          dao_test;

    private static final double[]   SCNFM_ALPHA     = new double[] { 7.0, 7.1, 7.2, 7.3, 7.4 };
    private static final String[]   SCNFM_AMBIENT   = new String[] { "#ff0000", "#00ff00", "#0000f0", "#000000" };
    private static final String[]   SCNFM_DIFFUSE   = new String[] { "#ff0001", "#00ff01", "#0000f1", "#000001" };
    private static final String[]   SCNFM_EMISSIVE  = new String[] { "#ff0002", "#00ff02", "#0000f2", "#000002" };
    private static final String[]   SCNFM_NAME      = new String[] { "Member Z", "Member Y", "Member X", "Member W" };
    private static final double[]   SCNFM_POS_X     = new double[] { 1.0, 1.1, 1.2, 1.3 };
    private static final double[]   SCNFM_POS_Y     = new double[] { 2.0, 2.1, 2.2, 2.3 };
    private static final double[]   SCNFM_POS_Z     = new double[] { 3.0, 3.1, 3.2, 3.3 };
    private static final double[]   SCNFM_ROT_X     = new double[] { 4.0, 4.1, 4.2, 4.3 };
    private static final double[]   SCNFM_ROT_Y     = new double[] { 5.0, 5.1, 5.2, 5.3 };
    private static final double[]   SCNFM_ROT_Z     = new double[] { 6.0, 6.1, 6.2, 6.3 };
    private static final int[]      SCNFM_SHININESS = new int[] { 0, 5, 10, 15 };
    private static final String[]   SCNFM_SPECULAR  = new String[] { "#ff0003", "#00ff03", "#0000f3", "#000003" };


    @Before
    public void before()
    {
        dao_test.prep_SceneFragmentMemberDAO_listByFragment();
    }


    @Test
    public void test_listByFragment()
    {
        // Act
        List<SceneFragmentMember> objs = dao.listByFragment(0);

        // Test - are all objects retrieved?
        Assert.assertEquals(3, objs.size());

        // TEST - Retrieved members have correct values in correct order
        SceneFragmentMember member0 = objs.get(0);
        Assert.assertEquals(SCNFM_NAME[2], member0.getEntity().getName());
        Assert.assertEquals(true, member0.isVisible());
        Assert.assertEquals(SCNFM_POS_X[2], member0.getTransform().getPositionX());
        Assert.assertEquals(SCNFM_POS_Y[2], member0.getTransform().getPositionY());
        Assert.assertEquals(SCNFM_POS_Z[2], member0.getTransform().getPositionZ());
        Assert.assertEquals(SCNFM_ROT_X[2], member0.getTransform().getRotationX());
        Assert.assertEquals(SCNFM_ROT_Y[2], member0.getTransform().getRotationY());
        Assert.assertEquals(SCNFM_ROT_Z[2], member0.getTransform().getRotationZ());
        Assert.assertEquals(SCNFM_ALPHA[2], member0.getStyle().getAlpha(), 3);
        Assert.assertEquals(SCNFM_AMBIENT[2], member0.getStyle().getAmbient());
        Assert.assertEquals(SCNFM_DIFFUSE[2], member0.getStyle().getDiffuse());
        Assert.assertEquals(SCNFM_EMISSIVE[2], member0.getStyle().getEmissive());
        Assert.assertEquals(SCNFM_SPECULAR[2], member0.getStyle().getSpecular());
        Assert.assertEquals(SCNFM_SHININESS[2], member0.getStyle().getShininess());

        SceneFragmentMember member1 = objs.get(2);
        Assert.assertEquals(SCNFM_NAME[0], member1.getEntity().getName());
        Assert.assertEquals(true, member1.isVisible());
        Assert.assertEquals(SCNFM_POS_X[0], member1.getTransform().getPositionX());
        Assert.assertEquals(SCNFM_POS_Y[0], member1.getTransform().getPositionY());
        Assert.assertEquals(SCNFM_POS_Z[0], member1.getTransform().getPositionZ());
        Assert.assertEquals(SCNFM_ROT_X[0], member1.getTransform().getRotationX());
        Assert.assertEquals(SCNFM_ROT_Y[0], member1.getTransform().getRotationY());
        Assert.assertEquals(SCNFM_ROT_Z[0], member1.getTransform().getRotationZ());
        Assert.assertEquals(SCNFM_ALPHA[0], member1.getStyle().getAlpha(), 3);
        Assert.assertEquals(SCNFM_AMBIENT[0], member1.getStyle().getAmbient());
        Assert.assertEquals(SCNFM_DIFFUSE[0], member1.getStyle().getDiffuse());
        Assert.assertEquals(SCNFM_EMISSIVE[0], member1.getStyle().getEmissive());
        Assert.assertEquals(SCNFM_SPECULAR[0], member1.getStyle().getSpecular());
        Assert.assertEquals(SCNFM_SHININESS[0], member1.getStyle().getShininess());
    }
}