-- This import script is intended for manual use when a fresh database needs to be populated with initial data.
--
-- It evolved from the import script used automatically on web-app deployment but now requires manual use as Hibernate only supports 
-- automatic import scripts when the database auto configuration is set to 'create', not 'update'. 'create' overwrites all DB content, which is 
-- undesirable. 

INSERT INTO "public"."stylesheet" (ssheet_id, ssheet_name) VALUES (1, 'Default styles');
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (1, 'Bone organ', '#808080', '#fff0d0', '#000000', '#ffffff', '50', '0', 1);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (2, 'Vein', '#000000', '#0000ff', '#000000', '#ffffff', '50', '2', 1);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (3, 'Artery', '#000000', '#ff0000', '#000000', '#ffffff', '50', '6', 1);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (4, 'Cartilage organ', '#c0e0ff', '#c0e0ff', '#000000', '#ffffff', '10', '7', 1);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (5, 'Muscle organ', '#400010', '#400010', '#400010', '#ffffff', '10', '3', 1);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (6, 'Head of muscle organ', '#400010', '#400010', '#400010', '#ffffff', '10', '4', 1);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (7, 'Parenchymatous organ', '#301008', '#301008', '#000000', '#ffffff', '10', '15', 1);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (8, 'Zone of muscle organ', '#600020', '#600020', '#000000', '#ffffff', '10', '12', 1);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (9, 'Zone of bone organ', '#808080', '#fff0d0', '#000000', '#ffffff', '10', '1', 1);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (10, 'Zone of muscle organ', '#400010', '#400010', '#400010', '#ffffff', '10', '5', 1);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (11, 'Subdivision of skeletal system', '#808080', '#fff0d0', '#000000', '#ffffff', '10', '4', 1);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (12, 'Segment of venous tree organ', '#303030', '#0000ff', '#000000', '#ffffff', '10', '7', 1);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (13, 'Segment of arterial tree organ', '#ff0000', '#ff0000', '#000000', '#ffffff', '10', '9', 1);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (14, 'Neural tree organ', '#8000a0', '#8000a0', '#000000', '#ffffff', '10', '13', 1);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (15, 'Nerve trunk', '#8000a0', '#8000a0', '#000000', '#ffffff', '10', '14', 1);

INSERT INTO "public"."stylesheet" (ssheet_id, ssheet_name) VALUES (2, 'Brain parts');                                                                                      
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (21, 'Segment of cerebellum', '#800000', '#800000', '#000000', '#000000', '128', '0', 2);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (22, 'Gyrus of temporal lobe', '#0000ff', '#0000ff', '#000000', '#ffffff', '10', '1', 2);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (23, 'Segment of gyrus of temporal lobe', '#0000ff', '#0000ff', '#000000', '#ffffff', '10', '2', 2);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (24, 'Gyrus of frontal lobe', '#0080ff', '#0080ff', '#000000', '#ffffff', '10', '3', 2);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (25, 'Segment of gyrus of frontal lobe', '#0080ff', '#0080ff', '#000000', '#ffffff', '10', '4', 2);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (26, 'Gyrus of parietal lobe', '#00ffff', '#00ffff', '#000000', '#ffffff', '10', '5', 2);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (27, 'Segment of gyrus of parietal lobe', '#00ffff', '#00ffff', '#000000', '#ffffff', '10', '6', 2);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (28, 'Gyrus of occipital lobe', '#00ff80', '#00ff80', '#202020', '#ffffff', '10', '7', 2);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (29, 'Subarachnoid sulcus', '#ffa0a0', '#ffa0a0', '#000000', '#ffffff', '10', '8', 2);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (30, 'Segment of telencephalon', '#ffff40', '#ffff40', '#000000', '#ffffff', '10', '9', 2);

INSERT INTO "public"."stylesheet" (ssheet_id, ssheet_name) VALUES (3, 'Bone types');
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (31, 'Flat bone', '#ffb040', '#ffb040', '#000000', '#ffffff', '50', '0', 3);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (32, 'Irregular bone', '#ff9000', '#ff9000', '#000000', '#ffffff', '50', '1', 3);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (33, 'Tooth', '#ffe0a0', '#ffe0a0', '#000000', '#ffffff', '10', '2', 3);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (34, 'Cartilage organ', '#B0E7D2', '#B0E7D2', '#000000', '#ffffff', '10', '3', 3);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (35, 'Long bone', '#ff4040', '#ff4040', '#000000', '#ffffff', '10', '4', 3);
INSERT INTO "public"."style" (style_id, style_tag, style_ambient, style_diffuse, style_emissive, style_specular, style_shininess, style_priority, style_ssheet_id) VALUES (36, 'Short bone', '#ff4090', '#ff4090', '#000000', '#ffffff', '10', '5', 3);

INSERT INTO "public"."query" (query_id, query_qid, query_name) VALUES ('6',  '294', 'Regional Parts (FMAID)');
INSERT INTO "public"."query_param" (queryp_variable, queryp_label, queryp_query_id) VALUES ('args', 'FMAID', 6);

INSERT INTO "public"."query" (query_id, query_qid, query_name) VALUES ('8',  '293', 'Regional Parts (Name)');
INSERT INTO "public"."query_param" (queryp_variable, queryp_label, queryp_query_id) VALUES ('args', 'FMA name', 8);

INSERT INTO "public"."query" (query_id, query_qid, query_name) VALUES ('10',  '295', 'Articulation (Name)');
INSERT INTO "public"."query_param" (queryp_variable, queryp_label, queryp_query_id) VALUES ('args', 'FMA name', 10);

INSERT INTO "public"."query" (query_id, query_qid, query_name) VALUES ('12',  '297', 'Articulation (FMAID)');
INSERT INTO "public"."query_param" (queryp_variable, queryp_label, queryp_query_id) VALUES ('args', 'FMAID', 12);

INSERT INTO "public"."query" (query_id, query_qid, query_name) VALUES ('14', '313', 'Members of a set (Name)');
INSERT INTO "public"."query_param" (queryp_variable, queryp_label, queryp_query_id) VALUES ('args', 'FMA name', 14);

INSERT INTO "public"."query" (query_id, query_qid, query_name) VALUES ('16', '315', 'Regional/Constutional parts (name)');
INSERT INTO "public"."query_param" (queryp_variable, queryp_label, queryp_query_id) VALUES ('args', 'FMA name', 16);


INSERT INTO usr (user_id, user_email, user_firstname, user_lastname, user_username, user_password) VALUES (1, 'xorgnz@uw.edu', 'Trond', 'Nilsen', 'xorgnz', 'abcd');
INSERT INTO usr (user_id, user_email, user_firstname, user_lastname, user_username, user_password) VALUES (2, 'brinkley@uw.edu', 'Jim', 'Brinkley', 'brinkley', 'abcd');
        