CREATE TABLE IF NOT EXISTS `inpatient_medical` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `orderTime` datetime DEFAULT NULL COMMENT '开立时间',
  `doctorOrder` varchar(255) DEFAULT NULL COMMENT '医嘱内容',
  `useMethod` varchar(255) DEFAULT NULL COMMENT '用法详情说明',
  `medicalId` int DEFAULT NULL COMMENT '医疗服务表外键',
  `status` int DEFAULT NULL COMMENT '医嘱状态：1-正常执行 0-作废 2-停止',
  `patientId` int DEFAULT NULL COMMENT '患者id',
  PRIMARY KEY (`id`),
  KEY `idx_medicalId` (`medicalId`),
  KEY `idx_patientId` (`patientId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者医嘱医疗服务项目信息表';

INSERT INTO `inpatient_medical` VALUES ('1084837890', '2022-12-05 10:00:00', '222', '222', '2', '1', '1');