CREATE TABLE IF NOT EXISTS `patient_registration`
(
    `id`          int NOT NULL AUTO_INCREMENT COMMENT '患者 ID',
    `caseNumber`  varchar(20)  DEFAULT NULL COMMENT '住院号',
    `patientName` varchar(50)  DEFAULT NULL COMMENT '患者姓名',
    `gender`      char(2)      DEFAULT NULL COMMENT '性别',
    `cardNumber`  varchar(20)  DEFAULT NULL COMMENT '身份证号',
    `birthday`    date         DEFAULT NULL COMMENT '出生日期',
    `page`        int          DEFAULT NULL COMMENT '年龄',
    `ageType`     char(2)      DEFAULT NULL COMMENT '年龄类型（岁、月、日）',
    `homeAddress` varchar(255) DEFAULT NULL COMMENT '住址',
    `visitDate`   datetime     DEFAULT NULL COMMENT '入院日期',
    `paymentType` varchar(20)  DEFAULT NULL COMMENT '结算类别（自费、医保）',
    `workStatus`  varchar(20)  DEFAULT NULL COMMENT '工作状态（在职、退休）',
    PRIMARY KEY (`id`),
    KEY `idx_realName` (`patientName`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='患者信息表';

INSERT INTO `patient_registration` VALUES ('1', '1000001', '夏落兴', '男', '123123200007011234', '2000-06-30', '22', '岁', '沈阳市', '2022-10-28 10:10:33', '医保', '在职');
INSERT INTO `patient_registration` VALUES ('2', '1000002', '李竞辰', '男', '123123200007011234', '2000-06-04', '22', '岁', '河北省秦皇岛市', '2022-10-31 11:53:03', '医保', '退休');
INSERT INTO `patient_registration` VALUES ('3', '1000003', '郝建国', '男', '123123200007011234', '2020-12-30', '22', '岁', '辽宁', '2022-12-22 10:03:02', '医保', '在职');