/*
 * 版权所有 © 2024 郑查磊.
 * 保留所有权利.
 *
 * 注意: 本文件受著作权法保护，未经授权不得复制或传播。
 */
package com.zhengchalei.cloud.platform.modules.sys.controller

import com.zhengchalei.cloud.platform.commons.QueryPage
import com.zhengchalei.cloud.platform.commons.R
import com.zhengchalei.cloud.platform.config.log.Log
import com.zhengchalei.cloud.platform.config.log.OperationType
import com.zhengchalei.cloud.platform.modules.sys.domain.dto.*
import com.zhengchalei.cloud.platform.modules.sys.service.SysDictService
import jakarta.validation.constraints.NotNull
import org.babyfish.jimmer.client.meta.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Api("sys")
@Validated
@RestController
@RequestMapping("/api/sys/dict")
class SysDictController(val sysDictService: SysDictService) {
    @PreAuthorize("hasAuthority('sys:dict:read') or hasAnyRole('admin')")
    @GetMapping("/id/{id}")
    fun findSysDictById(@PathVariable id: Long): R<SysDictDetailView> {
        val data = sysDictService.findSysDictById(id)
        return R(data = data)
    }

    @PreAuthorize("hasAuthority('sys:dict:read') or hasAnyRole('admin')")
    @GetMapping("/list")
    fun findSysDictList(@NotNull specification: SysDictListSpecification): R<List<SysDictPageView>> {
        val data = sysDictService.findSysDictList(specification)
        return R(data = data)
    }

    @PreAuthorize("hasAuthority('sys:dict:read') or hasAnyRole('admin')")
    @GetMapping("/page")
    fun findSysDictPage(
        @NotNull specification: SysDictListSpecification,
        @NotNull pageable: QueryPage,
    ): R<MutableList<SysDictPageView>> {
        val data = sysDictService.findSysDictPage(specification, pageable.page())
        return R.success(data)
    }

    @Log(value = "创建系统字典", type = OperationType.CREATE)
    @PreAuthorize("hasAuthority('sys:dict:create') or hasAnyRole('admin')")
    @PostMapping("/create")
    fun createSysDict(@NotNull @RequestBody sysDictCreateInput: SysDictCreateInput): R<SysDictDetailView> {
        val data = sysDictService.createSysDict(sysDictCreateInput)
        return R(data = data)
    }

    @Log(value = "修改系统字典", type = OperationType.UPDATE)
    @PreAuthorize("hasAuthority('sys:dict:edit') or hasAnyRole('admin')")
    @PostMapping("/update")
    fun updateSysDictById(@NotNull @RequestBody sysDictUpdateInput: SysDictUpdateInput): R<SysDictDetailView> {
        val data = sysDictService.updateSysDictById(sysDictUpdateInput)
        return R(data = data)
    }

    @Log(value = "删除系统字典", type = OperationType.DELETE)
    @PreAuthorize("hasAuthority('sys:dict:delete') or hasAnyRole('admin')")
    @DeleteMapping("/delete/{id}")
    fun deleteSysDictById(@NotNull @PathVariable id: Long): R<Boolean> {
        sysDictService.deleteSysDictById(id)
        return R()
    }
}
