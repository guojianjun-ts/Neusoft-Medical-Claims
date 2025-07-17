package com.gjj.nmcbackend.controller;

import com.gjj.nmcbackend.annotion.UserAuthCheck;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.model.entity.HospitalReimbursement;
import com.gjj.nmcbackend.model.vo.HospitalReimbursementVO;
import com.gjj.nmcbackend.service.HospitalReimbursementService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "医院报销比例管理", value = "医院报销比例相关操作")
@RestController
@RequestMapping("/api/hospital/reimburse")
public class HospitalReimbursementController {

    @Resource
    private HospitalReimbursementService reimbursementService;

    /**
     * 按医院等级查询
     */
    @ApiOperation(value = "按医院等级查询报销比例", notes = "根据医院等级查询对应的报销比例信息")
    @ApiImplicitParam(name = "hospitalLevel", value = "医院等级", required = true, paramType = "path", dataType = "String")
    @UserAuthCheck
    @GetMapping("/level/{hospitalLevel}")
    public BaseResponse<List<HospitalReimbursementVO>> getByLevel(
            @PathVariable String hospitalLevel) {
        return ResultUtils.success(
                reimbursementService.getVOByHospitalLevel(hospitalLevel));
    }

    /**
     * 新增报销比例
     */
    @ApiOperation(value = "新增医院报销比例", notes = "添加新的医院报销比例信息")
    @ApiImplicitParam(name = "reimbursement", value = "医院报销比例信息", required = true, dataType = "HospitalReimbursement")
    @UserAuthCheck
    @PostMapping("/add")
    public BaseResponse<Boolean> add(
            @RequestBody HospitalReimbursement reimbursement) {
        return ResultUtils.success(
                reimbursementService.saveOrUpdateReimbursement(reimbursement));
    }

    /**
     * 更新报销比例
     */
    @ApiOperation(value = "更新医院报销比例", notes = "更新已有的医院报销比例信息")
    @ApiImplicitParam(name = "reimbursement", value = "医院报销比例信息", required = true, dataType = "HospitalReimbursement")
    @UserAuthCheck
    @PutMapping("/update")
    public BaseResponse<Boolean> update(
            @RequestBody HospitalReimbursement reimbursement) {
        return ResultUtils.success(
                reimbursementService.saveOrUpdateReimbursement(reimbursement));
    }

    /**
     * 删除报销比例
     */
    @ApiOperation(value = "删除医院报销比例", notes = "根据ID删除医院报销比例信息")
    @ApiImplicitParam(name = "id", value = "医院报销比例ID", required = true, paramType = "path", dataType = "Integer")
    @UserAuthCheck
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> delete(@PathVariable Integer id) {
        return ResultUtils.success(reimbursementService.removeById(id));
    }
}