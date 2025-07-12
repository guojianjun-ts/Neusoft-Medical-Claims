package com.gjj.nmcbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.nmcbackend.model.entity.MedicalService;
import com.gjj.nmcbackend.service.MedicalServiceService;
import com.gjj.nmcbackend.model.entity.MedicalService;
import com.gjj.nmcbackend.mapper.MedicalServiceMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 78568
* @description 针对表【medical_service(医疗服务设施表)】的数据库操作Service实现
* @createDate 2025-07-09 22:20:21
*/
@Service
public class MedicalServiceServiceImpl extends ServiceImpl<MedicalServiceMapper, MedicalService>
    implements MedicalServiceService {

    @Override
    public List<MedicalService> getMedicalServiceByName(String drugName) {
        LambdaQueryWrapper<MedicalService> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(MedicalService::getServiceName, drugName);
        return this.list(queryWrapper);
    }

    @Override
    public Page<MedicalService> listMedicalServiceByPage(long current, long size,String serviceName) {
        LambdaQueryWrapper<MedicalService> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(serviceName)) {
            queryWrapper.like(MedicalService::getServiceName, serviceName);
        }
        return this.page(new Page<>(current, size), queryWrapper);
    }

    @Override
    public List<Integer> listAllMedicalServiceIds() {
        return this.list().stream()
                .map(MedicalService::getId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteMedicalServiceByIds(List<Long> ids) {
        return this.removeByIds(ids);
    }
}




