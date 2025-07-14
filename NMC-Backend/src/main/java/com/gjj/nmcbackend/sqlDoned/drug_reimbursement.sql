CREATE TABLE IF NOT EXISTS `drug_reimbursement`
(
    `id`                          int NOT NULL AUTO_INCREMENT COMMENT '药品报销 ID',
    `drugReimbursementType`       varchar(20)  DEFAULT NULL COMMENT '药品报销级别',
    `drugReimbursementProportion` int          DEFAULT NULL COMMENT '药品报销比例',
    `drugReimbursementInfo`       varchar(255) DEFAULT NULL COMMENT '药品报销等级说明',
    `drugStatus`                  tinyint(1)   DEFAULT '0' COMMENT '状态：1-正常 0-禁用',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='药品报销等级表';

INSERT INTO `drug_reimbursement` VALUES ('1', '甲类', '100', '基本医疗范畴已全部覆盖，100％可以报销', '1');
INSERT INTO `drug_reimbursement` VALUES ('2', '乙类', '80', '基本医疗范畴未全部覆盖，一般需要个人自付部分', '1');
INSERT INTO `drug_reimbursement` VALUES ('3', '丙类', '10', '基本医疗未覆盖', '1');