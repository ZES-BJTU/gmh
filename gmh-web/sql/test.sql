/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2018-02-04 23:33:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for appointment
-- ----------------------------
DROP TABLE IF EXISTS `appointment`;
CREATE TABLE `appointment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(255) DEFAULT NULL COMMENT '客户姓名',
  `customer_mobile` varchar(255) DEFAULT NULL COMMENT '客户联系电话',
  `customer_gender` tinyint(4) DEFAULT NULL COMMENT '客户性别',
  `is_vip` tinyint(4) DEFAULT NULL COMMENT '是否会员',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户id',
  `status` tinyint(4) DEFAULT NULL COMMENT '预约状态',
  `store_id` bigint(20) DEFAULT NULL COMMENT '预约所属门店id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of appointment
-- ----------------------------

-- ----------------------------
-- Table structure for appointment_project
-- ----------------------------
DROP TABLE IF EXISTS `appointment_project`;
CREATE TABLE `appointment_project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appointment_id` bigint(20) DEFAULT NULL COMMENT '预约id',
  `project_id` bigint(20) DEFAULT NULL COMMENT '预约项目id',
  `employee_id` bigint(20) DEFAULT NULL COMMENT '预约操作员(美容师、美甲师或美睫师)id',
  `begin_time` datetime DEFAULT NULL COMMENT '预约项目开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '预约项目结束时间',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of appointment_project
-- ----------------------------

-- ----------------------------
-- Table structure for consume_record
-- ----------------------------
DROP TABLE IF EXISTS `consume_record`;
CREATE TABLE `consume_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trade_serial_number` varchar(255) DEFAULT NULL COMMENT '交易流水号',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户id',
  `consume_type` tinyint(4) DEFAULT NULL COMMENT '消费类型',
  `consume_money` decimal(10,0) DEFAULT NULL COMMENT '消费金额',
  `payment_way` tinyint(4) DEFAULT NULL COMMENT '支付方式',
  `activity_name` varchar(255) DEFAULT NULL COMMENT '活动名称(打印单据使用)',
  `is_modified` tinyint(4) DEFAULT NULL COMMENT '是否已经被修改',
  `remark` varchar(255) DEFAULT NULL COMMENT '消费记录备注',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '产品id',
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of consume_record
-- ----------------------------

-- ----------------------------
-- Table structure for consume_record_card
-- ----------------------------
DROP TABLE IF EXISTS `consume_record_card`;
CREATE TABLE `consume_record_card` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_card_id` bigint(20) DEFAULT NULL COMMENT '会员卡id',
  `member_card_amount` int(11) DEFAULT NULL COMMENT '会员卡',
  `consultant_id` bigint(20) DEFAULT NULL COMMENT '美容师/顾问id',
  `consultant_name` varchar(255) DEFAULT NULL COMMENT '美容师/顾问姓名',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of consume_record_card
-- ----------------------------

-- ----------------------------
-- Table structure for consume_record_gift
-- ----------------------------
DROP TABLE IF EXISTS `consume_record_gift`;
CREATE TABLE `consume_record_gift` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` tinyint(4) DEFAULT NULL COMMENT '赠品类型',
  `project_id` bigint(20) DEFAULT NULL COMMENT '赠送项目id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '赠送产品id',
  `coupon_money` decimal(10,0) DEFAULT NULL COMMENT '赠送代金券金额',
  `amount` int(11) DEFAULT NULL COMMENT '赠送项目/产品和代金券数量',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of consume_record_gift
-- ----------------------------

-- ----------------------------
-- Table structure for consume_record_product
-- ----------------------------
DROP TABLE IF EXISTS `consume_record_product`;
CREATE TABLE `consume_record_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `product_amount` int(11) DEFAULT NULL COMMENT '产品购买数量',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of consume_record_product
-- ----------------------------

