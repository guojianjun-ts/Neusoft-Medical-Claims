package com.gjj.nmcbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.nmcbackend.exception.BusinessException;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.mapper.InpatientDiseaseMapper;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientDiseaseRequest;
import com.gjj.nmcbackend.model.entity.InpatientDisease;
import com.gjj.nmcbackend.service.InpatientDiseaseService;
import org.springframework.stereotype.Service;

import java.util.Date;

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
}