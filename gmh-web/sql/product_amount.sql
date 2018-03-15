/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : gmh

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-03-11 22:09:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for product_amount
-- ----------------------------
DROP TABLE IF EXISTS `product_amount`;
CREATE TABLE `product_amount` (
  `id` bigint(20) NOT NULL,
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `amount` decimal(10,0) DEFAULT NULL COMMENT '某个门店对应产品当前总量',
  `store_id` bigint(20) DEFAULT NULL COMMENT '门店id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
