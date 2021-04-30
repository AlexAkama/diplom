ALTER TABLE `posts` ADD `like_count` int(11) NOT NULL DEFAULT 0;
ALTER TABLE `posts` ADD `dislike_count` int(11) NOT NULL DEFAULT 0;
ALTER TABLE `posts` ADD `comment_count` int(11) NOT NULL DEFAULT 0;

UPDATE `posts` p SET `like_count` =  (SELECT COUNT(IF(value = 1, 1, NULL)) FROM `post_votes` WHERE post_id = p.id);
UPDATE `posts` p SET `dislike_count` =  (SELECT COUNT(IF(value = -1, 1, NULL)) FROM `post_votes` WHERE post_id = p.id);
UPDATE `posts` p SET `comment_count` =  (SELECT COUNT(*) FROM `post_comments` WHERE post_id = p.id);