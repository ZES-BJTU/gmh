/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : gmh

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2018-01-28 22:55:42
*/

SET FOREIGN_KEY_CHECKS=0;

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
  `employee_work_type_id` bigint(20) DEFAULT NULL COMMENT '员工工种id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for employee_work_type
-- ----------------------------
DROP TABLE IF EXISTS `employee_work_type`;
CREATE TABLE `employee_work_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `top_type` int(11) DEFAULT NULL COMMENT '员工工种所属顶层分类',
  `name` varchar(255) DEFAULT NULL COMMENT '员工工种名称',
  `store_id` bigint(20) DEFAULT NULL COMMENT '员工工种所属门店',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for member_card
-- ----------------------------
DROP TABLE IF EXISTS `member_card`;
CREATE TABLE `member_card` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` tinyint(4) DEFAULT NULL COMMENT '会员卡类型',
  `name` varchar(255) DEFAULT NULL COMMENT '会员卡名称',
  `price` decimal(10,0) DEFAULT NULL COMMENT '价格',
  `project_id` bigint(20) DEFAULT NULL COMMENT '次卡绑定项目id',
  `times` int(11) DEFAULT NULL COMMENT '次卡可消费次数',
  `amount` decimal(10,0) DEFAULT NULL COMMENT '储值卡总额',
  `project_discount` decimal(10,0) DEFAULT NULL COMMENT '储值卡做项目优惠(折扣)',
  `product_discount` decimal(10,0) DEFAULT NULL COMMENT '储值卡买产品优惠(折扣)',
  `store_id` bigint(20) DEFAULT NULL COMMENT '会员卡所属门店id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
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
  `unitn_ame` bigint(20) DEFAULT NULL COMMENT '产品计量单位id',
  `unit_price` decimal(10,0) DEFAULT NULL COMMENT '产品单价',
  `total_amount` bigint(20) DEFAULT NULL COMMENT '产品数量(针对产品计量单位)',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for product_consume
-- ----------------------------
DROP TABLE IF EXISTS `product_consume`;
CREATE TABLE `product_consume` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `amount` bigint(20) DEFAULT NULL COMMENT '卖出产品数量',
  `store_id` bigint(20) DEFAULT NULL COMMENT '卖出产品所属门店id',
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
  `store_id` bigint(20) DEFAULT NULL COMMENT '产品分类所说门店',
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
  `stock_consume_amount` bigint(20) DEFAULT NULL COMMENT '库存消耗数量',
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
-- Table structure for stock
-- ----------------------------
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库存id',
  `stock_type_id` bigint(20) DEFAULT NULL COMMENT '库存分类id',
  `code` varchar(255) DEFAULT NULL COMMENT '库存唯一代码',
  `name` varchar(255) DEFAULT NULL COMMENT '库存名称',
  `unit_name` varchar(255) DEFAULT NULL COMMENT '库存计量单位名称',
  `total_amount` decimal(10,0) DEFAULT NULL COMMENT '库存总量(针对库存计量单位)',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for stock_consume
-- ----------------------------
DROP TABLE IF EXISTS `stock_consume`;
CREATE TABLE `stock_consume` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stock_id` bigint(20) DEFAULT NULL COMMENT '库存id',
  `amount` bigint(20) DEFAULT NULL COMMENT '库存消耗数量',
  `store_id` bigint(20) DEFAULT NULL COMMENT '库存消耗所属门店',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for stock_type
-- ----------------------------
DROP TABLE IF EXISTS `stock_type`;
CREATE TABLE `stock_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库存类型id',
  `name` varchar(255) DEFAULT NULL COMMENT '库存分类名称',
  `store_id` bigint(20) DEFAULT NULL COMMENT '库存分类所属门店id',
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
  `principal_id` bigint(20) DEFAULT NULL COMMENT '门店负责人id',
  `mobile` varchar(255) DEFAULT NULL COMMENT '门店手机号码',
  `phone` varchar(255) DEFAULT NULL COMMENT '门店座机电话',
  `remark` varchar(255) DEFAULT NULL COMMENT '门店备注信息',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_principal_id` (`principal_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
