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
package com.zhengchalei.xadmin.modules.sys.service

import com.zhengchalei.xadmin.modules.sys.domain.SysDepartment
import com.zhengchalei.xadmin.modules.sys.domain.dto.*
import com.zhengchalei.xadmin.modules.sys.repository.SysDepartmentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 系统部门服务
 *
 * @param [sysDepartmentRepository] 系统部门存储库
 * @constructor 创建[SysDepartmentService]
 * @author 郑查磊
 * @date 2024-08-17
 */
@Service
@Transactional(rollbackFor = [Exception::class])
class SysDepartmentService(
    private val sysDepartmentRepository: SysDepartmentRepository,
    private val jdbcTemplate: JdbcTemplate,
) {

    private val log = org.slf4j.LoggerFactory.getLogger(SysDepartmentService::class.java)

    // 查询当前节点和所有子节点的id
    fun findChildrenIds(id: Long): List<Long> {
        val sql =
            """
            WITH RECURSIVE DepartmentHierarchy AS (
                SELECT id FROM sys_department WHERE id = ?
                UNION ALL
                SELECT d.id FROM sys_department d
                INNER JOIN DepartmentHierarchy dh ON d.parent_id = dh.id
            )
            SELECT id FROM DepartmentHierarchy
        """
        val sysDepartmentIds = jdbcTemplate.queryForList(sql, Long::class.java, id)
        return sysDepartmentIds
    }

    /**
     * 查找系统部门通过ID
     *
     * @param [id] ID
     * @return [SysDepartmentDetailView]
     */
    fun findSysDepartmentById(id: Long): SysDepartmentDetailView {
        val data = this.sysDepartmentRepository.findDetailById(id)
        return data
    }

    /**
     * 查找系统部门列表
     *
     * @param [specification] 查询条件构造器
     */
    fun findSysDepartmentList(specification: SysDepartmentListSpecification): List<SysDepartmentPageView> {
        val data = this.sysDepartmentRepository.findList(specification)
        return data
    }

    /**
     * 查找系统部门树
     *
     * @param [specification] 查询条件构造器
     */
    fun findSysDepartmentTree(specification: SysDepartmentListSpecification): List<SysDepartmentTreeView> {
        val data = this.sysDepartmentRepository.findTree(specification)
        return data
    }

    /**
     * 查找系统部门分页
     *
     * @param [specification] 查询条件构造器
     * @param [pageable] 可分页
     */
    fun findSysDepartmentPage(
        specification: SysDepartmentListSpecification,
        pageable: Pageable,
    ): Page<SysDepartmentPageView> {
        val data = this.sysDepartmentRepository.findPage(specification, pageable)
        return data
    }

    /**
     * 创建系统部门
     *
     * @param [sysDepartmentCreateInput] 系统部门创建输入
     * @return [SysDepartmentDetailView]
     */
    fun createSysDepartment(sysDepartmentCreateInput: SysDepartmentCreateInput): SysDepartmentDetailView {
        val sysDepartment: SysDepartment = this.sysDepartmentRepository.insert(sysDepartmentCreateInput)
        val data = findSysDepartmentById(sysDepartment.id)
        return data
    }

    /**
     * 更新系统部门通过ID
     *
     * @param [sysDepartmentUpdateInput] 系统部门更新输入
     * @return [SysDepartmentDetailView]
     */
    fun updateSysDepartmentById(sysDepartmentUpdateInput: SysDepartmentUpdateInput): SysDepartmentDetailView {
        val sysDepartment = this.sysDepartmentRepository.update(sysDepartmentUpdateInput)
        return findSysDepartmentById(sysDepartment.id)
    }

    /**
     * 删除系统部门通过ID
     *
     * @param [id] ID
     */
    fun deleteSysDepartmentById(id: Long) {
        this.sysDepartmentRepository.deleteSysDepartmentById(id)
    }

    /**
     * 查找系统部门树根
     *
     * @param [specification] 查询条件构造器
     */
    fun findSysDepartmentTreeRoot(specification: SysDepartmentListSpecification): List<SysDepartmentTreeRootView> {
        val data = this.sysDepartmentRepository.findTreeRoot(specification)
        return data
    }

    /**
     * 查找系统部门树选择
     *
     * @param [specification] 查询条件构造器
     * @return [List<SysDepartmentTreeSelectView>]
     */
    fun findSysDepartmentTreeSelect(specification: SysDepartmentListSpecification): List<SysDepartmentTreeSelectView> {
        val data = this.sysDepartmentRepository.findTreeSelect(specification)
        return data
    }
}
