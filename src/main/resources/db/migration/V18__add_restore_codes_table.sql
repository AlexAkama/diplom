CREATE TABLE `restore_codes`
(
    `id`          int(11)                          NOT NULL AUTO_INCREMENT,
    `code`        tinytext COLLATE utf8_unicode_ci NOT NULL,
    `time`        datetime(6)                      NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;