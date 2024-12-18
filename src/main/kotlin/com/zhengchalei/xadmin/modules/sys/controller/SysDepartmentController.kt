/*
Copyright 2024 [郑查磊]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.zhengchalei.xadmin.modules.sys.controller

import com.zhengchalei.xadmin.commons.QueryPage
import com.zhengchalei.xadmin.commons.R
import com.zhengchalei.xadmin.modules.sys.domain.Log
import com.zhengchalei.xadmin.modules.sys.domain.OperationType
import com.zhengchalei.xadmin.modules.sys.domain.dto.*
import com.zhengchalei.xadmin.modules.sys.service.SysDepartmentService
import jakarta.validation.constraints.NotNull
import org.babyfish.jimmer.client.meta.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Api("sys")
@Validated
@RestController
@RequestMapping("/api/sys/department")
class SysDepartmentController(val sysDepartmentService: SysDepartmentService) {
    @PreAuthorize("hasAuthority('sys:department:read') or hasAnyRole('admin')")
    @GetMapping("/id/{id}")
    fun findSysDepartmentById(@PathVariable id: Long): R<SysDepartmentDetailView> {
        val data = sysDepartmentService.findSysDepartmentById(id)
        return R(data = data)
    }

    @PreAuthorize("hasAuthority('sys:department:read') or hasAnyRole('admin')")
    @GetMapping("/list")
    fun findSysDepartmentList(@NotNull specification: SysDepartmentListSpecification): R<List<SysDepartmentPageView>> {
        val data = sysDepartmentService.findSysDepartmentList(specification)
        return R(data = data)
    }

    @PreAuthorize("hasAuthority('sys:department:read') or hasAnyRole('admin')")
    @GetMapping("/tree-root")
    fun findSysDepartmentTreeRoot(specification: SysDepartmentListSpecification): R<List<SysDepartmentTreeRootView>> {
        val data = sysDepartmentService.findSysDepartmentTreeRoot(specification)
        return R(data = data)
    }

    @PreAuthorize("hasAuthority('sys:department:read') or hasAnyRole('admin')")
    @GetMapping("/page")
    fun findSysDepartmentPage(
        @NotNull specification: SysDepartmentListSpecification,
        @NotNull pageable: QueryPage,
    ): R<MutableList<SysDepartmentPageView>> {
        val data = sysDepartmentService.findSysDepartmentPage(specification, pageable.page())
        return R.success(data)
    }

    @PreAuthorize("hasAuthority('sys:department:read') or hasAnyRole('admin')")
    @GetMapping("/tree")
    fun findSysDepartmentTree(specification: SysDepartmentListSpecification): R<List<SysDepartmentTreeView>> {
        val data = sysDepartmentService.findSysDepartmentTree(specification)
        return R(data = data)
    }

    @PreAuthorize("hasAuthority('sys:department:read') or hasAnyRole('admin')")
    @GetMapping("/tree-select")
    fun findSysDepartmentTreeSelect(
        specification: SysDepartmentListSpecification
    ): R<List<SysDepartmentTreeSelectView>> {
        val data = sysDepartmentService.findSysDepartmentTreeSelect(specification)
        return R(data = data)
    }

    @Log(value = "创建系统部门", type = OperationType.CREATE)
    @PreAuthorize("hasAuthority('sys:department:create') or hasAnyRole('admin')")
    @PostMapping("/create")
    fun createSysDepartment(
        @NotNull @RequestBody sysDepartmentCreateInput: SysDepartmentCreateInput
    ): R<SysDepartmentDetailView> {
        val data = sysDepartmentService.createSysDepartment(sysDepartmentCreateInput)
        return R(data = data)
    }

    @Log(value = "修改系统部门", type = OperationType.UPDATE)
    @PreAuthorize("hasAuthority('sys:department:edit') or hasAnyRole('admin')")
    @PostMapping("/update")
    fun updateSysDepartmentById(
        @NotNull @RequestBody sysDepartmentUpdateInput: SysDepartmentUpdateInput
    ): R<SysDepartmentDetailView> {
        val data = sysDepartmentService.updateSysDepartmentById(sysDepartmentUpdateInput)
        return R(data = data)
    }

    @Log(value = "删除系统部门", type = OperationType.DELETE)
    @PreAuthorize("hasAuthority('sys:department:delete') or hasAnyRole('admin')")
    @DeleteMapping("/delete/{id}")
    fun deleteSysDepartmentById(@NotNull @PathVariable id: Long): R<Boolean> {
        sysDepartmentService.deleteSysDepartmentById(id)
        return R()
    }
}
