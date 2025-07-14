CREATE TABLE IF NOT EXISTS `inpatient_diagnosis` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '患者医嘱诊疗项目 ID',
  `orderTime` datetime DEFAULT NULL COMMENT '开立时间',
  `doctorOrder` varchar(255) DEFAULT NULL COMMENT '医生医嘱诊疗项目信息',
  `useMethod` varchar(255) DEFAULT NULL COMMENT '用法详情说明',
  `diagnosisId` int DEFAULT NULL COMMENT '诊疗项目表外键',
  `status` int DEFAULT NULL COMMENT '医嘱状态：1-正常执行 0-作废 2-停止',
  `patientId` int DEFAULT NULL COMMENT '患者表外键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者医嘱诊疗项目信息表';

INSERT INTO `inpatient_diagnosis` VALUES ('715665409', '2022-12-06 10:00:00', '222', '222', '299', '1', '1');