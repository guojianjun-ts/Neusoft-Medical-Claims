CREATE TABLE IF NOT EXISTS `hospital_reimbursement`
(
    `id`            int NOT NULL AUTO_INCREMENT COMMENT '医院报销 ID',
    `minPayLevel`   int          DEFAULT NULL COMMENT '医院报销起付金额',
    `maxPayLevel`   int          DEFAULT NULL COMMENT '医院报销等级线',
    `peopleType`    tinyint      DEFAULT NULL COMMENT '人员类别(1在职人员，0退休人员)',
    `payProportion` int          DEFAULT NULL COMMENT '医院报销比例',
    `hospitalLevel` varchar(255) DEFAULT NULL COMMENT '医院等级',
    `status`        tinyint      DEFAULT NULL COMMENT '状态：1-正常 0-禁用',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='医院报销等级表';

INSERT INTO `hospital_reimbursement` VALUES ('1', '1300', '30000', '1', '90', '一级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('2', '30000', '40000', '1', '95', '一级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('3', '40000', '100000', '1', '97', '一级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('4', '100000', '300000', '1', '85', '一级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('5', '1300', '30000', '1', '87', '二级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('6', '30000', '40000', '1', '92', '二级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('7', '40000', '100000', '1', '97', '二级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('8', '100000', '300000', '1', '85', '二级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('9', '1300', '30000', '1', '85', '三级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('10', '30000', '40000', '1', '90', '三级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('11', '40000', '100000', '1', '95', '三级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('12', '100000', '300000', '1', '85', '三级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('13', '1300', '30000', '0', '94', '一级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('14', '30000', '40000', '0', '97', '一级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('15', '40000', '100000', '0', '98', '一级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('16', '100000', '300000', '0', '85', '一级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('17', '1300', '30000', '0', '92', '二级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('18', '30000', '40000', '0', '95', '二级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('19', '40000', '100000', '0', '98', '二级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('20', '100000', '300000', '0', '85', '二级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('21', '1300', '30000', '0', '91', '三级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('22', '30000', '40000', '0', '94', '三级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('23', '40000', '100000', '0', '97', '三级医院', '1');
INSERT INTO `hospital_reimbursement` VALUES ('24', '100000', '300000', '0', '85', '三级医院', '1');