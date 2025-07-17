package com.gjj.nmcbackend.controller;

import com.gjj.nmcbackend.annotion.UserAuthCheck;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.exception.ThrowUtils;
import com.gjj.nmcbackend.model.entity.DrugReimbursement;
import com.gjj.nmcbackend.service.DrugReimbursementService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "药品报销比例管理", value = "药品报销比例相关操作")
@RestController
@RequestMapping("/drugReimbursement")
public class DrugReimbursementController {

    @Resource
    private DrugReimbursementService drugReimbursementService;

    @ApiOperation(value = "添加药品报销比例", notes = "添加一个新的药品报销比例信息")
    @ApiImplicitParam(name = "drugReimbursement", value = "药品报销比例信息", required = true, dataType = "DrugReimbursement")
    @UserAuthCheck
    @PostMapping("/add")
    public BaseResponse<Integer> addDrugReimbursement(@RequestBody DrugReimbursement drugReimbursement) {
        ThrowUtils.throwIf(drugReimbursement == null , ErrorCode.PARAMS_ERROR);
        drugReimbursementService.save(drugReimbursement);
        return ResultUtils.success(drugReimbursement.getId());
    }

    @ApiOperation(value = "更新药品报销比例", notes = "更新药品报销比例信息")
    @ApiImplicitParam(name = "drugReimbursement", value = "药品报销比例信息", required = true, dataType = "DrugReimbursement")
    @UserAuthCheck
    @PostMapping("/update")
    public BaseResponse<Boolean> updateDrugReimbursement(@RequestBody DrugReimbursement drugReimbursement) {
        ThrowUtils.throwIf(drugReimbursement == null , ErrorCode.PARAMS_ERROR);
        Boolean result = drugReimbursementService.updateById(drugReimbursement);
        return ResultUtils.success(result);
    }

    @ApiOperation(value = "获取所有药品报销比例", notes = "获取全部药品报销比例列表")
    @UserAuthCheck
    @GetMapping("/listAll")
    public BaseResponse<List<DrugReimbursement>> listAllReimbursement() {
        List<DrugReimbursement> list = drugReimbursementService.list();
        return ResultUtils.success(list);
    }

    @ApiOperation(value = "删除药品报销比例", notes = "根据ID删除药品报销比例信息")
    @ApiImplicitParam(name = "id", value = "药品报销比例ID", required = true, paramType = "query", dataType = "Integer")
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
