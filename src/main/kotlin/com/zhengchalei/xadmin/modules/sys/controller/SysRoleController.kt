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
import com.zhengchalei.xadmin.modules.sys.service.SysRoleService
import jakarta.validation.constraints.NotNull
import org.babyfish.jimmer.client.meta.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Api("sys")
@Validated
@RestController
@RequestMapping("/api/sys/role")
class SysRoleController(val sysRoleService: SysRoleService) {
    @PreAuthorize("hasAuthority('sys:role:read') or hasAnyRole('admin')")
    @GetMapping("/id/{id}")
    fun findSysRoleById(@PathVariable id: Long): R<SysRoleDetailView> {
        val data = sysRoleService.findSysRoleById(id)
        return R(data = data)
    }

    @PreAuthorize("hasAuthority('sys:role:read') or hasAnyRole('admin')")
    @GetMapping("/list")
    fun findSysRoleList(@NotNull specification: SysRoleListSpecification): R<List<SysRolePageView>> {
        val data = sysRoleService.findSysRoleList(specification)
        return R(data = data)
    }

    @PreAuthorize("hasAuthority('sys:role:read') or hasAnyRole('admin')")
    @GetMapping("/page")
    fun findSysRolePage(
        @NotNull specification: SysRoleListSpecification,
        @NotNull pageable: QueryPage,
    ): R<MutableList<SysRolePageView>> {
        val data = sysRoleService.findSysRolePage(specification, pageable.page())
        return R.success(data)
    }

    @Log(value = "创建系统角色", type = OperationType.CREATE)
    @PreAuthorize("hasAuthority('sys:role:create') or hasAnyRole('admin')")
    @PostMapping("/create")
    fun createSysRole(@NotNull @RequestBody sysRoleCreateInput: SysRoleCreateInput): R<SysRoleDetailView> {
        val data = sysRoleService.createSysRole(sysRoleCreateInput)
        return R(data = data)
    }

    @Log(value = "修改系统角色", type = OperationType.UPDATE)
    @PreAuthorize("hasAuthority('sys:role:edit') or hasAnyRole('admin')")
    @PostMapping("/update")
    fun updateSysRoleById(@NotNull @RequestBody sysRoleUpdateInput: SysRoleUpdateInput): R<SysRoleDetailView> {
        val data = sysRoleService.updateSysRoleById(sysRoleUpdateInput)
        return R(data = data)
    }

    @Log(value = "删除系统角色", type = OperationType.DELETE)
    @PreAuthorize("hasAuthority('sys:role:delete') or hasAnyRole('admin')")
    @DeleteMapping("/delete/{id}")
    fun deleteSysRoleById(@NotNull @PathVariable id: Long): R<Boolean> {
        sysRoleService.deleteSysRoleById(id)
        return R()
    }
}
