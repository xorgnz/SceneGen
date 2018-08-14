package org.memehazard.wheel.core.facade;

import java.util.HashSet;

import org.memehazard.wheel.query.dao.QueryDAO;
import org.memehazard.wheel.query.model.Query;
import org.memehazard.wheel.query.model.QueryParameter;
import org.memehazard.wheel.rbac.dao.PermissionDAO;
import org.memehazard.wheel.rbac.dao.RoleDAO;
import org.memehazard.wheel.rbac.dao.UserDAO;
import org.memehazard.wheel.rbac.dao.UserGroupDAO;
import org.memehazard.wheel.rbac.model.Permission;
import org.memehazard.wheel.rbac.model.Role;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.rbac.model.UserGroup;
import org.memehazard.wheel.style.dao.StyleDAO;
import org.memehazard.wheel.style.dao.StylesheetDAO;
import org.memehazard.wheel.style.model.Style;
import org.memehazard.wheel.style.model.Stylesheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminFacade
{

    @Autowired
    private PermissionDAO dao_permission;
    @Autowired
    private RoleDAO       dao_role;
    @Autowired
    private QueryDAO      dao_query;
    @Autowired
    private StyleDAO      dao_style;
    @Autowired
    private StylesheetDAO dao_ssheet;
    @Autowired
    private UserDAO       dao_user;
    @Autowired
    private UserGroupDAO  dao_usergrp;


    public void initializeDB()
    {
        // ////////////////////////////// RBAC objects

        // Create objects
        Permission p = new Permission("P1", "No description");
        Role r_admin = new Role("Admin", "administrator");
        User u_trond = new User("xorgnz", "n1ls3n", "xorgnz@gmail.com", "Trond", "Nilsen");
        User u_jim = new User("brinkley", "br1nkl3y", "brinkley@uw.edu", "Jim", "Brinkley");
        UserGroup ug_sig = new UserGroup("SIG", "Members of the Structural Informatics Group");

        // Add to DB
        dao_permission.add(p);
        dao_role.add(r_admin);
        dao_user.add(u_trond);
        dao_user.add(u_jim);
        dao_usergrp.add(ug_sig);

        // Create Assignments
        dao_permission.assignToRole(p.getId(), r_admin.getId());
        dao_role.assignToUser(r_admin.getId(), u_trond.getId());
        dao_role.assignToUser(r_admin.getId(), u_jim.getId());
        dao_user.assignToUserGroup(u_trond.getId(), ug_sig.getId());
        dao_user.assignToUserGroup(u_jim.getId(), ug_sig.getId());


        // ///////////////////////////// Queries
        HashSet<String> tags_entityId = new HashSet<String>();
        tags_entityId.add("entityId");
        HashSet<String> tags_entityName = new HashSet<String>();
        tags_entityName.add("entityName");


        // Create Queries
        Query q_regional_fmaid = new Query(
                "Regional Parts (FMAID)",
                "Retrieves entities that are regional parts of entity specified by FMAID.",
                294,
                "entities");
        q_regional_fmaid.addParameter(new QueryParameter("args", "FMAID", tags_entityId));
        dao_query.add(q_regional_fmaid);

        Query q_regional_name = new Query(
                "Regional Parts (FMA Label)",
                "Retrieves entities that are regional parts of entity specified by FMA name.",
                293,
                "entities");
        q_regional_name.addParameter(new QueryParameter("args", "FMA Entity Label", tags_entityName));
        dao_query.add(q_regional_name);

        Query q_artic_fmaid = new Query(
                "Articulation (FMAID)",
                "Select all entities that articulate with the entity with the given FMAID. Applies to bones.",
                297,
                "entities");
        q_artic_fmaid.addParameter(new QueryParameter("args", "FMAID", tags_entityId));
        dao_query.add(q_artic_fmaid);

        Query q_artic_name = new Query(
                "Articulation (FMA Label)",
                "Select all entities that articulate with the entity with the given name. Applies to bones.",
                295,
                "entities");
        q_artic_name.addParameter(new QueryParameter("args", "FMA Entity Label", tags_entityName));
        dao_query.add(q_artic_name);

        Query q_members_fmaid = new Query(
                "Members of a set (FMAID)",
                "Retrieves entities that are members of set specified by FMAID",
                415,
                "entities");
        q_members_fmaid.addParameter(new QueryParameter("args", "FMAID", tags_entityId));
        dao_query.add(q_members_fmaid);

        Query q_members_name = new Query(
                "Members of a set (FMA Label)",
                "Retrieves entities that are members of set specified by FMA name",
                414,
                "entities");
        q_members_name.addParameter(new QueryParameter("args", "FMA Entity Label", tags_entityName));
        dao_query.add(q_members_name);

        Query q_parts_fmaid = new Query(
                "Regional / Constitutional Parts (FMAID)",
                "Retrieves entities that are either regional or constitutional parts of entity specified by FMAID.",
                417,
                "entities");
        q_parts_fmaid.addParameter(new QueryParameter("args", "FMAID", tags_entityId));
        dao_query.add(q_parts_fmaid);

        Query q_parts_name = new Query(
                "Regional / Constitutional Parts (FMA Label)",
                "Retrieves entities that are either regional or constitutional parts of entity specified by FMA name.",
                416,
                "entities");
        q_parts_name.addParameter(new QueryParameter("args", "FMA Entity Label", tags_entityName));
        dao_query.add(q_parts_name);

        Query q_data_137 = new Query(
                "Data - Query 137 (single URSI)",
                "Retrieves Voxel Based Morphometry values for brain regions. " +
                        "<br/><br/>" +
                        "Requires Universal Research Subject Identifier​ (URSI). Valid values include M02101222, M02103447, M02104909, M02108122, " +
                        "M02108714, M02109398, M02110676, M02111314, M02114646, M02116465.",
                413,
                "data");
        q_data_137.addParameter(new QueryParameter("args", "URSI", new HashSet<String>()));
        dao_query.add(q_data_137);

        Query q_data_188 = new Query(
                "Data - Query 188",
                "Retrieves number of activations in each region for healthy and schizophrenic patients in BIRN study.",
                419,
                "data");
        q_data_188.addParameter(new QueryParameter("args", "FMAID", tags_entityId));
        dao_query.add(q_data_188);

        Query q_data_188_cached = new Query(
                "Data - Query 188 (pre-cached)",
                "Retrieves number of activations in each region for healthy and schizophrenic patients in BIRN study.<br/><br/>Results pre-cached for speed.",
                422,
                "data");


        dao_query.add(q_data_188_cached);

        // ///////////////////////////// Styles

        // Create stylesheets
        Stylesheet ssheet_bones = new Stylesheet("Bone Types", "Colors bones, teeth, and cartilage according to type", "");
        Stylesheet ssheet_brain = new Stylesheet("Brain Parts", "Colors entities according to which region of the brain they belong to", "");
        Stylesheet ssheet_default = new Stylesheet("Default Styles", "Classic anatomic styling; red arteries, blue veins, etc", "");

        // Add stylesheets to DB
        dao_ssheet.add(ssheet_bones);
        dao_ssheet.add(ssheet_brain);
        dao_ssheet.add(ssheet_default);

        // Add styles
        dao_style.add(new Style(ssheet_default, "Esophagus", 1, "#000000", "#ff8080", "#000000", "#ffffff", 10, 21));
        dao_style.add(new Style(ssheet_default, "Duodenum", 1, "#000000", "#ff8080", "#000000", "#ffffff", 10, 22));
        dao_style.add(new Style(ssheet_default, "Ileum", 1, "#000000", "#ff8080", "#000000", "#ffffff", 10, 23));
        dao_style.add(new Style(ssheet_default, "Bone organ", 1, "#808080", "#fff0d0", "#000000", "#ffffff", 50, 0));
        dao_style.add(new Style(ssheet_default, "Gallbladder", 1, "#000000", "#008800", "#000000", "#ffffff", 10, 25));
        dao_style.add(new Style(ssheet_default, "Segment of tracheobronchial tree", 1, "#000000", "#86d0f9", "#000000", "#ffffff", 10, 26));
        dao_style.add(new Style(ssheet_default, "Jejunum", 1, "#000000", "#ff8080", "#000000", "#ffffff", 10, 24));
        dao_style.add(new Style(ssheet_default, "Zone of bone organ", 1, "#808080", "#fff0d0", "#000000", "#ffffff", 50, 1));
        dao_style.add(new Style(ssheet_default, "Subdivision of skeletal system", 1, "#808080", "#fff0d0", "#000000", "#ffffff", 50, 2));
        dao_style.add(new Style(ssheet_default, "Tooth", 1, "#ffe0a0", "#ffe0a0", "#000000", "#ffffff", 10, 3));
        dao_style.add(new Style(ssheet_default, "Muscle organ", 1, "#400010", "#400010", "#400010", "#ffffff", 50, 4));
        dao_style.add(new Style(ssheet_default, "Head of muscle organ", 1, "#400010", "#400010", "#400010", "#ffffff", 50, 5));
        dao_style.add(new Style(ssheet_default, "Zone of muscle organ", 1, "#400010", "#400010", "#400010", "#ffffff", 50, 6));
        dao_style.add(new Style(ssheet_default, "Cartilage organ", 1, "#c0e0ff", "#c0e0ff", "#000000", "#ffffff", 50, 7));
        dao_style.add(new Style(ssheet_default, "Vein", 1, "#000000", "#0000ff", "#000000", "#ffffff", 50, 8));
        dao_style.add(new Style(ssheet_default, "Segment of venous tree organ", 1, "#303030", "#0000ff", "#000000", "#ffffff", 50, 9));
        dao_style.add(new Style(ssheet_default, "Artery", 1, "#000000", "#ff0000", "#000000", "#ffffff", 50, 12));
        dao_style.add(new Style(ssheet_default, "Segment of arterial tree organ", 1, "#000000", "#ff0000", "#000000", "#ffffff", 50, 13));
        dao_style.add(new Style(ssheet_default, "Neural tree organ", 1, "#804040", "#ffff80", "#000000", "#ffffff", 50, 16));
        dao_style.add(new Style(ssheet_default, "Nerve trunk", 1, "#804040", "#ffff80", "#000000", "#ffffff", 50, 17));
        dao_style.add(new Style(ssheet_default, "Parenchymatous organ", 1, "#301008", "#301008", "#000000", "#ffffff", 50, 18));
        dao_style.add(new Style(ssheet_default, "Venous trunk", 1, "#000000", "#0000ff", "#000000", "#ffffff", 10, 10));
        dao_style.add(new Style(ssheet_default, "Segment of venous trunk", 1, "#000000", "#0000ff", "#000000", "#ffffff", 10, 11));
        dao_style.add(new Style(ssheet_default, "Segment of arterial trunk", 1, "#000000", "#ff0000", "#000000", "#ffffff", 10, 14));
        dao_style.add(new Style(ssheet_default, "Arterial trunk", 1, "#000000", "#ff0000", "#000000", "#ffffff", 10, 15));
        dao_style.add(new Style(ssheet_default, "Lobe of lung", 1, "#8c8c00", "#ff80c0", "#000000", "#ffffff", 10, 19));
        dao_style.add(new Style(ssheet_default, "Stomach", 1, "#000000", "#fe818a", "#000000", "#ffffff", 10, 20));

        dao_style.add(new Style(ssheet_brain, "Segment of cerebellum", 1, "#800000", "#800000", "#000000", "#000000", 128, 1));
        dao_style.add(new Style(ssheet_brain, "Gyrus of temporal lobe", 1, "#0000ff", "#0000ff", "#000000", "#ffffff", 50, 0));
        dao_style.add(new Style(ssheet_brain, "Segment of gyrus of temporal lobe", 1, "#0000ff", "#0000ff", "#000000", "#ffffff", 50, 2));
        dao_style.add(new Style(ssheet_brain, "Gyrus of frontal lobe", 1, "#0080ff", "#0080ff", "#000000", "#ffffff", 50, 3));
        dao_style.add(new Style(ssheet_brain, "Segment of gyrus of frontal lobe", 1, "#0080ff", "#0080ff", "#000000", "#ffffff", 50, 4));
        dao_style.add(new Style(ssheet_brain, "Gyrus of parietal lobe", 1, "#00ffff", "#00ffff", "#000000", "#ffffff", 50, 7));
        dao_style.add(new Style(ssheet_brain, "Segment of gyrus of parietal lobe", 1, "#00ffff", "#00ffff", "#000000", "#ffffff", 50, 8));
        dao_style.add(new Style(ssheet_brain, "Gyrus of occipital lobe", 1, "#00ff80", "#00ff80", "#202020", "#ffffff", 50, 9));
        dao_style.add(new Style(ssheet_brain, "Subarachnoid sulcus", 1, "#ffa0a0", "#ffa0a0", "#000000", "#ffffff", 50, 10));
        dao_style.add(new Style(ssheet_brain, "Segment of telencephalon", 1, "#ffff40", "#ffff40", "#000000", "#ffffff", 50, 11));
        dao_style.add(new Style(ssheet_brain, "Gray matter of gyrus of frontal lobe", 1, "#0080ff", "#0080ff", "#000000", "#ffffff", 10, 5));
        dao_style.add(new Style(ssheet_brain, "Supplemental motor cortex", 1, "#0080ff", "#0080ff", "#000000", "#ffffff", 10, 6));
        dao_style.add(new Style(ssheet_brain, "Nucleus of brain", 1, "#cbe41b", "#93ea1e", "#000000", "#ffffff", 10, 12));
        dao_style.add(new Style(ssheet_brain, "Thalamus", 1, "#ff80ff", "#ff80c0", "#000000", "#ffffff", 10, 13));

        dao_style.add(new Style(ssheet_bones, "Flat bone", 1.0, "#ffb040", "#ffb040", "#000000", "#ffffff", 50, 0));
        dao_style.add(new Style(ssheet_bones, "Irregular bone", 1.0, "#ff9000", "#ff9000", "#000000", "#ffffff", 50, 1));
        dao_style.add(new Style(ssheet_bones, "Tooth", 1.0, "#ffe0a0", "#ffe0a0", "#000000", "#ffffff", 50, 2));
        dao_style.add(new Style(ssheet_bones, "Cartilage organ", 1.0, "#B0E7D2", "#B0E7D2", "#000000", "#ffffff", 50, 3));
        dao_style.add(new Style(ssheet_bones, "Long bone", 1.0, "#ff4040", "#ff4040", "#000000", "#ffffff", 50, 4));
        dao_style.add(new Style(ssheet_bones, "Short bone", 1.0, "#ff4090", "#ff4090", "#000000", "#ffffff", 50, 5));
    }
}