DROP TABLE IF EXISTS "public"."scene_fragment_member";
DROP TABLE IF EXISTS "public"."scene_fragment";
DROP TABLE IF EXISTS "public"."scene";
DROP TABLE IF EXISTS "public"."query_cache_line";
DROP TABLE IF EXISTS "public"."query_param";
DROP TABLE IF EXISTS "public"."query";
DROP TABLE IF EXISTS "public"."asset_styletag";
DROP TABLE IF EXISTS "public"."asset";
DROP TABLE IF EXISTS "public"."asset_set";
DROP TABLE IF EXISTS "public"."role_perm_link";
DROP TABLE IF EXISTS "public"."user_role_link";
DROP TABLE IF EXISTS "public"."usergrp_user_link";
DROP TABLE IF EXISTS "public"."permission";
DROP TABLE IF EXISTS "public"."role";
DROP TABLE IF EXISTS "public"."usergroup";
DROP TABLE IF EXISTS "public"."style";
DROP TABLE IF EXISTS "public"."stylesheet";
DROP TABLE IF EXISTS "public"."bayes";
DROP TABLE IF EXISTS "public"."its_student_model";
DROP TABLE IF EXISTS "public"."its_enrolment";
DROP TABLE IF EXISTS "public"."its_event";
DROP TABLE IF EXISTS "public"."demo";
DROP TABLE IF EXISTS "public"."usr";
DROP TABLE IF EXISTS "public"."text_completion";
DROP TABLE IF EXISTS "public"."activity_template";
DROP TABLE IF EXISTS "public"."activity_instance";
DROP TABLE IF EXISTS "public"."activity_template_param";
DROP TABLE IF EXISTS "public"."exercise";


-- Table definitions
CREATE TABLE "public"."usergroup"           
( 
    "usergrp_id" serial NOT NULL, 
    "usergrp_description" text NOT NULL, 
    "usergrp_name" varchar(255) NOT NULL, 
    CONSTRAINT "usergroup_pkey" PRIMARY KEY ("usergrp_id") 
) 
WITH (OIDS=FALSE);


-- Table named usr instead of user, as user is reserved word
CREATE TABLE "public"."usr"                 
(
    "user_id" serial NOT NULL, 
    "user_email" varchar(255) NOT NULL, 
    "user_firstname" varchar(255) NOT NULL, 
    "user_lastname" varchar(255) NOT NULL, 
    "user_password" varchar(255), 
    "user_username" varchar(255) NOT NULL, 
    CONSTRAINT "user_pkey" PRIMARY KEY ("user_id"), 
    CONSTRAINT "user_username_key" UNIQUE ("user_username"), 
    CONSTRAINT "user_email_key" UNIQUE ("user_email") 
) 
WITH (OIDS=FALSE);


CREATE TABLE "public"."role"                
(
    "role_id" serial NOT NULL, 
    "role_description" text NOT NULL, 
    "role_name" varchar(255) NOT NULL, 
    CONSTRAINT "role_pkey" PRIMARY KEY ("role_id"), 
    CONSTRAINT "role_name_key" UNIQUE ("role_name") 
) 
WITH (OIDS=FALSE);


CREATE TABLE "public"."permission"          
(
    "perm_id" serial NOT NULL, 
    "perm_description" text NOT NULL, 
    "perm_name" varchar(255) NOT NULL, 
    CONSTRAINT "permission_pkey" PRIMARY KEY ("perm_id"), 
    CONSTRAINT "perm_name_key" UNIQUE ("perm_name") 
) 
WITH (OIDS=FALSE);


CREATE TABLE "public"."role_perm_link"      
( 
    "role_perm_link_role_id" int4 NOT NULL, 
    "role_perm_link_perm_id" int4 NOT NULL, 
    CONSTRAINT "role_perm_link_pkey" PRIMARY KEY ("role_perm_link_role_id", "role_perm_link_perm_id"),
    CONSTRAINT "role_perm_link_perm_id_fkey" FOREIGN KEY ("role_perm_link_perm_id") REFERENCES "public"."permission" ("perm_id") ON DELETE CASCADE ON UPDATE NO ACTION, 
    CONSTRAINT "role_perm_link_role_id_fkey" FOREIGN KEY ("role_perm_link_role_id") REFERENCES "public"."role" ("role_id") ON DELETE CASCADE ON UPDATE NO ACTION 
) 
WITH (OIDS=FALSE);


