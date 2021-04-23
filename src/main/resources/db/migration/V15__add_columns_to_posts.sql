ALTER TABLE `posts` ADD`like` int(11) NOT NULL DEFAULT 0;
ALTER TABLE `posts` ADD`dislike` int(11) NOT NULL DEFAULT 0;
ALTER TABLE `posts` ADD`comment` int(11) NOT NULL DEFAULT 0;

UPDATE `posts` p SET `like` =  (SELECT COUNT(IF(value = 1, 1, NULL)) FROM `post_votes` WHERE post_id = p.id);
UPDATE `posts` p SET `dislike` =  (SELECT COUNT(IF(value = -1, 1, NULL)) FROM `post_votes` WHERE post_id = p.id);
UPDATE `posts` p SET `comment` =  (SELECT COUNT(*) FROM `post_comments` WHERE post_id = p.id);