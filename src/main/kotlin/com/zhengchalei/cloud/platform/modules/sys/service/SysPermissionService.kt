/*
 * 版权所有 © 2024 郑查磊.
 * 保留所有权利.
 *
 * 注意: 本文件受著作权法保护，未经授权不得复制或传播。
 */
package com.zhengchalei.cloud.platform.modules.sys.service

import com.zhengchalei.cloud.platform.modules.sys.domain.SysPermission
import com.zhengchalei.cloud.platform.modules.sys.domain.dto.SysPermissionCreateInput
import com.zhengchalei.cloud.platform.modules.sys.domain.dto.SysPermissionDetailView
import com.zhengchalei.cloud.platform.modules.sys.domain.dto.SysPermissionListSpecification
import com.zhengchalei.cloud.platform.modules.sys.domain.dto.SysPermissionUpdateInput
import com.zhengchalei.cloud.platform.modules.sys.repository.SysPermissionRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 系统权限服务
 *
 * @param [sysPermissionRepository] 系统权限存储库
 * @constructor 创建[SysPermissionService]
 * @author 郑查磊
 * @date 2024-08-17
 */
@Service
@Transactional(rollbackFor = [Exception::class])
class SysPermissionService(private val sysPermissionRepository: SysPermissionRepository) {
    private val logger = org.slf4j.LoggerFactory.getLogger(SysPermissionService::class.java)

    /**
     * 查找系统权限通过ID
     *
     * @param [id] ID
     * @return [SysPermissionDetailView]
     */
    fun findSysPermissionById(id: Long): SysPermissionDetailView = this.sysPermissionRepository.findDetailById(id)

    /**
     * 查找系统权限列表
     *
     * @param [specification] 查询条件构造器
     */
    fun findSysPermissionList(specification: SysPermissionListSpecification) =
        this.sysPermissionRepository.findList(specification)

    /**
     * 查找系统权限树
     *
     * @param [specification] 查询条件构造器
     */
    fun findSysPermissionTree(specification: SysPermissionListSpecification) =
        this.sysPermissionRepository.findTree(specification)

    /**
     * 查找系统权限分页
     *
     * @param [specification] 查询条件构造器
     * @param [pageable] 可分页
     */
    fun findSysPermissionPage(specification: SysPermissionListSpecification, pageable: Pageable) =
        this.sysPermissionRepository.findPage(specification, pageable)

    /**
     * 创建系统权限
     *
     * @param [sysPermissionCreateInput] 系统权限创建输入
     * @return [SysPermissionDetailView]
     */
    fun createSysPermission(sysPermissionCreateInput: SysPermissionCreateInput): SysPermissionDetailView {
        val sysPermission: SysPermission = this.sysPermissionRepository.insert(sysPermissionCreateInput)
        return findSysPermissionById(sysPermission.id)
    }

    /**
     * 更新系统权限通过ID
     *
     * @param [sysPermissionUpdateInput] 系统权限更新输入
     * @return [SysPermissionDetailView]
     */
    fun updateSysPermissionById(sysPermissionUpdateInput: SysPermissionUpdateInput): SysPermissionDetailView {
        val sysPermission = this.sysPermissionRepository.update(sysPermissionUpdateInput)
        return findSysPermissionById(sysPermission.id)
    }

    /**
     * 删除系统权限通过ID
     *
     * @param [id] ID
     */
    fun deleteSysPermissionById(id: Long) = this.sysPermissionRepository.deleteSysPermissionById(id)

    /**
     * 查找系统权限树根
     *
     * @param [specification] 查询条件构造器
     */
    fun findSysPermissionTreeRoot(specification: SysPermissionListSpecification) =
        this.sysPermissionRepository.treeRoot(specification)

    /**
     * 查找系统权限树选择
     *
     * @param [specification] 查询条件构造器
     */
    fun findSysPermissionTreeSelect(specification: SysPermissionListSpecification) =
        this.sysPermissionRepository.treeSelect(specification)
}
