package com.gjj.nmcbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.nmcbackend.exception.BusinessException;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientDrugRequest;
import com.gjj.nmcbackend.model.entity.DrugInfo;
import com.gjj.nmcbackend.model.entity.InpatientDrug;
import com.gjj.nmcbackend.model.entity.InpatientDrug;
import com.gjj.nmcbackend.model.vo.InpatientDrugVO;
import com.gjj.nmcbackend.service.DrugInfoService;
import com.gjj.nmcbackend.service.InpatientDrugService;
import com.gjj.nmcbackend.mapper.InpatientDrugMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 78568
 * @description 针对表【inpatient_drug(患者医嘱药品信息表)】的数据库操作Service实现
 * @createDate 2025-07-14 14:19:27
 */
@Service
public class InpatientDrugServiceImpl extends ServiceImpl<InpatientDrugMapper, InpatientDrug> implements InpatientDrugService {


    @Resource
    private DrugInfoService drugInfoService;

    @Override
    public boolean addDrug(AddInpatientDrugRequest request) {
        // 检查是否已存在相同记录
        if (checkDrugExists(request.getPatientId() , request.getDrugId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR , "已添加该数据");
        }
        InpatientDrug inpatientDrug = new InpatientDrug();
        // 使用BeanUtils拷贝属性
        BeanUtils.copyProperties(request , inpatientDrug);

        return this.save(inpatientDrug);
    }

    @Override
    public boolean checkDrugExists(Integer patientId , Integer drugId) {
        LambdaQueryWrapper<InpatientDrug> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InpatientDrug::getPatientId , patientId).eq(InpatientDrug::getDrugId , drugId);

        return this.count(queryWrapper) > 0;
    }

    @Override
    public Page<InpatientDrugVO> listInpatientDrugByPage(long current , long size , String drugName) {

        //1.先查询 DrugInfo 原实体类的分页数据
        LambdaQueryWrapper<DrugInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(drugName)) {
            queryWrapper.like(DrugInfo::getChinaName , drugName);
        }
        Page<DrugInfo> drugInfoPage = drugInfoService.page(new Page<>(current , size) , queryWrapper);


        //2.开始转化为 InpatientDrugVO 类型的分页数据
        Page<InpatientDrugVO> inpatientDrugVOPage = new Page<>(current , size);
        BeanUtils.copyProperties(drugInfoPage , inpatientDrugVOPage);

        //3.转换 records（List<InpatientDrug> → List<InpatientDrugVO>）
        List<InpatientDrugVO> voRecords = drugInfoPage.getRecords().stream().map(drugInfo -> {
            InpatientDrugVO inpatientDrugVO = new InpatientDrugVO();
            BeanUtils.copyProperties(drugInfo , inpatientDrugVO);
            return inpatientDrugVO;
        }).collect(Collectors.toList());
        inpatientDrugVOPage.setRecords(voRecords);
        return inpatientDrugVOPage;
    }

    @Override
    public boolean deleteByPatientAndDrug(Integer patientId , Integer drugId) {
        LambdaQueryWrapper<InpatientDrug> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InpatientDrug::getPatientId, patientId)
                .eq(InpatientDrug::getDrugId, drugId);

        return this.remove(queryWrapper);
    }
}




