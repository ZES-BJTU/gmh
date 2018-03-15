/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : gmh

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-03-15 21:16:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '活动名称',
  `price` decimal(10,0) DEFAULT NULL COMMENT '活动价格',
  `code` varchar(255) DEFAULT NULL COMMENT '活动代码',
  `deadline` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL COMMENT '活动备注',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动表';

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

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '员工姓名',
  `gender` tinyint(4) DEFAULT NULL COMMENT '员工性别',
  `mobile` varchar(255) DEFAULT NULL COMMENT '员工手机号',
  `entry_time` datetime DEFAULT NULL COMMENT '员工入职时间',
  `is_working` tinyint(4) DEFAULT NULL COMMENT '员工是否在职',
  `store_id` bigint(20) DEFAULT NULL COMMENT '员工所属门店',
  `remark` varchar(255) DEFAULT NULL COMMENT '员工备注',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_entry_time` (`entry_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for employee_work
-- ----------------------------
DROP TABLE IF EXISTS `employee_work`;
CREATE TABLE `employee_work` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `employee_id` bigint(20) DEFAULT NULL COMMENT '员工id',
  `top_type` tinyint(4) DEFAULT NULL COMMENT '员工工种顶层分类',
  `work_type` int(11) DEFAULT NULL COMMENT '员工工种',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_type_id` bigint(20) DEFAULT NULL COMMENT '产品类型id',
  `name` varchar(255) DEFAULT NULL COMMENT '产品名称',
  `unit_name` varchar(255) DEFAULT NULL COMMENT '产品计量单位名称',
  `unit_price` decimal(10,0) DEFAULT NULL COMMENT '产品单价(单位:元)',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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

-- ----------------------------
-- Table structure for product_flow
-- ----------------------------
DROP TABLE IF EXISTS `product_flow`;
CREATE TABLE `product_flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `type` tinyint(4) DEFAULT NULL,
  `amount` decimal(10,0) DEFAULT NULL COMMENT '买入或卖出产品数量',
  `store_id` bigint(20) DEFAULT NULL COMMENT '卖出产品所属门店id',
  `status` tinyint(4) DEFAULT NULL COMMENT '流水是否有效(1:有效,0:逻辑删除)',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for product_stock_unit_map
-- ----------------------------
DROP TABLE IF EXISTS `product_stock_unit_map`;
CREATE TABLE `product_stock_unit_map` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `product_amount` bigint(20) DEFAULT NULL COMMENT '产品数量',
  `stock_id` bigint(20) DEFAULT NULL COMMENT '库存id',
  `stock_amount` bigint(20) DEFAULT NULL COMMENT '库存数量',
  `store_id` bigint(20) DEFAULT NULL COMMENT '产品库存转换关系所属门店id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for product_type
-- ----------------------------
DROP TABLE IF EXISTS `product_type`;
CREATE TABLE `product_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '产品分类名称',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_type_id` bigint(20) DEFAULT NULL COMMENT '项目类型id',
  `name` varchar(255) DEFAULT NULL COMMENT '项目名称',
  `unit_price` decimal(10,0) DEFAULT NULL COMMENT '项目单价',
  `integral` decimal(10,0) DEFAULT NULL COMMENT '项目积分',
  `intern_integral` decimal(10,0) DEFAULT NULL COMMENT '项目实习生积分',
  `store_id` bigint(20) DEFAULT NULL COMMENT '项目所属门店id',
  `remark` varchar(255) DEFAULT NULL COMMENT '项目备注',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for project_stock
-- ----------------------------
DROP TABLE IF EXISTS `project_stock`;
CREATE TABLE `project_stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目id',
  `stock_id` bigint(20) DEFAULT NULL COMMENT '库存id',
  `stock_consume_amount` decimal(10,0) DEFAULT NULL COMMENT '库存消耗数量',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for project_type
-- ----------------------------
DROP TABLE IF EXISTS `project_type`;
CREATE TABLE `project_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `top_type` int(11) DEFAULT NULL COMMENT '项目顶层分类',
  `name` varchar(255) DEFAULT NULL COMMENT '项目分类名称',
  `store_id` bigint(20) DEFAULT NULL COMMENT '项目分类所属门店id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for store
-- ----------------------------
DROP TABLE IF EXISTS `store`;
CREATE TABLE `store` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '门店id',
  `name` varchar(255) DEFAULT NULL COMMENT '门店名称',
  `address` varchar(255) DEFAULT NULL COMMENT '门店地址',
  `phone` varchar(255) DEFAULT NULL COMMENT '门店座机电话',
  `remark` varchar(255) DEFAULT NULL COMMENT '门店备注信息',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `role` tinyint(4) DEFAULT NULL,
  `account` varchar(255) DEFAULT NULL COMMENT '用户名',
  `email` varchar(255) DEFAULT NULL COMMENT '用户邮箱',
  `mobile` varchar(255) DEFAULT NULL COMMENT '用户手机号',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `salt` varchar(255) DEFAULT NULL COMMENT '盐值',
  `name` varchar(255) DEFAULT NULL COMMENT '用户姓名',
  `gender` tinyint(4) DEFAULT NULL COMMENT '用户性别',
  `store_id` bigint(20) DEFAULT NULL COMMENT '用户所属门店id',
  `remark` varchar(255) DEFAULT NULL COMMENT '用户备注信息',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account` (`account`) USING BTREE,
  UNIQUE KEY `uk_email` (`email`) USING BTREE,
  UNIQUE KEY `uk_mobile` (`mobile`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_token
-- ----------------------------
DROP TABLE IF EXISTS `user_token`;
CREATE TABLE `user_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户tokenid',
  `user_id` bigint(20) DEFAULT NULL COMMENT '系统登录用户id',
  `token` varchar(255) DEFAULT NULL COMMENT '系统登录用户token',
  `login_time` datetime DEFAULT NULL COMMENT '系统登录用户登录时间',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE,
  UNIQUE KEY `uk_token` (`token`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
