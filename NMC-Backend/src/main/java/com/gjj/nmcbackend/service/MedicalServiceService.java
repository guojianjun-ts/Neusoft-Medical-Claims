package com.gjj.nmcbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.model.entity.MedicalService;
import com.gjj.nmcbackend.model.entity.MedicalService;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 78568
* @description 针对表【medical_service(医疗服务设施表)】的数据库操作Service
* @createDate 2025-07-09 22:20:21
*/
public interface MedicalServiceService extends IService<MedicalService> {
    /**
     * 根据药品名称获取药品信息
     *
     * @param chineseName 药品名称
     * @return 药品信息列表
     */
    public List<MedicalService> getMedicalServiceByName(String chineseName);

    /**
     * 分页获取药品信息
     * @param current 当前页
     * @param size 每页大小
     * @return 分页结果
     */
    Page<MedicalService> listMedicalServiceByPage(long current, long size,String serviceName);

    /**
     * 获取所有药品的ID列表
     * @return 药品ID列表
     */
    List<Integer> listAllMedicalServiceIds();

    /**
     * 批量删除药品信息
     * @param ids 药品ID列表
     * @return 是否删除成功
     */
    boolean deleteMedicalServiceByIds(List<Long> ids);
}
