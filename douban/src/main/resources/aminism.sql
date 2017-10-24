CREATE DATABASE `aminism` /*!40100 DEFAULT CHARACTER SET utf8 */;
CREATE TABLE `doubanactivity` (
  `activityid` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `inserttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`activityid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `doubanbook` (
  `bookid` bigint(20) NOT NULL COMMENT 'doubanbookid',
  `bookname` varchar(255) NOT NULL,
  `author` varchar(255) DEFAULT NULL,
  `publisher` varchar(255) DEFAULT NULL,
  `publishdate` date DEFAULT NULL,
  `pageno` int(11) DEFAULT NULL COMMENT '装帧',
  `binding` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `ISBN` bigint(20) DEFAULT NULL,
  `bookintro` mediumtext,
  `faceimg` varchar(255) DEFAULT NULL COMMENT '封面图像',
  `authorintro` mediumtext,
  `menu` mediumtext,
  `sample` mediumtext,
  `tagids` varchar(255) DEFAULT NULL,
  `douban_IBCF` mediumtext,
  `douban_UBCF` mediumtext,
  `rate` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `extention` mediumtext,
  `inserttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`bookid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `doubanbook_comment` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) NOT NULL,
  `doubanuserid` varchar(50) NOT NULL,
  `bookid` bigint(20) NOT NULL,
  `rate` smallint(5) NOT NULL,
  `comment` mediumtext NOT NULL,
  `follownum` int(11) DEFAULT '0',
  `ratedate` date DEFAULT NULL,
  `inserttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1034 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `doubanbook_offer` (
  `id` bigint(20) NOT NULL,
  `userid` bigint(20) NOT NULL,
  `bookid` bigint(20) NOT NULL,
  `state` varchar(255) DEFAULT NULL COMMENT '状态，借，卖',
  `mark` varchar(255) DEFAULT NULL,
  `offerdate` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `city` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `inserttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `doubanbook_review` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) NOT NULL,
  `doubanuserid` varchar(50) NOT NULL,
  `bookid` bigint(20) NOT NULL,
  `rate` smallint(5) NOT NULL,
  `review` mediumtext,
  `follownum` int(11) DEFAULT '0',
  `disfollownum` int(11) DEFAULT NULL,
  `review_recusers` varchar(255) DEFAULT NULL,
  `review_likeuser` varchar(255) DEFAULT NULL COMMENT 'userids',
  `url` varchar(255) DEFAULT NULL,
  `ratedate` datetime DEFAULT NULL,
  `inserttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=242 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `doubanbook_review_comment` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) NOT NULL,
  `doubanuserid` varchar(50) NOT NULL,
  `bookid` bigint(20) NOT NULL,
  `reviewid` bigint(20) NOT NULL,
  `comment` mediumtext NOT NULL,
  `replyid` mediumtext,
  `ratedate` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `inserttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1251 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `doubangroup` (
  `groupid` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `inserttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `doubanmovie` (
  `movieid` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `inserttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`movieid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `doubanmusic` (
  `musicid` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `inserttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`musicid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `doubanrate` (
  `doubanbookid` bigint(20) NOT NULL,
  `userid` bigint(20) NOT NULL,
  `doubanuserid` bigint(20) NOT NULL,
  `rate` smallint(5) NOT NULL,
  `inserttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`doubanbookid`,`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Log` (
  `idLog` int(11) NOT NULL AUTO_INCREMENT,
  `message` varchar(2048) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `insertime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idLog`)
) ENGINE=InnoDB AUTO_INCREMENT=1794 DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `userid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `doubanuserid` varchar(50) NOT NULL,
  `uname` varchar(255) NOT NULL,
  `avatar` varchar(255) NOT NULL COMMENT '头像',
  `sentence` varchar(255) DEFAULT NULL COMMENT '个性签名',
  `city` varchar(255) DEFAULT NULL,
  `groups` varchar(255) DEFAULT NULL COMMENT '豆瓣小组',
  `attentionTo` varchar(255) DEFAULT NULL COMMENT '关注谁',
  `Toattention` varchar(255) DEFAULT NULL COMMENT '被谁关注',
  `book_read` varchar(255) DEFAULT '0' COMMENT 'bookids读过',
  `book_plan` varchar(255) DEFAULT '0' COMMENT 'bookids想读',
  `music_listen` varchar(255) DEFAULT NULL,
  `music_plan` varchar(255) DEFAULT NULL,
  `movie_watch` varchar(255) DEFAULT NULL,
  `movie_plan` varchar(255) DEFAULT NULL,
  `activity_online` varchar(255) DEFAULT NULL,
  `activity_offline` varchar(255) DEFAULT NULL,
  `resgistdate` date DEFAULT NULL,
  `flag` int(11) DEFAULT '0' COMMENT '0:没有更新详细数据，1：更新书籍，2：更新音乐，3：更新电影，12,13,23,',
  `extention` text,
  `inserttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userid`),
  KEY `duid` (`doubanuserid`)
) ENGINE=InnoDB AUTO_INCREMENT=3909 DEFAULT CHARSET=utf8;
