package com.gjj.nmcbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.model.entity.DiagnosisTreatment;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;

import java.util.List;

/**
* @author 78568
* @description 针对表【diagnosis_treatment(诊疗项目表)】的数据库操作Service
* @createDate 2025-07-09 22:17:31
*/
public interface DiagnosisTreatmentService extends IService<DiagnosisTreatment> {
    /**
     * 根据诊疗项目名称获取诊疗项目信息
     *
     * @param chineseName 诊疗项目名称
     * @return 诊疗项目信息列表
     */
    public List<DiagnosisTreatment> getDiagnosisTreatmentByName(String chineseName);

    /**
     * 分页获取诊疗项目信息
     * @param current 当前页
     * @param size 每页大小
     * @return 分页结果
     */
    Page<DiagnosisTreatment> listDiagnosisTreatmentByPage(long current, long size);

    /**
     * 获取所有诊疗项目的ID列表
     * @return 诊疗项目ID列表
     */
    List<Integer> listAllDiagnosisTreatmentIds();

    /**
     * 批量删除诊疗项目信息
     * @param ids 诊疗项目ID列表
     * @return 是否删除成功
     */
    boolean deleteDiagnosisTreatmentByIds(List<Integer> ids);
}