CREATE TABLE "public"."user_role_link"      
( 
    "user_role_link_user_id" int4 NOT NULL, 
    "user_role_link_role_id" int4 NOT NULL, 
    CONSTRAINT "user_role_link_pkey" PRIMARY KEY ("user_role_link_user_id", "user_role_link_role_id"),
    CONSTRAINT "user_role_link_role_id_fkey" FOREIGN KEY ("user_role_link_role_id") REFERENCES "public"."role" ("role_id") ON DELETE CASCADE ON UPDATE NO ACTION, 
    CONSTRAINT "user_role_link_user_id_fkey" FOREIGN KEY ("user_role_link_user_id") REFERENCES "public"."usr" ("user_id") ON DELETE CASCADE ON UPDATE NO ACTION 
) 
WITH (OIDS=FALSE);


CREATE TABLE "public"."usergrp_user_link"   
( 
    "usergrp_user_link_usergrp_id" int4 NOT NULL, 
    "usergrp_user_link_user_id" int4 NOT NULL, 
    CONSTRAINT "usergrp_user_link_pkey" PRIMARY KEY ("usergrp_user_link_usergrp_id", "usergrp_user_link_user_id"),
    CONSTRAINT "usergrp_user_link_usergrp_id_fkey" FOREIGN KEY ("usergrp_user_link_usergrp_id") REFERENCES "public"."usergroup" ("usergrp_id") ON DELETE CASCADE ON UPDATE NO ACTION, 
    CONSTRAINT "usergrp_user_link_user_id_fkey" FOREIGN KEY ("usergrp_user_link_user_id") REFERENCES "public"."usr" ("user_id") ON DELETE CASCADE ON UPDATE NO ACTION 
) 
WITH (OIDS=FALSE);


CREATE TABLE "public"."asset_set"           
(
    "assetset_id" serial NOT NULL, 
    "assetset_maintainer" varchar(255) NOT NULL, 
    "assetset_name" varchar(255) NOT NULL, 
    CONSTRAINT "asset_set_pkey" PRIMARY KEY ("assetset_id") 
) 
WITH (OIDS=FALSE);


CREATE TABLE "public"."asset"               
(
    "asset_id" serial NOT NULL, 
    "asset_entity_id" int4 NOT NULL, 
    "asset_name" varchar(255) NOT NULL, 
    "asset_obj_filename" varchar(255), 
    "asset_centroid_x" float8, 
    "asset_centroid_y" float8, 
    "asset_centroid_z" float8, 
    "asset_max_x" float8, 
    "asset_max_y" float8, 
    "asset_max_z" float8, 
    "asset_min_x" float8, 
    "asset_min_y" float8, 
    "asset_min_z" float8, 
    "asset_x3d_filename" varchar(255), 
    "asset_assetset_id" int4, 
    CONSTRAINT "asset_pkey" PRIMARY KEY ("asset_id"), 
    CONSTRAINT "asset__assetset_id_fkey" FOREIGN KEY ("asset_assetset_id") REFERENCES "public"."asset_set" ("assetset_id") ON DELETE CASCADE ON UPDATE NO ACTION
) 
WITH (OIDS=FALSE);


CREATE TABLE "public"."asset_styletag"      
(
    "astag_asset_id" int4 NOT NULL, 
    "astag_tag" varchar(255), 
    CONSTRAINT "asset_styletag_pkey" PRIMARY KEY ("astag_asset_id", "astag_tag"), 
    CONSTRAINT "astag_asset_id_fkey" FOREIGN KEY ("astag_asset_id") REFERENCES "public"."asset" ("asset_id") ON DELETE CASCADE ON UPDATE NO ACTION
) 
WITH (OIDS=FALSE);
    
    
CREATE TABLE "public"."query"               
(
    "query_id" serial NOT NULL, 
    "query_name" varchar(255), 
    "query_qid" int4, 
    CONSTRAINT "query_pkey" PRIMARY KEY ("query_id") 
) 
WITH (OIDS=FALSE);


