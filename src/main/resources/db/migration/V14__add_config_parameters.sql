CREATE TABLE `config`
(
    `id`    BIGINT(11)      COLLATE utf8_unicode_ci NOT NULL AUTO_INCREMENT,
    `name`  VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `value` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `config`(name, value) VALUES ('title','NUTRI MAM');
INSERT INTO `config`(name, value) VALUES ('subtitle','Детский нутрициолог');
INSERT INTO `config`(name, value) VALUES ('phone','+7 912 268 2724');
INSERT INTO `config`(name, value) VALUES ('email','maslova.nutrition@gmail.com');
INSERT INTO `config`(name, value) VALUES ('copyright','Екатерина Маслова');
INSERT INTO `config`(name, value) VALUES ('copyrightYear','2018');

INSERT INTO `config`(name, value) VALUES ('timeout', '10');