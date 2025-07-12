package com.gjj.nmcbackend.controller;

import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.model.entity.HospitalReimbursement;
import com.gjj.nmcbackend.model.vo.HospitalReimbursementVO;
import com.gjj.nmcbackend.service.HospitalReimbursementService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/hospital/reimburse")
public class HospitalReimbursementController {

    @Resource
    private HospitalReimbursementService reimbursementService;

    /**
     * 按医院等级查询
     */
    @GetMapping("/level/{hospitalLevel}")
    public BaseResponse<List<HospitalReimbursementVO>> getByLevel(
            @PathVariable String hospitalLevel) {
        return ResultUtils.success(
                reimbursementService.getVOByHospitalLevel(hospitalLevel));
    }

    /**
     * 新增报销比例
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> add(
            @RequestBody HospitalReimbursement reimbursement) {
        return ResultUtils.success(
                reimbursementService.saveOrUpdateReimbursement(reimbursement));
    }

    /**
     * 更新报销比例
     */
    @PutMapping("/update")
    public BaseResponse<Boolean> update(
            @RequestBody HospitalReimbursement reimbursement) {
        return ResultUtils.success(
                reimbursementService.saveOrUpdateReimbursement(reimbursement));
    }

    /**
     * 删除报销比例
     */
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> delete(@PathVariable Integer id) {
        return ResultUtils.success(reimbursementService.removeById(id));
    }
}