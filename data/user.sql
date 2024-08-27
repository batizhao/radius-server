CREATE DATABASE `radius` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

USE `radius`;

CREATE TABLE `ims_user`
(
    `id`          int          NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    varchar(64)  NOT NULL COMMENT '用户名',
    `password`    varchar(256) NOT NULL COMMENT '密码',
    PRIMARY KEY (`id`)
);

INSERT INTO `ims_user` (`username`, `password`)
VALUES
    (X'746F6D', X'2432612431302472466F4F7262574432702E31436A426F42715465614F55677078466D745A6B6E7344457646373841734D5876737855314179415A75');
