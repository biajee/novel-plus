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
  `password` varchar(50) DEFAULT NULL COMMENT '钱包文件密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='账户';

-- ----------------------------
-- Records of 账户
-- 本地节点测试账户
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
  `token_name` varchar(20) DEFAULT NULL COMMENT '通证名称',
  `token_symbol` varchar(20) DEFAULT NULL COMMENT '通证代码',
  `token_address` varchar(42) DEFAULT NULL COMMENT '通证合约地址',
  `token_txhash` varchar(100) DEFAULT NULL COMMENT '通证合约产生的交易HASH',
  `token_supply` int(10) DEFAULT NULL COMMENT '通证总量',
  `token_creator` varchar(42) DEFAULT NULL COMMENT '通证的拥有者或者管理者',
  `token_decimals` tinyint(4) DEFAULT '16' COMMENT '通证小数位置，默认使用以太坊的1e-16',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='作品通证';

-- ----------------------------
-- Records of 通证
-- 本地测试节点
-- ----------------------------
INSERT INTO `book_token` VALUES ('1', '1423443050795876354','弘略演绎', '1423443050795876354','0xe6beb4e09e64c0f61ef639944ef97e0ca9a069a7', '0x3ea3634a33c88959247f4a191177d04cb9a6272be385a6920592b5e5de024636', '1000000', '0x7312f4b8a4457a36827f185325fd6b66a3f8bb8b', '18', '2021-08-26 13:00:30');
INSERT INTO `book_token` VALUES ('2', '1423443050795876353','另一种历史', '1423443050795876353','0xc3c6e85820d97477172498ce7aed37b0bb22e67e', '0x513b0393b0223c54fee918bb12c1d047c63fb26ee5988ebc2338243953b96ac6', '1000000', '0xa8863fc8ce3816411378685223c03daae9770ebb', '18', '2021-08-25 14:01:31');


-- ----------------------------
-- Table structure for user_token_list
-- 显示用户所拥有的通证列表
-- 通证名称，需要与小说名相同
-- 增加了记录通证收入的token_income
-- ----------------------------
DROP TABLE IF EXISTS `user_token_list`;
CREATE TABLE `user_token_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `book_id` bigint(20) NOT NULL COMMENT '小说ID',
  `token_name` varchar(50) NOT NULL COMMENT '通证名称',
  `token_address` varchar(42) DEFAULT NULL COMMENT '作品区块链合约地址0x开头',
  `token_balance` bigint(20) DEFAULT NULL COMMENT '作品区块链合约通证数量',
  `token_income` bigint(20) DEFAULT 0 COMMENT '作品区块链合约通证的收入，单位为分',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_userid_bookid` (`user_id`,`book_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户通证列表';

INSERT INTO `user_token_list` VALUES ('8', '1423713850367029248','1423443050795876353','另一种历史','0xe592303d955ca9c515a7d62f374cf46cbeb42dfb','100','1000','2021-08-1 10:53:51');
INSERT INTO `user_token_list` VALUES ('9', '1423713850367029248','1423443050795876354','弘略演绎','0x03afc7ca5b56434ebcf8f03eb80f9c52d6b36fed','10','1000', '2021-08-15 10:53:51');
INSERT INTO `user_token_list` VALUES ('10', '1423713850367029248','1414653961212780544','临高启明','0x48b106f4bf30f9ef83557141341c060e6d954e19','20','2000', '2021-08-12 08:52:37');

INSERT INTO `user_token_list` VALUES ('11', '1413956884249956352','1423443050795876353','另一种历史','0xe592303d955ca9c515a7d62f374cf46cbeb42dfb','1000','510','2021-07-14 10:53:51');
INSERT INTO `user_token_list` VALUES ('12', '1413956884249956352','1423443050795876354','弘略演绎','0x03afc7ca5b56434ebcf8f03eb80f9c52d6b36fed','100','100', '2021-07-15 10:53:51');
INSERT INTO `user_token_list` VALUES ('13', '1413956884249956352','1414653961212780544','临高启明','0x48b106f4bf30f9ef83557141341c060e6d954e19','200','2000', '2021-07-12 08:52:37');

-- ----------------------------
-- Table structure for token_tx_list
-- 包含通证交易的历史信息，从区块链节点提取，
-- 参考区块链浏览器内容
-- ----------------------------
DROP TABLE IF EXISTS `token_tx_list`;
CREATE TABLE `token_tx_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `token_address` varchar(42) DEFAULT NULL COMMENT '通证合约地址',
  `tx_hash` varchar(66) DEFAULT NULL COMMENT '交易HASH',
  `block_number` bigint(20) NOT NULL COMMENT '交易所在区块高度',
  `tx_time` datetime DEFAULT NULL COMMENT '交易时间',
  `from_address` varchar(42) DEFAULT NULL COMMENT '通证交易发起账户',
  `to_address` varchar(42) DEFAULT NULL COMMENT '通证交易接收账户',
  `amount` double DEFAULT NULL COMMENT '通证交易数量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_code` (`tx_hash`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='通证交易列表';

-- ----------------------------
-- Records of token_tx_list
-- ----------------------------
INSERT INTO `token_tx_list` VALUES ('1', '0xe6beb4e09e64c0f61ef639944ef97e0ca9a069a7', '0xa8cae136204e546d58fe9f81eb14e0e7321fc428395a49c2ef5c3c8123c273ca', '7655', '2020-05-27 22:43:45', '0x7312f4b8a4457a36827f185325fd6b66a3f8bb8b', '0xa8863fc8ce3816411378685223c03daae9770ebb','1.0');

-- ----------------------------
-- Table structure for user_income
-- 显示读者所阅读的作品全部收入
--
-- ???
-- ----------------------------
CREATE TABLE `user_income_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `book_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '作品ID,0表示全部作品',
  `token_address` varchar(42) DEFAULT NULL COMMENT '通证合约地址',
  `income_account` int(11) NOT NULL DEFAULT '0' COMMENT '订阅总额',
  `income_count` int(11) NOT NULL DEFAULT '0' COMMENT '订阅次数',
  `income_number` int(11) NOT NULL DEFAULT '0' COMMENT '订阅人数',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='稿费收入明细统计表';
