CREATE TABLE IF NOT EXISTS `inpatient_disease` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '患者疾病 ID',
  `patientId` int DEFAULT NULL COMMENT '住院患者表外键',
  `diseaseId` int DEFAULT NULL COMMENT '疾病表外键',
  `orderTime` datetime DEFAULT NULL COMMENT '疾病诊断时间',
  `diseaseType` tinyint DEFAULT NULL COMMENT '诊断类型（1、入院诊断，2、主要诊断，3、其他诊断）',
  PRIMARY KEY (`id`),
  KEY `idx_patientId` (`patientId`),
  KEY `idx_diseaseId` (`diseaseId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者诊断疾病表';

INSERT INTO `inpatient_disease` VALUES ('-1691795455', '1', '12', '2022-12-24 21:00:58', '2');
INSERT INTO `inpatient_disease` VALUES ('2', '1', '4', '2022-10-31 16:14:12', '1');
INSERT INTO `inpatient_disease` VALUES ('3', '2', '5', '2022-10-31 16:14:24', '1');
INSERT INTO `inpatient_disease` VALUES ('4', '2', '6', '2022-10-31 16:14:35', '2');
INSERT INTO `inpatient_disease` VALUES ('6', '1', '11', '2022-11-01 14:43:19', '1');
INSERT INTO `inpatient_disease` VALUES ('770260993', '1', '33', '2022-12-24 21:01:19', '3');
INSERT INTO `inpatient_disease` VALUES ('1932083202', '1', '9', '2022-12-24 19:56:22', '1');