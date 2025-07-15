package com.gjj.nmcbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientDrugRequest;
import com.gjj.nmcbackend.model.entity.DrugInfo;
import com.gjj.nmcbackend.model.entity.InpatientDrug;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gjj.nmcbackend.model.vo.DrugCostVO;
import com.gjj.nmcbackend.model.vo.InpatientDrugVO;

/**
* @author 78568
* @description 针对表【inpatient_drug(患者医嘱药品信息表)】的数据库操作Service
* @createDate 2025-07-14 14:19:28
*/
public interface InpatientDrugService extends IService<InpatientDrug> {

    boolean addDrug(AddInpatientDrugRequest request);
    
    
    boolean checkDrugExists(Integer patientId, Integer drugId);

    Page<InpatientDrugVO> listInpatientDrugByPage(long current, long size, String drugName);

    boolean deleteByPatientAndDrug(Integer patientId , Integer drugId);

    /**
     * 根据患者ID分页查询药品费用
     */
    Page<DrugCostVO> listDrugCostByPatientId(Integer patientId, Integer current, Integer size);
}
