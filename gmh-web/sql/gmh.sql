/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : gmh

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-01-20 16:22:41
*/

SET FOREIGN_KEY_CHECKS=0;

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
  PRIMARY KEY (`id`),
  KEY `idx_principal_id` (`principal_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户主键id',
  `role` tinyint(4) DEFAULT NULL COMMENT '用户角色',
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '系统登录用户id',
  `token` varchar(255) DEFAULT NULL COMMENT '系统登录用户token',
  `login_time` datetime DEFAULT NULL COMMENT '系统登录用户登录时间',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE,
  UNIQUE KEY `uk_token` (`token`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
