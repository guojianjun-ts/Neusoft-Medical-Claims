package com.gjj.nmcbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.nmcbackend.exception.BusinessException;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientDiagnosisRequest;
import com.gjj.nmcbackend.model.entity.DiagnosisTreatment;
import com.gjj.nmcbackend.model.entity.InpatientDiagnosis;
import com.gjj.nmcbackend.model.entity.InpatientDiagnosis;
import com.gjj.nmcbackend.model.vo.DiagnosisCostVO;
import com.gjj.nmcbackend.model.vo.InpatientDiagnosisVO;
import com.gjj.nmcbackend.service.DiagnosisTreatmentService;
import com.gjj.nmcbackend.service.InpatientDiagnosisService;
import com.gjj.nmcbackend.mapper.InpatientDiagnosisMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 78568
 * @description 针对表【inpatient_diagnosis(患者医嘱药品信息表)】的数据库操作Service实现
 * @createDate 2025-07-14 14:19:27
 */
@Service
public class InpatientDiagnosisServiceImpl extends ServiceImpl<InpatientDiagnosisMapper, InpatientDiagnosis> implements InpatientDiagnosisService {


    @Resource
    private DiagnosisTreatmentService diagnosisInfoService;

    @Override
    public boolean addDiagnosis(AddInpatientDiagnosisRequest request) {
        // 检查是否已存在相同记录
        if (checkDiagnosisExists(request.getPatientId() , request.getDiagnosisId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR , "已添加该数据");
        }
        InpatientDiagnosis inpatientDiagnosis = new InpatientDiagnosis();
        // 使用BeanUtils拷贝属性
        BeanUtils.copyProperties(request , inpatientDiagnosis);

        return this.save(inpatientDiagnosis);
    }

    @Override
    public boolean checkDiagnosisExists(Integer patientId , Integer diagnosisId) {
        LambdaQueryWrapper<InpatientDiagnosis> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InpatientDiagnosis::getPatientId , patientId).eq(InpatientDiagnosis::getDiagnosisId , diagnosisId);

        return this.count(queryWrapper) > 0;
    }

    @Override
    public Page<InpatientDiagnosisVO> listInpatientDiagnosisByPage(long current , long size , String diagnosisName) {
        //1.先查询 DiagnosisTreatment 原实体类的分页数据
        LambdaQueryWrapper<DiagnosisTreatment> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(diagnosisName)) {
            queryWrapper.like(DiagnosisTreatment::getTreatmentName , diagnosisName);
        }
        Page<DiagnosisTreatment> diagnosisInfoPage = diagnosisInfoService.page(new Page<>(current , size) , queryWrapper);


        //2.开始转化为 InpatientDiagnosisVO 类型的分页数据
        Page<InpatientDiagnosisVO> inpatientDiagnosisVOPage = new Page<>(current , size);
        BeanUtils.copyProperties(diagnosisInfoPage , inpatientDiagnosisVOPage);

        //3.转换 records（List<InpatientDiagnosis> → List<InpatientDiagnosisVO>）
        List<InpatientDiagnosisVO> voRecords = diagnosisInfoPage.getRecords().stream().map(diagnosisInfo -> {
            InpatientDiagnosisVO inpatientDiagnosisVO = new InpatientDiagnosisVO();
            BeanUtils.copyProperties(diagnosisInfo , inpatientDiagnosisVO);
            return inpatientDiagnosisVO;
        }).collect(Collectors.toList());
        inpatientDiagnosisVOPage.setRecords(voRecords);
        return inpatientDiagnosisVOPage;
    }

    @Override
    public boolean deleteByPatientAndDiagnosis(Integer patientId , Integer diagnosisId) {
        LambdaQueryWrapper<InpatientDiagnosis> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InpatientDiagnosis::getPatientId, patientId)
                .eq(InpatientDiagnosis::getDiagnosisId, diagnosisId);

        return this.remove(queryWrapper);
    }

    @Override
    public Page<DiagnosisCostVO> listDiagnosisCostByPatientId(Integer patientId , Integer current , Integer size) {
        return null;
    }
}




