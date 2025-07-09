CREATE TABLE IF NOT EXISTS `inpatient_drug`
(
    `id`          int NOT NULL AUTO_INCREMENT COMMENT '患者医嘱药品信息 ID',
    `startTime`   datetime     DEFAULT NULL COMMENT '起始时间',
    `endTime`     datetime     DEFAULT NULL COMMENT '结束时间',
    `doctorOrder` varchar(255) DEFAULT NULL COMMENT '医嘱内容',
    `orderNumber` int          DEFAULT NULL COMMENT '医嘱频次（次/天）',
    `useMethod`   varchar(255) DEFAULT NULL COMMENT '用法',
    `drugId`      int          DEFAULT NULL COMMENT '药品表外键',
    `patientId`   int          DEFAULT NULL COMMENT '患者表外键',
    `status`      int          DEFAULT NULL COMMENT '医嘱状态：1-正常执行 0-作废 2-停止',
    PRIMARY KEY (`id`),
    KEY `idx_drugId` (`drugId`),
    KEY `idx_patientId` (`patientId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='患者医嘱药品信息表';

INSERT INTO `inpatient_drug` VALUES ('-320221182', '2021-12-27 00:07:19', '2021-12-27 00:07:21', '3333', '1', '3333', '9633', '1', '1');
INSERT INTO `inpatient_drug` VALUES ('-148254719', '2021-12-27 00:07:19', '2021-12-27 00:07:21', '11111', '1', '1111', '9633', '1', '1');
INSERT INTO `inpatient_drug` VALUES ('1', '2022-11-01 16:07:49', '2022-11-04 16:07:54', '一日三次餐后口服', '3', '口服', '9656', '1', '1');
INSERT INTO `inpatient_drug` VALUES ('2', '2022-10-31 16:42:56', '2022-11-05 16:43:01', '一日三次餐后口服', '3', '口服', '11620', '1', '1');