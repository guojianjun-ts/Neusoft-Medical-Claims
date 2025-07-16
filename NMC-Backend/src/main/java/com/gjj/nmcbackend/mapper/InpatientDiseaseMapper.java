package com.gjj.nmcbackend.mapper;

import com.gjj.nmcbackend.model.entity.InpatientDisease;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjj.nmcbackend.model.vo.PatientDiseaseVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 78568
* @description 针对表【inpatient_disease(患者诊断疾病表)】的数据库操作Mapper
* @createDate 2025-07-14 13:42:34
* @Entity com.gjj.nmcbackend.model.entity.InpatientDisease
*/
public interface InpatientDiseaseMapper extends BaseMapper<InpatientDisease> {

    @Select("SELECT id.id, di.diseaseName, id.diseaseType " +
            "FROM inpatient_disease id " +
            "JOIN disease_info di ON id.diseaseId = di.id " +
            "WHERE id.patientId = #{patientId} " +
            "LIMIT #{offset}, #{size}")
    List<PatientDiseaseVO> selectPatientDiseaseVO(
            @Param("patientId") Integer patientId,
            @Param("offset") Integer offset,
            @Param("size") Integer size);

    @Select("SELECT COUNT(*) FROM inpatient_disease WHERE patientId = #{patientId}")
    Long countPatientDisease(@Param("patientId") Integer patientId);
}



