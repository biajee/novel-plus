/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : novel_blockchain

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2021-08-07 21:42:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for token_details
-- 用于作品的通证存储，根据区块链浏览器的内容设计
-- ----------------------------
DROP TABLE IF EXISTS `token_details`;
CREATE TABLE `token_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `token_name` bigint(20) DEFAULT NULL COMMENT '通证名称',
  `token_address` varchar(42) DEFAULT NULL COMMENT '邀请码',
  `total_supply` bigint(20) DEFAULT NULL COMMENT '通证总量',
  `token_creator` varchar(42) DEFAULT NULL COMMENT '通证的拥有者或者管理者',
  `decimals` tinyint(4) DEFAULT '16' COMMENT '通证小数位置，默认使用以太坊的1e-16',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='作者表';

-- ----------------------------
-- Records of 通证
-- ----------------------------
INSERT INTO `token_details` VALUES ('1', null, 'reerer', 'abc', '13560487656', '23484388', '23484388@qq.com', '0', '0', null);
INSERT INTO `token_details` VALUES ('2', '另一种历史', 'rwrr445554', '梦入神机', '13560421324', '1179705413', 'reerer@qq.com', '0', '0', '2020-05-13 14:01:31');

-- ----------------------------
-- Table structure for token_list
-- ----------------------------
DROP TABLE IF EXISTS `author_code`;
CREATE TABLE `author_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `invite_code` varchar(100) DEFAULT NULL COMMENT '邀请码',
  `validity_time` datetime DEFAULT NULL COMMENT '有效时间',
  `is_use` tinyint(1) DEFAULT '0' COMMENT '是否使用过，0：未使用，1:使用过',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_code` (`invite_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='作家邀请码表';

-- ----------------------------
-- Records of author_code
-- ----------------------------
INSERT INTO `author_code` VALUES ('3', 'reerer', '2020-05-27 22:43:45', '1', '2020-05-13 11:40:56', '1');
INSERT INTO `author_code` VALUES ('4', '123456', '2020-05-28 00:00:00', '0', '2020-05-13 14:09:55', '1');
INSERT INTO `author_code` VALUES ('5', '111111', '2020-05-21 00:00:00', '0', '2020-08-13 14:18:58', '1');



-- ----------------------------
-- Table structure for user_buy_record
-- ----------------------------
DROP TABLE IF EXISTS `user_buy_record`;
CREATE TABLE `user_buy_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `book_id` bigint(20) DEFAULT NULL COMMENT '购买的小说ID',
  `book_name` varchar(50) DEFAULT NULL COMMENT '购买的小说名',
  `book_index_id` bigint(20) DEFAULT NULL COMMENT '购买的章节ID',
  `book_index_name` varchar(100) DEFAULT NULL COMMENT '购买的章节名',
  `buy_amount` int(11) DEFAULT NULL COMMENT '购买使用的屋币数量',
  `create_time` datetime DEFAULT NULL COMMENT '购买时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_userId_indexId` (`user_id`,`book_index_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户消费记录表';

-- ----------------------------
-- Records of user_buy_record
-- ----------------------------
INSERT INTO `user_buy_record` VALUES ('1', '1255060328322027520', '1260400284744613890', '我是一只消消乐2', '1260522024606953472', '第三章', '10', '2020-05-13 21:29:09');
INSERT INTO `user_buy_record` VALUES ('2', '1255060328322027520', '1260400284744613890', '我是一只消消乐2', '1260564410687107072', '第四章', '10', '2020-05-13 21:40:38');
