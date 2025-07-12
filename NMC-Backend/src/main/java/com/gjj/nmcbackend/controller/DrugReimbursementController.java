package com.gjj.nmcbackend.controller;


import com.gjj.nmcbackend.annotion.UserAuthCheck;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.exception.ThrowUtils;
import com.gjj.nmcbackend.model.entity.DrugReimbursement;
import com.gjj.nmcbackend.service.DrugReimbursementService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/drugReimbursement")
public class DrugReimbursementController {

    @Resource
    private DrugReimbursementService drugReimbursementService;

    @UserAuthCheck
    @PostMapping("/add")
    public BaseResponse<Integer> addDrugReimbursement(@RequestBody DrugReimbursement drugReimbursement) {
        ThrowUtils.throwIf(drugReimbursement == null , ErrorCode.PARAMS_ERROR);
        drugReimbursementService.save(drugReimbursement);
        return ResultUtils.success(drugReimbursement.getId());
    }

    @UserAuthCheck
    @PostMapping("/update")
    public BaseResponse<Boolean> updateDrugReimbursement(@RequestBody DrugReimbursement drugReimbursement) {
        ThrowUtils.throwIf(drugReimbursement == null , ErrorCode.PARAMS_ERROR);
        Boolean result = drugReimbursementService.updateById(drugReimbursement);
        return ResultUtils.success(result);
    }

    @GetMapping("/listAll")
    public BaseResponse<List<DrugReimbursement>> listAllReimbursement() {
        List<DrugReimbursement> list = drugReimbursementService.list();
        return ResultUtils.success(list);
    }

    @UserAuthCheck
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteDrugReimbursement(@RequestParam Integer id) {
        //1.校验参数
        ThrowUtils.throwIf(id == null , ErrorCode.PARAMS_ERROR);

        //2.判断是否存在
        DrugReimbursement drugReimbursement= drugReimbursementService.getById(id);
        ThrowUtils.throwIf(drugReimbursement == null , ErrorCode.NOT_FOUND_ERROR,"药品报销比例不存在");

        // 3. 删除药品报销比例
        boolean result = drugReimbursementService.removeById(id);
        return ResultUtils.success(result);
    }


}

