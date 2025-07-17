package com.gjj.nmcbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.annotion.UserAuthCheck;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientDiseaseRequest;
import com.gjj.nmcbackend.model.entity.DiseaseInfo;
import com.gjj.nmcbackend.model.entity.InpatientDisease;
import com.gjj.nmcbackend.service.DiseaseInfoService;
import com.gjj.nmcbackend.service.InpatientDiseaseService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "住院疾病管理", value = "住院患者疾病诊断相关操作")
@RestController
@RequestMapping("/inpatient/disease")
public class InpatientDiseaseController {

    @Resource
    private InpatientDiseaseService inpatientDiseaseService;

    @Resource
    private DiseaseInfoService diseaseInfoService;

    /**
     * 添加诊断记录
     */
    @ApiOperation(value = "添加疾病诊断记录", notes = "为住院患者添加新的疾病诊断记录")
    @ApiImplicitParam(name = "request", value = "疾病诊断添加请求参数", required = true, dataType = "AddInpatientDiseaseRequest")
    @UserAuthCheck
    @PostMapping("/add")
    public BaseResponse<Boolean> addDisease(@RequestBody AddInpatientDiseaseRequest request) {
        boolean result = inpatientDiseaseService.addDisease(request);
        return ResultUtils.success(result);
    }

    /**
     * 删除诊断记录
     */
    @ApiOperation(value = "删除疾病诊断记录", notes = "根据ID删除疾病诊断记录")
    @ApiImplicitParam(name = "id", value = "疾病诊断记录ID", required = true, paramType = "path", dataType = "Integer")
    @UserAuthCheck
    @PostMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteDisease(@PathVariable Integer id) {
        boolean result = inpatientDiseaseService.removeById(id);
        return ResultUtils.success(result);
    }

    @ApiOperation(value = "分页查询疾病信息", notes = "分页获取疾病基础信息，可选项按疾病名称筛选")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", defaultValue = "1", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "diseaseName", value = "疾病名称(可选)", paramType = "query", dataType = "String")
    })
    @UserAuthCheck
    @GetMapping("/list/page/vo")
    public BaseResponse<Page<DiseaseInfo>> listDiseaseInfoByPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String diseaseName) {
        Page<DiseaseInfo> page = diseaseInfoService.listDiseaseInfoByPage(current, size, diseaseName);
        return ResultUtils.success(page);
    }

    /**
     * 检查患者是否已添加该疾病诊断
     */
    @ApiOperation(value = "检查疾病诊断记录", notes = "检查患者是否已存在特定疾病诊断记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "diseaseId", value = "疾病ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "diseaseType", value = "疾病类型(可选)", paramType = "query", dataType = "Integer")
    })
    @UserAuthCheck
    @GetMapping("/check")
    public BaseResponse<Boolean> checkDiseaseExists(
            @RequestParam Integer patientId,
            @RequestParam Integer diseaseId,
            @RequestParam(required = false) Integer diseaseType) {
        boolean exists = inpatientDiseaseService.checkDiseaseExists(patientId, diseaseId, diseaseType);
        return ResultUtils.success(exists);
    }

    /**
     * 分页查询患者的诊断记录
     */
    @ApiOperation(value = "分页查询患者诊断记录", notes = "分页获取指定患者的疾病诊断记录，可选项按疾病类型筛选")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", defaultValue = "1", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "diseaseType", value = "疾病类型(可选)", paramType = "query", dataType = "Integer")
    })
    @UserAuthCheck
    @GetMapping("/list/page")
    public BaseResponse<Page<InpatientDisease>> listDiseaseByPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam Integer patientId,
            @RequestParam(required = false) Integer diseaseType) {
        Page<InpatientDisease> page = inpatientDiseaseService.listDiseaseByPage(current, size, patientId, diseaseType);
        return ResultUtils.success(page);
    }
}