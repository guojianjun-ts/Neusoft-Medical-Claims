package com.gjj.nmcbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.exception.ThrowUtils;
import com.gjj.nmcbackend.mapper.HospitalReimbursementMapper;
import com.gjj.nmcbackend.model.entity.HospitalReimbursement;
import com.gjj.nmcbackend.model.vo.HospitalReimbursementVO;
import com.gjj.nmcbackend.service.HospitalReimbursementService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HospitalReimbursementServiceImpl
        extends ServiceImpl<HospitalReimbursementMapper, HospitalReimbursement>
        implements HospitalReimbursementService {

    @Override
    public List<HospitalReimbursementVO> getVOByHospitalLevel(String hospitalLevel) {
        List<HospitalReimbursement> entities = this.list(new LambdaQueryWrapper<HospitalReimbursement>()
                .eq(HospitalReimbursement::getHospitalLevel, hospitalLevel)
                .orderByAsc(HospitalReimbursement::getMinPayLevel));

        return entities.stream()
                .map(HospitalReimbursementVO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean saveOrUpdateReimbursement(HospitalReimbursement entity) {
        // 业务校验示例
        ThrowUtils.throwIf(entity.getMinPayLevel()>= entity.getMaxPayLevel(), ErrorCode.PARAMS_ERROR,"起付线应小于等极线");
        return this.saveOrUpdate(entity);
    }
}