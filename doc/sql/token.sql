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
-- Table structure for user_account
-- 用户区块链账户信息，包括账户地址、私钥、keystore和密码
-- 钱包文件长度一般不超过500个bytes；
-- 一个用户名称有可能对应多个钱包地址；
-- TODO： 用户私钥将来需要替换，目前仅为DEBUG用。
-- ----------------------------
DROP TABLE IF EXISTS `user_account`;
CREATE TABLE `user_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `blockchain_address` varchar(42) DEFAULT NULL COMMENT '用户账户地址',
  `private_key` varchar(100) DEFAULT NULL COMMENT '用户私钥',
  `keystore` varchar(500) DEFAULT NULL COMMENT '用户钱包文件',
  `password` varchar(50) NOT NULL COMMENT '钱包文件密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='账户';

-- ----------------------------
-- Records of 通证
-- 本地节点测试token：

-- ----------------------------
INSERT INTO `user_account` VALUES ('1', '1260400284744613891', '0x7312f4b8a4457a36827f185325fd6b66a3f8bb8b', '0xc75a5f85ef779dcf95c651612efb3c3b9a6dfafb1bb5375905454d9fc8be8a6b', null, null);
INSERT INTO `user_account` VALUES ('2', '1413956884249956352', '0xa8863fc8ce3816411378685223c03daae9770ebb', null, null, null);


-- ----------------------------
-- Table structure for token_details
-- 用于作品的通证存储，根据区块链浏览器的内容设计
-- 一个作品只能有一个token，通证与作品为一一对应
-- ----------------------------
DROP TABLE IF EXISTS `book_token`;
CREATE TABLE `book_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `book_id` bigint(20) DEFAULT NULL COMMENT '小说ID',
  `token_name` bigint(20) DEFAULT NULL COMMENT '通证名称',
  `token_symbol` varchar(20) DEFAULT NULL COMMENT '通证代码',
  `token_address` varchar(42) DEFAULT NULL COMMENT '通证合约地址',
  `token_txhash` varchar(100) DEFAULT NULL COMMENT '通证合约产生的交易HASH',
  `total_supply` bigint(20) DEFAULT NULL COMMENT '通证总量',
  `token_creator` varchar(42) DEFAULT NULL COMMENT '通证的拥有者或者管理者',
  `decimals` tinyint(4) DEFAULT '16' COMMENT '通证小数位置，默认使用以太坊的1e-16',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='作品通证';

-- ----------------------------
-- Records of 通证
-- ----------------------------
INSERT INTO `token_details` VALUES ('1', '1423443050795876354','弘略演绎', '1423443050795876354','0xe6beb4e09e64c0f61ef639944ef97e0ca9a069a7', '0x3ea3634a33c88959247f4a191177d04cb9a6272be385a6920592b5e5de024636', '1000000', '0x7312f4b8a4457a36827f185325fd6b66a3f8bb8b', '18', '2021-08-26 13:00:30');
INSERT INTO `token_details` VALUES ('2', '1423443050795876353','另一种历史', '1423443050795876353','0xc3c6e85820d97477172498ce7aed37b0bb22e67e', '0x513b0393b0223c54fee918bb12c1d047c63fb26ee5988ebc2338243953b96ac6', '1000000', '0xa8863fc8ce3816411378685223c03daae9770ebb', '18', '2021-08-25 14:01:31');


-- ----------------------------
-- Table structure for user_token_list
--
-- ----------------------------
DROP TABLE IF EXISTS `user_token_list`;
CREATE TABLE `user_token_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `book_id` bigint(20) NOT NULL COMMENT '小说ID',
  `book_name` varchar(50) DEFAULT NULL COMMENT '小说名',
  `book_blockchain_address` varchar(42) DEFAULT NULL COMMENT '作品区块链合约地址0x开头',
  `token_balance` bigint(20) DEFAULT NULL COMMENT '作品区块链合约通证数量',
  `update_time` datetime DEFAULT NULL,
  `update_block_height` bigint(20) NOT NULL COMMENT '通证更新的区块高度',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_userid_bookid` (`user_id`,`book_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COMMENT='用户通证列表';

INSERT INTO `user_token_list` VALUES ('11', '1413956884249956352','1414650094664269824','大国游戏','0xe592303d955ca9c515a7d62f374cf46cbeb42dfb','101','2021-07-14 10:53:51','0');
INSERT INTO `user_token_list` VALUES ('12', '1413956884249956352','1414653690969579520','西游记','0x03afc7ca5b56434ebcf8f03eb80f9c52d6b36fed','102', '2021-07-15 10:53:51','0');
INSERT INTO `user_token_list` VALUES ('13', '1413956884249956352','1414653961212780544','三国演义','0x48b106f4bf30f9ef83557141341c060e6d954e19','103', '2021-07-12 08:52:37','0');

-- ----------------------------
-- Table structure for token_tx_list
-- 包含通证交易的历史信息，从区块链节点提取
-- ----------------------------
DROP TABLE IF EXISTS `token_tx_list`;
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
