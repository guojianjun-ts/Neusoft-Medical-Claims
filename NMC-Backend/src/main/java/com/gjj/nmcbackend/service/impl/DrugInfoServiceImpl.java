package com.gjj.nmcbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.nmcbackend.model.entity.DrugInfo;
import com.gjj.nmcbackend.service.DrugInfoService;
import com.gjj.nmcbackend.mapper.DrugInfoMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    // Service层修改
    @Override
    public Page<DrugInfo> listDrugInfoByPage(long current, long size, String chinaName) {
        LambdaQueryWrapper<DrugInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(chinaName)) {
            queryWrapper.like(DrugInfo::getChinaName, chinaName);
        }
        return this.page(new Page<>(current, size), queryWrapper);
    }

    @Override
    public List<Long> listAllDrugInfoIds() {
        return this.list().stream()
                .map(DrugInfo::getId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteDrugInfoByIds(List<Long> ids) {
        return this.removeByIds(ids);
    }
}




