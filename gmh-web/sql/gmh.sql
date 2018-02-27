-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: gmh
-- ------------------------------------------------------
-- Server version	5.7.21-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(255) DEFAULT NULL COMMENT '客户姓名',
  `customer_mobile` varchar(255) DEFAULT NULL COMMENT '客户联系电话',
  `customer_gender` tinyint(4) DEFAULT NULL COMMENT '客户性别',
  `is_vip` tinyint(4) DEFAULT NULL COMMENT '是否会员',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户id',
  `is_line` tinyint(4) DEFAULT NULL COMMENT '是否点排',
  `status` tinyint(4) DEFAULT NULL COMMENT '预约状态',
  `remarks` varchar(255) DEFAULT NULL,
  `store_id` bigint(20) DEFAULT NULL COMMENT '预约所属门店id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `appointment_project`
--

DROP TABLE IF EXISTS `appointment_project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `consume_record`
--

DROP TABLE IF EXISTS `consume_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consume_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trade_serial_number` varchar(255) DEFAULT NULL COMMENT '交易流水号',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户id',
  `customer_mobile` varchar(255) DEFAULT NULL,
  `consume_type` tinyint(4) DEFAULT NULL COMMENT '消费类型\n1：办卡\n2：买产品\n3：做项目',
  `consume_money` decimal(10,0) DEFAULT NULL COMMENT '消费金额',
  `payment_way` tinyint(4) DEFAULT NULL COMMENT '支付方式',
  `activity_id` bigint(20) DEFAULT NULL COMMENT '活动名称(打印单据使用)',
  `is_modified` tinyint(4) DEFAULT NULL COMMENT '是否已经被修改',
  `remark` varchar(255) DEFAULT NULL COMMENT '消费记录备注',
  `consume_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消费时间',
  `store_id` bigint(20) DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `consume_record_product`
--

DROP TABLE IF EXISTS `consume_record_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consume_record_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trade_serial_number` varchar(200) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `product_amount` int(11) DEFAULT NULL COMMENT '产品购买数量',
  `operator_id` bigint(20) DEFAULT NULL,
  `consultant_id` bigint(20) DEFAULT NULL,
  `sales_man_id` bigint(20) DEFAULT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '客户姓名',
  `gender` tinyint(4) DEFAULT NULL COMMENT '客户性别',
  `mobile` varchar(255) DEFAULT NULL COMMENT '客户联系电话',
  `birthday` date DEFAULT NULL COMMENT '客户生日',
  `input_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '客户信息录入时间',
  `source` varchar(255) DEFAULT NULL COMMENT '客户来源',
  `store_id` bigint(20) DEFAULT NULL COMMENT '客户所属门店',
  `remark` varchar(255) DEFAULT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_member_card`
--

DROP TABLE IF EXISTS `customer_member_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `employee_work`
--

DROP TABLE IF EXISTS `employee_work`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee_work` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `employee_id` bigint(20) DEFAULT NULL COMMENT '员工id',
  `employee_work_type_id` bigint(20) DEFAULT NULL COMMENT '员工工种id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `employee_work_type`
--

DROP TABLE IF EXISTS `employee_work_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee_work_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `top_type` int(11) DEFAULT NULL COMMENT '员工工种所属顶层分类',
  `name` varchar(255) DEFAULT NULL COMMENT '员工工种名称',
  `store_id` bigint(20) DEFAULT NULL COMMENT '员工工种所属门店',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `member_card`
--

DROP TABLE IF EXISTS `member_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_consume`
--

DROP TABLE IF EXISTS `product_consume`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_consume` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `amount` bigint(20) DEFAULT NULL COMMENT '卖出产品数量',
  `store_id` bigint(20) DEFAULT NULL COMMENT '卖出产品所属门店id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_stock_unit_map`
--

DROP TABLE IF EXISTS `product_stock_unit_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_type`
--

DROP TABLE IF EXISTS `product_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '产品分类名称',
  `store_id` bigint(20) DEFAULT NULL COMMENT '产品分类所说门店',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `project_stock`
--

DROP TABLE IF EXISTS `project_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project_stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目id',
  `stock_id` bigint(20) DEFAULT NULL COMMENT '库存id',
  `stock_consume_amount` decimal(10,0) DEFAULT NULL COMMENT '库存消耗数量',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `project_type`
--

DROP TABLE IF EXISTS `project_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `top_type` int(11) DEFAULT NULL COMMENT '项目顶层分类',
  `name` varchar(255) DEFAULT NULL COMMENT '项目分类名称',
  `store_id` bigint(20) DEFAULT NULL COMMENT '项目分类所属门店id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock`
--

DROP TABLE IF EXISTS `stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock_consume`
--

DROP TABLE IF EXISTS `stock_consume`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock_consume` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stock_id` bigint(20) DEFAULT NULL COMMENT '库存id',
  `amount` bigint(20) DEFAULT NULL COMMENT '库存消耗数量',
  `store_id` bigint(20) DEFAULT NULL COMMENT '库存消耗所属门店',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock_type`
--

DROP TABLE IF EXISTS `stock_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库存类型id',
  `name` varchar(255) DEFAULT NULL COMMENT '库存分类名称',
  `store_id` bigint(20) DEFAULT NULL COMMENT '库存分类所属门店id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trade_serial_number`
--

DROP TABLE IF EXISTS `trade_serial_number`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trade_serial_number` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `number_type` varchar(20) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_token`
--

DROP TABLE IF EXISTS `user_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-02-27 15:32:57
