package com.gjj.nmcbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.nmcbackend.model.entity.DiseaseInfo;
import com.gjj.nmcbackend.model.entity.DrugInfo;
import com.gjj.nmcbackend.service.DiseaseInfoService;
import com.gjj.nmcbackend.mapper.DiseaseInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 78568
* @description 针对表【disease_info(疾病信息表)】的数据库操作Service实现
* @createDate 2025-07-14 12:40:26
*/
@Service
public class DiseaseInfoServiceImpl extends ServiceImpl<DiseaseInfoMapper, DiseaseInfo>
    implements DiseaseInfoService {

    /**
     * 分页查询（支持搜索）
     * @param current 页码
     * @param size 页面大小
     * @param diseaseName 疾病名称
     * @return 数据页
     */
    @Override
    public Page<DiseaseInfo> listDiseaseInfoByPage(long current, long size, String diseaseName) {
        LambdaQueryWrapper<DiseaseInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(diseaseName)) {
            queryWrapper.like(DiseaseInfo::getDiseaseName,diseaseName );
        }
        return this.page(new Page<>(current, size), queryWrapper);
    }
}




