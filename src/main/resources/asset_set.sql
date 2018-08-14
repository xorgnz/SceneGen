/*
Navicat PGSQL Data Transfer

Source Server         : Localhost
Source Server Version : 90302
Source Host           : localhost:5432
Source Database       : sceneGenTest
Source Schema         : public

Target Server Type    : PGSQL
Target Server Version : 90302
File Encoding         : 65001

Date: 2014-01-10 22:42:36
*/


-- ----------------------------
-- Table structure for "public"."asset_set"
-- ----------------------------
DROP TABLE "public"."asset_set";
CREATE TABLE "public"."asset_set" (
"id" int4 NOT NULL,
"maintainer" varchar(255) NOT NULL,
"name" varchar(255) NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of asset_set
-- ----------------------------

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table "public"."asset_set"
-- ----------------------------
ALTER TABLE "public"."asset_set" ADD PRIMARY KEY ("id");
