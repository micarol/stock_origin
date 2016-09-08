CREATE TABLE `alarm_mapping` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `stock_code` int(11) NOT NULL,
  `keyword` int(11) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0-del;1-in use;',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_u_c_k` (`user_id`,`stock_code`,`keyword`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `stock_code` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `code` int(11) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `psw` varchar(32) NOT NULL DEFAULT '',
  `email` varchar(64) NOT NULL DEFAULT '',
  `level` tinyint(4) NOT NULL DEFAULT '0' COMMENT '权限等级',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;