-- ----------------------------
-- Table structure for consume_record_project
-- ----------------------------
DROP TABLE IF EXISTS `consume_record_project`;
CREATE TABLE `consume_record_project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目id',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作员id',
  `consultant_id` bigint(20) DEFAULT NULL COMMENT '美容师/顾问id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of consume_record_project
-- ----------------------------

-- ----------------------------
-- Table structure for consume_record_salesman
-- ----------------------------
DROP TABLE IF EXISTS `consume_record_salesman`;
CREATE TABLE `consume_record_salesman` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `salesman_id` bigint(20) DEFAULT NULL COMMENT '推销员id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of consume_record_salesman
-- ----------------------------

-- ----------------------------
-- Table structure for consume_record_update
-- ----------------------------
DROP TABLE IF EXISTS `consume_record_update`;
CREATE TABLE `consume_record_update` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trade_serial_number` varchar(255) DEFAULT NULL COMMENT '交易流水号',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户id',
  `consume_type` tinyint(4) DEFAULT NULL COMMENT '消费类型',
  `consume_money` decimal(10,0) DEFAULT NULL COMMENT '消费金额',
  `payment_way` tinyint(4) DEFAULT NULL COMMENT '支付方式',
  `activity_name` varchar(255) DEFAULT NULL COMMENT '活动名称(打印单据使用)',
  `remark` varchar(255) DEFAULT NULL COMMENT '消费记录备注',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '产品id',
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of consume_record_update
-- ----------------------------

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '客户姓名',
  `gender` tinyint(4) DEFAULT NULL COMMENT '客户性别',
  `mobile` varchar(255) DEFAULT NULL COMMENT '客户联系电话',
  `birthday` date DEFAULT NULL COMMENT '客户生日',
  `input_time` datetime DEFAULT NULL COMMENT '客户信息录入时间',
  `source` varchar(255) DEFAULT NULL COMMENT '客户来源',
  `store_id` bigint(20) DEFAULT NULL COMMENT '客户所属门店',
  `remark` varchar(255) DEFAULT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer
-- ----------------------------

-- ----------------------------
-- Table structure for customer_member_card
-- ----------------------------
DROP TABLE IF EXISTS `customer_member_card`;
CREATE TABLE `customer_member_card` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户id',
  `member_card_id` bigint(20) DEFAULT NULL COMMENT '会员卡id',
  `remaining_money` decimal(10,0) DEFAULT NULL COMMENT '会员卡剩余金额',
  `remaining_times` int(11) DEFAULT NULL COMMENT '会员卡剩余项目次数',
  `is_valid` tinyint(4) DEFAULT NULL COMMENT '当前卡是否有效',
  `is_returned` tinyint(4) DEFAULT NULL COMMENT '是否已退',
  `returned_reason` varchar(255) DEFAULT NULL COMMENT '退卡原因',
  `returned_time` datetime DEFAULT NULL COMMENT '退卡时间',
  `returned_money` decimal(10,0) DEFAULT NULL COMMENT '退还金额',
  `is_turned` tinyint(4) DEFAULT NULL COMMENT '是否为转出卡',
  `turned_reason` varchar(255) DEFAULT NULL COMMENT '转卡原因',
  `turned_time` datetime DEFAULT NULL COMMENT '转卡时间',
  `turned_money` varchar(255) DEFAULT NULL COMMENT '转卡退还或补交金额',
  `unique_identifier` varchar(255) DEFAULT NULL COMMENT '会员卡的唯一标识(针对转卡过程中逻辑卡唯一定位)',
  `store_id` bigint(20) DEFAULT NULL COMMENT '客户会员卡当前所属门店',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer_member_card
-- ----------------------------

-- ----------------------------
-- Table structure for customer_member_card_coupon
-- ----------------------------
DROP TABLE IF EXISTS `customer_member_card_coupon`;
CREATE TABLE `customer_member_card_coupon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_member_card_id` bigint(20) DEFAULT NULL COMMENT '客户会员卡id',
  `name` varchar(255) DEFAULT NULL COMMENT '代金券名称',
  `money` decimal(10,0) DEFAULT NULL COMMENT '代金券金额',
  `piece` int(11) DEFAULT NULL COMMENT '代金券张数',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer_member_card_coupon
-- ----------------------------
