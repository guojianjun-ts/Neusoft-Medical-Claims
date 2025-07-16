package com.gjj.nmcbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.nmcbackend.exception.BusinessException;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.mapper.MedicalServiceMapper;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientMedicalRequest;
import com.gjj.nmcbackend.model.entity.MedicalService;
import com.gjj.nmcbackend.model.entity.InpatientMedical;
import com.gjj.nmcbackend.model.entity.InpatientMedical;
import com.gjj.nmcbackend.model.vo.DiagnosisCostVO;
import com.gjj.nmcbackend.model.vo.InpatientMedicalVO;
import com.gjj.nmcbackend.model.vo.MedicalServiceCostVO;
import com.gjj.nmcbackend.service.MedicalServiceService;
import com.gjj.nmcbackend.service.InpatientMedicalService;
import com.gjj.nmcbackend.mapper.InpatientMedicalMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 78568
 * @description 针对表【inpatient_medical(患者医嘱药品信息表)】的数据库操作Service实现
 * @createDate 2025-07-14 14:19:27
 */
@Service
public class InpatientMedicalServiceImpl extends ServiceImpl<InpatientMedicalMapper, InpatientMedical> implements InpatientMedicalService {


    @Resource
    private MedicalServiceService medicalInfoService;

    @Resource
    private MedicalServiceMapper medicalServiceMapper;


    @Override
    public boolean addMedical(AddInpatientMedicalRequest request) {
        // 检查是否已存在相同记录
        if (checkMedicalExists(request.getPatientId() , request.getMedicalId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR , "已添加该数据");
        }
        InpatientMedical inpatientMedical = new InpatientMedical();
        // 使用BeanUtils拷贝属性
        BeanUtils.copyProperties(request , inpatientMedical);

        return this.save(inpatientMedical);
    }

    @Override
    public boolean checkMedicalExists(Integer patientId , Integer medicalId) {
        LambdaQueryWrapper<InpatientMedical> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InpatientMedical::getPatientId , patientId).eq(InpatientMedical::getMedicalId , medicalId);

        return this.count(queryWrapper) > 0;
    }

    @Override
    public Page<InpatientMedicalVO> listInpatientMedicalByPage(long current , long size , String medicalName) {

        //1.先查询 MedicalService 原实体类的分页数据
        LambdaQueryWrapper<MedicalService> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(medicalName)) {
            queryWrapper.like(MedicalService::getServiceName , medicalName);
        }
        Page<MedicalService> medicalInfoPage = medicalInfoService.page(new Page<>(current , size) , queryWrapper);


        //2.开始转化为 InpatientMedicalVO 类型的分页数据
        Page<InpatientMedicalVO> inpatientMedicalVOPage = new Page<>(current , size);
        BeanUtils.copyProperties(medicalInfoPage , inpatientMedicalVOPage);

        //3.转换 records（List<InpatientMedical> → List<InpatientMedicalVO>）
        List<InpatientMedicalVO> voRecords = medicalInfoPage.getRecords().stream().map(medicalInfo -> {
            InpatientMedicalVO inpatientMedicalVO = new InpatientMedicalVO();
            BeanUtils.copyProperties(medicalInfo , inpatientMedicalVO);
            return inpatientMedicalVO;
        }).collect(Collectors.toList());
        inpatientMedicalVOPage.setRecords(voRecords);
        return inpatientMedicalVOPage;
    }

    @Override
    public boolean deleteByPatientAndMedical(Integer patientId, Integer medicalId) {
        LambdaQueryWrapper<InpatientMedical> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InpatientMedical::getPatientId, patientId)
                .eq(InpatientMedical::getMedicalId, medicalId);

        return this.remove(queryWrapper);
    }

    @Override
    public Page<MedicalServiceCostVO> listMedicalServiceCostByPatientId(Integer patientId, Integer current, Integer size) {
        // 1. 查询患者医疗服务记录
        QueryWrapper<InpatientMedical> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patientId", patientId)
                .eq("status", 1); // 只查询正常执行的医嘱

        Page<InpatientMedical> inpatientMedicalPage = this.page(new Page<>(current, size), queryWrapper);

        // 2. 获取医疗服务ID列表
        List<Integer> medicalIds = inpatientMedicalPage.getRecords().stream()
                .map(InpatientMedical::getMedicalId)
                .collect(Collectors.toList());

        if (medicalIds.isEmpty()) {
            return new Page<>(current, size, 0);
        }

        // 3. 批量查询医疗服务信息
        QueryWrapper<MedicalService> medicalQueryWrapper = new QueryWrapper<>();
        medicalQueryWrapper.in("id", medicalIds);
        List<MedicalService> medicalServices = medicalServiceMapper.selectList(medicalQueryWrapper);

        // 4. 构建医疗服务信息映射表
        Map<Integer, MedicalService> medicalServiceMap = medicalServices.stream()
                .collect(Collectors.toMap(MedicalService::getId, Function.identity()));

        // 5. 构建返回结果
        List<MedicalServiceCostVO> medicalServiceCostVOs = new ArrayList<>();
        for (InpatientMedical inpatientMedical : inpatientMedicalPage.getRecords()) {
            MedicalService service = medicalServiceMap.get(inpatientMedical.getMedicalId());
            if (service != null) {
                MedicalServiceCostVO medicalServiceCostVO = new MedicalServiceCostVO();
                // 设置医疗服务ID
                medicalServiceCostVO.setMedicalId(service.getId());
                // 复制医疗服务信息
                medicalServiceCostVO.setServiceName(service.getServiceName());
                medicalServiceCostVO.setServiceCode(service.getServiceNumber());
                medicalServiceCostVO.setExcludeContent(service.getServiceExclude());
                medicalServiceCostVO.setUnit(service.getServiceUnit());
                medicalServiceCostVO.setPrice(service.getServicePrice());
                medicalServiceCostVO.setExcludeContent(service.getServiceExclude());

                medicalServiceCostVOs.add(medicalServiceCostVO);
            }
        }

        // 6. 构建分页结果
        Page<MedicalServiceCostVO> resultPage = new Page<>(
                inpatientMedicalPage.getCurrent(),
                inpatientMedicalPage.getSize(),
                inpatientMedicalPage.getTotal()
        );
        resultPage.setRecords(medicalServiceCostVOs);

        return resultPage;
    }
}




