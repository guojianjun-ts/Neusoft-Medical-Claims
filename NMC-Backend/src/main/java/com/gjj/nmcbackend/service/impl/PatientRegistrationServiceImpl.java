package com.gjj.nmcbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.nmcbackend.model.entity.PatientRegistration;
import com.gjj.nmcbackend.model.vo.PatientVO;
import com.gjj.nmcbackend.service.PatientRegistrationService;
import com.gjj.nmcbackend.mapper.PatientRegistrationMapper;
import org.springframework.stereotype.Service;

/**
* @author 78568
* @description 针对表【patient_registration(患者信息表)】的数据库操作Service实现
* @createDate 2025-07-14 09:16:50
*/
import org.springframework.beans.BeanUtils;

@Service
public class PatientRegistrationServiceImpl extends ServiceImpl<PatientRegistrationMapper, PatientRegistration>
        implements PatientRegistrationService {

    @Override
    public Page<PatientVO> listPatientInfoByPage(long current, long size, String patientName) {
        // 1. 构建分页对象
        Page<PatientRegistration> page = new Page<>(current, size);

        // 2. 构建查询条件
        LambdaQueryWrapper<PatientRegistration> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(patientName)) {
            queryWrapper.like(PatientRegistration::getPatientName, patientName);
        }

        // 3. 执行分页查询
        Page<PatientRegistration> patientPage = this.page(page, queryWrapper);

        // 4. 使用 BeanUtils.copyProperties 转换对象
        return (Page<PatientVO>) patientPage.convert(patient -> {
            PatientVO patientVO = new PatientVO();
            BeanUtils.copyProperties(patient, patientVO);
            return patientVO;
        });
    }
}




