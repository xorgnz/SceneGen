DROP TABLE IF EXISTS "public"."exercise";
DROP TABLE IF EXISTS "public"."activity_instance";
DROP TABLE IF EXISTS "public"."activity_template_param";
DROP TABLE IF EXISTS "public"."activity_template";
DROP TABLE IF EXISTS "public"."game_param";
DROP TABLE IF EXISTS "public"."game_template_param";
DROP TABLE IF EXISTS "public"."game";
DROP TABLE IF EXISTS "public"."game_template";


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
    "exercise_id" serial NOT NULL,
    "exercise_activityi_id" int4,
    "exercise_curriculum_id" int4,
    CONSTRAINT "exercise_pkey" PRIMARY KEY ("exercise_id"),
    CONSTRAINT "exercise_unique" UNIQUE ("exercise_activityi_id","exercise_curriculum_id"),
    CONSTRAINT "exercise_activityi_id_fkey" FOREIGN KEY ("exercise_activityi_id") REFERENCES "public"."activity_instance" ("activityi_id") ON DELETE CASCADE ON UPDATE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."exercise" OWNER TO "postgres";
SELECT setval('exercise_exercise_id_seq', 100);
