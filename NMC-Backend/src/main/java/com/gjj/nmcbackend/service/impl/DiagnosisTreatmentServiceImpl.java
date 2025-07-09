package com.gjj.nmcbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.nmcbackend.model.entity.DiagnosisTreatment;
import com.gjj.nmcbackend.service.DiagnosisTreatmentService;
import com.gjj.nmcbackend.mapper.DiagnosisTreatmentMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 78568
* @description 针对表【diagnosis_treatment(诊疗项目表)】的数据库操作Service实现
* @createDate 2025-07-09 22:17:31
*/
@Service
public class DiagnosisTreatmentServiceImpl extends ServiceImpl<DiagnosisTreatmentMapper, DiagnosisTreatment>
    implements DiagnosisTreatmentService {
    @Override
    public List<DiagnosisTreatment> getDiagnosisTreatmentByName(String treatmentName) {
        LambdaQueryWrapper<DiagnosisTreatment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(DiagnosisTreatment::getTreatmentName, treatmentName);
        return this.list(queryWrapper);
    }

    @Override
    public Page<DiagnosisTreatment> listDiagnosisTreatmentByPage(long current, long size) {
        Page<DiagnosisTreatment> page = new Page<>(current, size);
        return this.page(page);
    }

    @Override
    public List<Integer> listAllDiagnosisTreatmentIds() {
        return this.list().stream()
                .map(DiagnosisTreatment::getId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteDiagnosisTreatmentByIds(List<Integer> ids) {
        return this.removeByIds(ids);
    }
}




