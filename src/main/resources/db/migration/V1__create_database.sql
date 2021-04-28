CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `is_moderator` integer(4) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `photo` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `reg_time` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `posts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `is_active` integer(4) NOT NULL,
  `moderation_status` varchar(8) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'NEW',
  `text` varchar(1024) COLLATE utf8_unicode_ci NOT NULL,
  `time` datetime(6) NOT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `view_count` int(11) NOT NULL,
  `moderator_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `post_moderator_fk` (`moderator_id`),
  KEY `post_user_fk` (`user_id`),
  CONSTRAINT `post_user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `post_moderator_fk` FOREIGN KEY (`moderator_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `post_comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `time` datetime(6) NOT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `post_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `comment_parent_fk` (`parent_id`),
  KEY `comment_post_fk` (`post_id`),
  KEY `comment_user_fk` (`user_id`),
  CONSTRAINT `comment_post_fk` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `comment_parent_fk` FOREIGN KEY (`parent_id`) REFERENCES `post_comments` (`id`),
  CONSTRAINT `comment_user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `post_votes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` datetime(6) NOT NULL,
  `value` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `vote_post_fk` (`post_id`),
  KEY `vote_user_fk` (`user_id`),
  CONSTRAINT `vote_post_fk` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `vote_user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `tag2post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `post_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `relation_tag_fk` (`post_id`),
  KEY `relation_post_fk` (`tag_id`),
  CONSTRAINT `relation_tag_fk` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`),
  CONSTRAINT `relation_post_fk` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `captcha_codes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` tinytext COLLATE utf8_unicode_ci NOT NULL,
  `secret_code` tinytext COLLATE utf8_unicode_ci NOT NULL,
  `time` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `global_settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `value` varchar(3) COLLATE utf8_unicode_ci DEFAULT 'NO',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;