package com.gjj.nmcbackend.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.annotion.UserAuthCheck;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.exception.ThrowUtils;
import com.gjj.nmcbackend.model.entity.DrugInfo;
import com.gjj.nmcbackend.service.DrugInfoService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "药品信息管理", value = "药品信息相关操作")
@RestController
@RequestMapping("/drugInfo")
public class DrugInfoController {

    @Resource
    private DrugInfoService drugInfoService;

    /**
     * 添加药品信息
     *
     * @param addDrugInfoRequest 药品信息
     * @return 添加结果
     */
    @ApiOperation(value = "添加药品信息", notes = "添加一个新的药品信息")
    @ApiImplicitParam(name = "addDrugInfoRequest", value = "药品信息实体", required = true, dataType = "DrugInfo")
    @UserAuthCheck
    @PostMapping("/add")
    public BaseResponse<Long> addDrugInfo(@RequestBody DrugInfo addDrugInfoRequest) {
        ThrowUtils.throwIf(addDrugInfoRequest == null , ErrorCode.PARAMS_ERROR);
        drugInfoService.save(addDrugInfoRequest);
        return ResultUtils.success(addDrugInfoRequest.getId());
    }

    /**
     * 删除药品信息
     *
     * @param id 药品ID
     * @return 删除结果
     */
    @ApiOperation(value = "删除药品信息", notes = "根据ID删除药品信息")
    @ApiImplicitParam(name = "id", value = "药品ID", required = true, paramType = "path", dataType = "Long")
    @UserAuthCheck
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteDrugInfo(@PathVariable Long id) {
        // 1. 校验参数
        ThrowUtils.throwIf(id == null , ErrorCode.PARAMS_ERROR , "药品ID不能为空");

        // 2. 检查药品是否存在
        DrugInfo drugInfo = drugInfoService.getById(id);
        ThrowUtils.throwIf(drugInfo == null , ErrorCode.NOT_FOUND_ERROR , "药品信息不存在");

        // 3. 删除药品信息
        boolean result = drugInfoService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 修改药品信息
     *
     * @param drugInfo 药品信息
     * @return 修改结果
     */
    @ApiOperation(value = "更新药品信息", notes = "更新药品信息")
    @ApiImplicitParam(name = "drugInfo", value = "药品信息实体", required = true, dataType = "DrugInfo")
    @UserAuthCheck
    @PutMapping("/update")
    public BaseResponse<Boolean> updateDrugInfo(@RequestBody DrugInfo drugInfo) {
        ThrowUtils.throwIf(drugInfo == null , ErrorCode.PARAMS_ERROR);
        boolean result = drugInfoService.updateById(drugInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据药品名称查询
     *
     * @param chineseName 药品名称
     * @return 药品信息列表
     */
    @ApiOperation(value = "根据名称查询药品", notes = "根据中文名称查询药品信息")
    @ApiImplicitParam(name = "chineseName", value = "药品中文名称", required = true, paramType = "query", dataType = "String")
    @UserAuthCheck
    @GetMapping("/getByName")
    public BaseResponse<List<DrugInfo>> getDrugInfoByName(@RequestParam String chineseName) {
        ThrowUtils.throwIf(StrUtil.isEmpty(chineseName) , ErrorCode.PARAMS_ERROR);
        List<DrugInfo> drugInfoList = drugInfoService.getDrugInfoByName(chineseName);
        return ResultUtils.success(drugInfoList);
    }

    /**
     * 分页获取药品信息
     *
     * @param current 当前页
     * @param size 每页大小
     * @return 分页结果
     */
    @ApiOperation(value = "分页查询药品信息", notes = "分页获取药品信息，可选项按中文名称筛选")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", defaultValue = "1", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "chinaName", value = "药品中文名称(可选)", paramType = "query", dataType = "String")
    })
    @UserAuthCheck
    @GetMapping("/list/page")
    public BaseResponse<Page<DrugInfo>> listDrugInfoByPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String chinaName) {

        Page<DrugInfo> page = drugInfoService.listDrugInfoByPage(current, size, chinaName);
        return ResultUtils.success(page);
    }

    /**
     * 批量删除药品信息
     * @param ids 药品ID列表
     * @return 删除结果
     */
    @ApiOperation(value = "批量删除药品信息", notes = "根据ID列表批量删除药品信息")
    @ApiImplicitParam(name = "ids", value = "药品ID列表", required = true, dataType = "List<Long>")
    @UserAuthCheck
    @DeleteMapping("/delete/batch")
    public BaseResponse<Boolean> deleteDrugInfoByIds(@RequestBody List<Long> ids) {
        ThrowUtils.throwIf(ids == null || ids.isEmpty() , ErrorCode.PARAMS_ERROR , "药品ID列表不能为空");
        boolean result = drugInfoService.deleteDrugInfoByIds(ids);
        return ResultUtils.success(result);
    }
}