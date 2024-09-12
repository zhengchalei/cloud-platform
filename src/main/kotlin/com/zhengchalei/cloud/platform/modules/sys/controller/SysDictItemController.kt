/*
 * 版权所有 © 2024 郑查磊.
 * 保留所有权利.
 *
 * 注意: 本文件受著作权法保护，未经授权不得复制或传播。
 */
package com.zhengchalei.cloud.platform.modules.sys.controller

import com.zhengchalei.cloud.platform.commons.QueryPage
import com.zhengchalei.cloud.platform.commons.R
import com.zhengchalei.cloud.platform.modules.sys.domain.dto.*
import com.zhengchalei.cloud.platform.modules.sys.service.SysDictItemService
import jakarta.validation.constraints.NotNull
import org.babyfish.jimmer.client.meta.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Api("sys")
@Validated
@RestController
@RequestMapping("/api/sys/dict-item")
class SysDictItemController(val sysDictItemService: SysDictItemService) {
    @PreAuthorize("hasAuthority('sys:dict-item:id') or hasAnyRole('admin')")
    @GetMapping("/id/{id}")
    fun findSysDictItemById(@PathVariable id: Long): R<SysDictItemDetailView> {
        val data = sysDictItemService.findSysDictItemById(id)
        return R(data = data)
    }

    @PreAuthorize("hasAuthority('sys:dict-item:list') or hasAnyRole('admin')")
    @GetMapping("/list")
    fun findSysDictItemList(@NotNull specification: SysDictItemPageSpecification): R<List<SysDictItemPageView>> {
        val data = sysDictItemService.findSysDictItemList(specification)
        return R(data = data)
    }

    @PreAuthorize("hasAuthority('sys:dict-item:page') or hasAnyRole('admin')")
    @GetMapping("/page")
    fun findSysDictItemPage(
        @NotNull specification: SysDictItemPageSpecification,
        @NotNull pageable: QueryPage,
    ): R<MutableList<SysDictItemPageView>> {
        val data = sysDictItemService.findSysDictItemPage(specification, pageable.page())
        return R.success(data)
    }

    @PreAuthorize("hasAuthority('sys:dict-item:create') or hasAnyRole('admin')")
    @PostMapping("/create")
    fun createSysDictItem(
        @NotNull @RequestBody sysDictItemCreateInput: SysDictItemCreateInput
    ): R<SysDictItemDetailView> {
        val data = sysDictItemService.createSysDictItem(sysDictItemCreateInput)
        return R(data = data)
    }

    @PreAuthorize("hasAuthority('sys:dict-item:update') or hasAnyRole('admin')")
    @PostMapping("/update")
    fun updateSysDictItemById(
        @NotNull @RequestBody sysDictItemUpdateInput: SysDictItemUpdateInput
    ): R<SysDictItemDetailView> {
        val data = sysDictItemService.updateSysDictItemById(sysDictItemUpdateInput)
        return R(data = data)
    }

    @PreAuthorize("hasAuthority('sys:dict-item:delete') or hasAnyRole('admin')")
    @DeleteMapping("/delete/{id}")
    fun deleteSysDictItemById(@NotNull @PathVariable id: Long): R<Boolean> {
        sysDictItemService.deleteSysDictItemById(id)
        return R()
    }
}