CREATE TABLE "public"."query_param"         
( 
    "queryp_label" varchar(255), 
    "queryp_variable" varchar(255) NOT NULL, 
    "queryp_query_id" int4 NOT NULL, 
    CONSTRAINT "query_param_pkey" PRIMARY KEY ("queryp_query_id", "queryp_variable"), 
    CONSTRAINT "fkc90fde56a54bce6e" FOREIGN KEY ("queryp_query_id") REFERENCES "public"."query" ("query_id") ON DELETE CASCADE ON UPDATE NO ACTION 
) 
WITH (OIDS=FALSE);


CREATE TABLE "public"."query_cache_line"    
(
    "querycl_id" serial NOT NULL, 
    "querycl_param_val_str" varchar(255), 
    "querycl_result" text, 
    "querycl_retrieved" timestamp(6), 
    "querycl_query_id" int4, 
    UNIQUE (querycl_param_val_str, querycl_query_id), 
    CONSTRAINT "query_cache_line_pkey" PRIMARY KEY ("querycl_id"), 
    CONSTRAINT "fkdb118a68a54bce6e" FOREIGN KEY ("querycl_query_id") REFERENCES "public"."query" ("query_id") ON DELETE CASCADE ON UPDATE NO ACTION
) 
WITH (OIDS=FALSE);


CREATE TABLE "public"."stylesheet" 
(
    "ssheet_id" serial NOT NULL,
    "ssheet_name" varchar(255),
    CONSTRAINT "stylesheet_pkey" PRIMARY KEY ("ssheet_id")
)
WITH (OIDS=FALSE);


CREATE TABLE "public"."style" 
(
    "style_id" serial NOT NULL,
    "style_alpha" double precision,
    "style_ambient" varchar(255),
    "style_diffuse" varchar(255),
    "style_emissive" varchar(255),
    "style_priority" int4 NOT NULL,
    "style_shininess" int4 NOT NULL,
    "style_specular" varchar(255),
    "style_tag" varchar(255),
    "style_ssheet_id" int4,
    CONSTRAINT "style_pkey" PRIMARY KEY ("style_id"),
    CONSTRAINT "style_ssheet_id_fkey" FOREIGN KEY ("style_ssheet_id") REFERENCES "public"."stylesheet" ("ssheet_id") ON DELETE CASCADE ON UPDATE NO ACTION
)
WITH (OIDS=FALSE);


/* Scene Management */

CREATE TABLE "public"."scene"               
(
    "scene_id" serial NOT NULL, 
    "scene_name" varchar(255) NOT NULL, 
    "scene_viewpoint" varchar(255) NOT NULL,
    CONSTRAINT "scene_pkey" PRIMARY KEY ("scene_id")
) 
WITH (OIDS=FALSE);

CREATE TABLE "public"."scene_fragment"          
(
    "scenef_id" serial NOT NULL, 
    "scenef_name" varchar(255), 
    "scenef_query_param_str" varchar(255), 
    "scenef_type" int4, 
    "scenef_assetset_id" int4, 
    "scenef_query_id" int4, 
    "scenef_scene_id" int4, 
    "scenef_ssheet_id" int4, 
    CONSTRAINT "scene_fragment_pkey" PRIMARY KEY ("scenef_id"), 
    CONSTRAINT "scenef_assetset_id_fkey" FOREIGN KEY ("scenef_assetset_id") REFERENCES "public"."asset_set" ("assetset_id") ON DELETE NO ACTION ON UPDATE NO ACTION, 
    CONSTRAINT "scenef_ssheet_id_fkey" FOREIGN KEY ("scenef_ssheet_id") REFERENCES "public"."stylesheet" ("ssheet_id") ON DELETE NO ACTION ON UPDATE NO ACTION, 
    CONSTRAINT "scenef_query_id_fkey" FOREIGN KEY ("scenef_query_id") REFERENCES "public"."query" ("query_id") ON DELETE NO ACTION ON UPDATE NO ACTION, 
    CONSTRAINT "scenef_scene_id_fkey" FOREIGN KEY ("scenef_scene_id") REFERENCES "public"."scene" ("scene_id") ON DELETE CASCADE ON UPDATE NO ACTION
) 
WITH (OIDS=FALSE);

