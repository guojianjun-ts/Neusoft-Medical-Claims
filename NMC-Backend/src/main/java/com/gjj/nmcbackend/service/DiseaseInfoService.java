package com.gjj.nmcbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.model.entity.DiseaseInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 78568
* @description 针对表【disease_info(疾病信息表)】的数据库操作Service
* @createDate 2025-07-14 12:40:26
*/
public interface DiseaseInfoService extends IService<DiseaseInfo> {

    Page<DiseaseInfo> listDiseaseInfoByPage(long current, long size, String diseaseName);
}
