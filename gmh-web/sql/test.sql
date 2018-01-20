/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2018-01-20 11:26:51
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
  PRIMARY KEY (`id`)
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
  `product_unit_id` bigint(20) DEFAULT NULL COMMENT '产品计量单位id',
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
-- Table structure for product_stock_unit_convert
-- ----------------------------
DROP TABLE IF EXISTS `product_stock_unit_convert`;
CREATE TABLE `product_stock_unit_convert` (
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
-- Table structure for product_unit
-- ----------------------------
DROP TABLE IF EXISTS `product_unit`;
CREATE TABLE `product_unit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '产品计量单位',
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stock_type_id` bigint(20) DEFAULT NULL COMMENT '库存分类id',
  `name` varchar(255) DEFAULT NULL COMMENT '库存名称',
  `stock_unit_id` bigint(20) DEFAULT NULL COMMENT '库存计量单位id',
  `total_amount` bigint(20) DEFAULT NULL COMMENT '库存总量(针对库存计量单位)',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '库存分类名称',
  `store_id` bigint(20) DEFAULT NULL COMMENT '库存分类所属门店id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for stock_unit
-- ----------------------------
DROP TABLE IF EXISTS `stock_unit`;
CREATE TABLE `stock_unit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '库存计量单位',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for store
-- ----------------------------
DROP TABLE IF EXISTS `store`;
CREATE TABLE `store` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '门店名称',
  `address` varchar(255) DEFAULT NULL COMMENT '门店地址',
  `principal_id` bigint(20) DEFAULT NULL COMMENT '门店负责人id',
  `mobile` varchar(255) DEFAULT NULL COMMENT '门店手机号码',
  `phone` varchar(255) DEFAULT NULL COMMENT '门店座机电话',
  `remark` varchar(255) DEFAULT NULL COMMENT '门店备注信息',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