CREATE TABLE "public"."scene_fragment_member"
(
    "scenefm_id" serial NOT NULL,
    "scenefm_entity_name" varchar(255),
    "scenefm_entity_id" int4,    
    "scenefm_visible" boolean,
    "scenefm_transform_pos_x" float8,
    "scenefm_transform_pos_y" float8,
    "scenefm_transform_pos_z" float8,
    "scenefm_transform_rot_x" float8,
    "scenefm_transform_rot_y" float8,
    "scenefm_transform_rot_z" float8,
    "scenefm_mtl_alpha" float8,
    "scenefm_mtl_ambient" varchar(255),
    "scenefm_mtl_diffuse" varchar(255),
    "scenefm_mtl_emissive" varchar(255),
    "scenefm_mtl_shininess" int4 NOT NULL,
    "scenefm_mtl_specular" varchar(255),
    "scenefm_scenef_id" int4,
    "scenefm_asset_id" int4,
    CONSTRAINT "scene_fragment_member_pkey" PRIMARY KEY ("scenefm_id"),
    CONSTRAINT "scenefm_scenef_id_fkey" FOREIGN KEY ("scenefm_scenef_id") REFERENCES "public"."scene_fragment" ("scenef_id") ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT "scenefm_asset_id_fkey" FOREIGN KEY ("scenefm_asset_id") REFERENCES "public"."asset" ("asset_id") ON DELETE NO ACTION ON UPDATE NO ACTION
)
WITH (OIDS=FALSE);




CREATE TABLE "public"."its_student_model"
(
    "itss_domain_id" int8,
    "itss_student_id" int8,
    "itss_p" double precision,
    "itss_timestamp" timestamp,
    CONSTRAINT "its_student_model_pkey" PRIMARY KEY ("itss_domain_id", "itss_student_id")
)
WITH (OIDS=FALSE);

CREATE TABLE "public"."its_enrolment"
(
    "itse_curriculum_id" int8,
    "itse_student_id" int8,
    "itse_dirty" boolean,
    CONSTRAINT "its_enrolment_pkey" PRIMARY KEY ("itse_curriculum_id", "itse_student_id"),
    CONSTRAINT "itse_student_id_fkey" FOREIGN KEY ("itse_student_id") REFERENCES "public"."usr" ("user_id") ON DELETE NO ACTION ON UPDATE NO ACTION
)
WITH (OIDS=FALSE);

CREATE TABLE "public"."demo"
(
    "demo_id" serial NOT NULL,
    "demo_name" varchar(255),
    CONSTRAINT "demo_pkey" PRIMARY KEY ("demo_id")
)
WITH (OIDS=FALSE);

CREATE TABLE "public"."its_event"
(
    "itsev_id" serial NOT NULL,
    "itsev_student_id" int8,
    "itsev_curriculum_id" int8,
    "itsev_subject_entity_id" int8,
    "itsev_relation" varchar(255),
    "itsev_object_entity_id" int8,
    "itsev_timestamp" timestamp,
    "itsev_value" varchar(20),
    "itsev_p_assertion" double precision,
    "itsev_description" text,
    "itsev_source" text,
    CONSTRAINT "its_event_pkey" PRIMARY KEY ("itsev_id"),
    CONSTRAINT "itsev_student_id_fkey" FOREIGN KEY ("itsev_student_id") REFERENCES "public"."usr" ("user_id") ON DELETE NO ACTION ON UPDATE NO ACTION
)
WITH (OIDS=FALSE);




/* ****************************************************************************
 * Activity Management 
 * ****************************************************************************/

