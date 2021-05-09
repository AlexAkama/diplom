ALTER TABLE `tags` ADD `active` int(4) NOT NULL DEFAULT 0;

UPDATE `tags` t SET `active` = 1 WHERE id <= 7;
UPDATE `tags` t SET `active` = 0 WHERE id > 7;