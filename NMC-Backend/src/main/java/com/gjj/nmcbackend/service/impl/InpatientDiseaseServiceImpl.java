package com.gjj.nmcbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.nmcbackend.exception.BusinessException;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.mapper.InpatientDiseaseMapper;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientDiseaseRequest;
import com.gjj.nmcbackend.model.entity.DiseaseInfo;
import com.gjj.nmcbackend.model.entity.InpatientDisease;
import com.gjj.nmcbackend.model.vo.PatientDiseaseVO;
import com.gjj.nmcbackend.service.DiseaseInfoService;
import com.gjj.nmcbackend.service.InpatientDiseaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InpatientDiseaseServiceImpl extends ServiceImpl<InpatientDiseaseMapper, InpatientDisease>
        implements InpatientDiseaseService {

    @Override
    public Page<InpatientDisease> listDiseaseByPage(long current, long size, Integer patientId, Integer diseaseType) {
        LambdaQueryWrapper<InpatientDisease> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InpatientDisease::getPatientId, patientId);

        if (diseaseType != null) {
            queryWrapper.eq(InpatientDisease::getDiseaseType, diseaseType);
        }

        queryWrapper.orderByDesc(InpatientDisease::getOrderTime);

        return this.page(new Page<>(current, size), queryWrapper);
    }

    @Override
    public boolean addDisease(AddInpatientDiseaseRequest request) {
        // 检查是否已存在相同记录
        if (checkDiseaseExists(request.getPatientId(), request.getDiseaseId(), request.getDiseaseType())) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR , "已添加该数据");
        }

        InpatientDisease record = new InpatientDisease();
        record.setPatientId(request.getPatientId());
        record.setDiseaseId(request.getDiseaseId());
        record.setDiseaseType(request.getDiseaseType());
        record.setOrderTime(new Date());

        return this.save(record);
    }

    @Override
    public boolean checkDiseaseExists(Integer patientId, Integer diseaseId, Integer diseaseType) {
        LambdaQueryWrapper<InpatientDisease> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InpatientDisease::getPatientId, patientId)
                .eq(InpatientDisease::getDiseaseId, diseaseId);

        if (diseaseType != null) {
            queryWrapper.eq(InpatientDisease::getDiseaseType, diseaseType);
        }

        return this.count(queryWrapper) > 0;
    }

    @Override
    public Page<PatientDiseaseVO> listPatientDisease(Integer patientId, Integer current, Integer size) {
        // 1. 创建分页对象
        Page<PatientDiseaseVO> page = new Page<>(current, size);

        // 2. 执行原生SQL查询（避免N+1问题）
        List<PatientDiseaseVO> records = baseMapper.selectPatientDiseaseVO(
                patientId, (current - 1) * size, size);

        // 3. 查询总数
        Long total = baseMapper.countPatientDisease(patientId);

        // 4. 设置分页结果
        page.setRecords(records);
        page.setTotal(total);
        return page;
    }

}