CREATE TABLE "public"."activity_template"
(
    "activityt_id" serial NOT NULL,
    "activityt_name" varchar(255),
    "activityt_description" text,
    "activityt_url_play" text,
    "activityt_url_facts" text,
    CONSTRAINT "activity_template_pkey" PRIMARY KEY ("activityt_id")
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."activity_template" OWNER TO "postgres";
SELECT setval('activity_template_activityt_id_seq', 100);


CREATE TABLE "public"."activity_instance"
(
    "activityi_id" serial NOT NULL,
    "activityi_activityt_id" int4,
    "activityi_name" varchar(255),
    "activityi_description" text,
    "activityi_param_str" text,
    CONSTRAINT "activity_instance_pkey" PRIMARY KEY ("activityi_id"),
    CONSTRAINT "activityi_activityt_id_fkey" FOREIGN KEY ("activityi_activityt_id") REFERENCES "public"."activity_template" ("activityt_id") ON DELETE CASCADE ON UPDATE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."activity_instance" OWNER TO "postgres";
SELECT setval('activity_instance_activityi_id_seq', 100);


CREATE TABLE "public"."activity_template_param"
(
    "activitytp_label" varchar(255) NOT NULL,
    "activitytp_variable" varchar(255) NOT NULL, 
    "activitytp_activityt_id" int4 NOT NULL,
    "activitytp_type" varchar(8),
    CONSTRAINT "activity_template_param_pkey" PRIMARY KEY ("activitytp_variable","activitytp_activityt_id"),
    CONSTRAINT "activitytp_activityt_id_fkey" FOREIGN KEY ("activitytp_activityt_id") REFERENCES "public"."activity_template" ("activityt_id") ON DELETE CASCADE ON UPDATE NO ACTION 
) 
WITH (OIDS=FALSE);
ALTER TABLE "public"."activity_template_param" OWNER TO "postgres";


CREATE TABLE "public"."exercise"
(
    "exercise_activityi_id" int4,
    "exercise_curriculum_id" int4,
    CONSTRAINT "game_param_pkey" PRIMARY KEY ("exercise_activityi_id", "exercise_curriculum_id"), 
    CONSTRAINT "exercise_activityi_id_fkey" FOREIGN KEY ("exercise_activityi_id") REFERENCES "public"."activity_instance" ("activityi_id") ON DELETE CASCADE ON UPDATE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."exercise" OWNER TO "postgres";




/* Text Completion */
CREATE TABLE "public"."text_completion"
(
    "tc_value" varchar(512),
    CONSTRAINT "tc_pkey" PRIMARY KEY ("tc_value")
)
WITH (OIDS=FALSE);




-- Set table ownership
ALTER TABLE "public"."asset"                  OWNER TO "postgres";
ALTER TABLE "public"."asset_set"              OWNER TO "postgres";
ALTER TABLE "public"."asset_styletag"         OWNER TO "postgres";
ALTER TABLE "public"."demo"                   OWNER TO "postgres";
ALTER TABLE "public"."its_enrolment"          OWNER TO "postgres";
ALTER TABLE "public"."its_student_model"      OWNER TO "postgres";
ALTER TABLE "public"."its_event"              OWNER TO "postgres";
ALTER TABLE "public"."permission"             OWNER TO "postgres";
ALTER TABLE "public"."query"                  OWNER TO "postgres";
ALTER TABLE "public"."query_cache_line"       OWNER TO "postgres";
ALTER TABLE "public"."query_param"            OWNER TO "postgres";
ALTER TABLE "public"."role"                   OWNER TO "postgres";
ALTER TABLE "public"."role_perm_link"         OWNER TO "postgres";
ALTER TABLE "public"."scene"                  OWNER TO "postgres";
ALTER TABLE "public"."scene_fragment"         OWNER TO "postgres";
ALTER TABLE "public"."scene_fragment_member"  OWNER TO "postgres";
ALTER TABLE "public"."style"                  OWNER TO "postgres";
ALTER TABLE "public"."stylesheet"             OWNER TO "postgres";
ALTER TABLE "public"."text_completion"        OWNER TO "postgres";
ALTER TABLE "public"."user_role_link"         OWNER TO "postgres";
ALTER TABLE "public"."usergrp_user_link"      OWNER TO "postgres";
ALTER TABLE "public"."usergroup"              OWNER TO "postgres";
ALTER TABLE "public"."usr"                    OWNER TO "postgres";


-- Set primary key counters at 100 to avoid overlap with initial objects
SELECT setval('asset_asset_id_seq', 100);
SELECT setval('asset_set_assetset_id_seq', 100);
SELECT setval('demo_demo_id_seq', 100);
SELECT setval('its_event_itsev_id_seq', 100);
SELECT setval('permission_perm_id_seq', 100);
SELECT setval('query_query_id_seq', 100);
SELECT setval('query_cache_line_querycl_id_seq', 100);
SELECT setval('role_role_id_seq', 100);
SELECT setval('style_style_id_seq', 100);
SELECT setval('stylesheet_ssheet_id_seq', 100);
SELECT setval('usergroup_usergrp_id_seq', 100);
SELECT setval('usr_user_id_seq', 100);
SELECT setval('scene_scene_id_seq', 100);
SELECT setval('scene_fragment_scenef_id_seq', 100);
SELECT setval('scene_fragment_member_scenefm_id_seq', 100);

