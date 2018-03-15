/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : gmh

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-03-14 23:57:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for activity_content
-- ----------------------------
DROP TABLE IF EXISTS `activity_content`;
CREATE TABLE `activity_content` (
  `id` bigint(20) NOT NULL,
  `type` tinyint(4) DEFAULT NULL COMMENT '活动内容类型',
  `related_id` bigint(20) DEFAULT NULL COMMENT '关联id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
