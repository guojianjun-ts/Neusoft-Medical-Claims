package com.gjj.nmcbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.model.entity.DrugInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 78568
* @description 针对表【drug_info(药品信息表)】的数据库操作Service
* @createDate 2025-07-09 17:05:08
*/
public interface DrugInfoService extends IService<DrugInfo> {

    /**
     * 根据药品名称获取药品信息
     *
     * @param chineseName 药品名称
     * @return 药品信息列表
     */
    public List<DrugInfo> getDrugInfoByName(String chineseName);

    /**
     * 分页获取药品信息
     * @param current 当前页
     * @param size 每页大小
     * @return 分页结果
     */
    Page<DrugInfo> listDrugInfoByPage(long current, long size);

    /**
     * 获取所有药品的ID列表
     * @return 药品ID列表
     */
    List<Long> listAllDrugInfoIds();

    /**
     * 批量删除药品信息
     * @param ids 药品ID列表
     * @return 是否删除成功
     */
    boolean deleteDrugInfoByIds(List<Long> ids);
}
