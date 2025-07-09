package com.gjj.nmcbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.nmcbackend.model.entity.DrugInfo;
import com.gjj.nmcbackend.service.DrugInfoService;
import com.gjj.nmcbackend.mapper.DrugInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 78568
* @description 针对表【drug_info(药品信息表)】的数据库操作Service实现
* @createDate 2025-07-09 17:05:08
*/
@Service
public class DrugInfoServiceImpl extends ServiceImpl<DrugInfoMapper, DrugInfo>
    implements DrugInfoService {

    @Override
    public List<DrugInfo> getDrugInfoByName(String drugName) {
        LambdaQueryWrapper<DrugInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(DrugInfo::getChinaName, drugName);
        return this.list(queryWrapper);
    }

    @Override
    public Page<DrugInfo> listDrugInfoByPage(long current, long size) {
        Page<DrugInfo> page = new Page<>(current, size);
        return this.page(page);
    }
}




