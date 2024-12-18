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

import com.zhengchalei.xadmin.commons.Const
import com.zhengchalei.xadmin.config.exceptions.ServiceException
import com.zhengchalei.xadmin.config.exceptions.UserNotFoundException
import com.zhengchalei.xadmin.modules.sys.domain.SysUser
import com.zhengchalei.xadmin.modules.sys.domain.by
import com.zhengchalei.xadmin.modules.sys.domain.dto.*
import com.zhengchalei.xadmin.modules.sys.repository.SysUserRepository
import org.babyfish.jimmer.kt.new
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 系统用户服务
 *
 * @param [sysUserRepository] 系统用户存储库
 * @constructor 创建[SysUserService]
 * @author 郑查磊
 * @date 2024-08-17
 */
@Service
@Transactional(rollbackFor = [Exception::class])
class SysUserService(
    private val sysUserRepository: SysUserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jdbcTemplate: JdbcTemplate,
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(SysUserService::class.java)

    /**
     * 查找系统用户通过ID
     *
     * @param [id] ID
     * @return [SysUserDetailView]
     */
    fun findSysUserById(id: Long): SysUserDetailView = this.sysUserRepository.findDetailById(id)

    /**
     * 查找系统用户列表
     *
     * @param [specification] 查询条件构造器
     */
    fun findSysUserList(specification: SysUserListSpecification) = this.sysUserRepository.findList(specification)

    /**
     * 查找系统用户分页
     *
     * @param [specification] 查询条件构造器
     * @param [pageable] 可分页
     */
    fun findSysUserPage(specification: SysUserListSpecification, pageable: Pageable): Page<SysUserPageView> {
        // 此处为了查询能够查询到子节点
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
        if (specification.departmentId != null) {
            val sysDepartmentIds = jdbcTemplate.queryForList(sql, Long::class.java, specification.departmentId)
            return this.sysUserRepository.findPage(specification, pageable, sysDepartmentIds)
        }
        return this.sysUserRepository.findPage(specification, pageable)
    }

    /**
     * 创建系统用户
     *
     * @param [sysUserCreateInput] 系统用户创建输入
     * @return [SysUserDetailView]
     */
    fun createSysUser(sysUserCreateInput: SysUserCreateInput): SysUserDetailView {
        if (sysUserCreateInput.username == Const.ADMIN_USER) {
            throw ServiceException("用户名不能为 ${Const.ADMIN_USER}")
        }
        // TODO 集成邮件后, 这里密码应该由邮件发送
        val newSysUser =
            new(SysUser::class).by(sysUserCreateInput.toEntity()) {
                password = passwordEncoder.encode(Const.DEFAULT_PASSWORD)
            }
        val sysUser: SysUser = this.sysUserRepository.insert(newSysUser)
        return findSysUserById(sysUser.id)
    }

    /**
     * 更新系统用户分页通过ID
     *
     * @param [sysUserUpdateInput] 系统用户更新输入
     * @return [SysUserDetailView]
     */
    fun updateSysUserById(sysUserUpdateInput: SysUserUpdateInput): SysUserDetailView {
        val oldUser =
            this.sysUserRepository.findById(sysUserUpdateInput.id).orElseThrow { throw ServiceException("用户不存在") }
        if (oldUser.username == Const.ADMIN_USER) {
            throw ServiceException("${Const.ADMIN_USER} 不能修改用户名")
        }
        val sysUser = this.sysUserRepository.update(sysUserUpdateInput)
        return findSysUserById(sysUser.id)
    }

    /**
     * 删除系统用户分页通过ID
     *
     * @param [id] ID
     */
    fun deleteSysUserById(id: Long) {
        val sysUser = this.sysUserRepository.findById(id).orElseThrow { throw ServiceException("用户不存在") }
        if (sysUser.username == Const.ADMIN_USER) {
            throw ServiceException("${sysUser.username} 不能删除")
        }
        this.sysUserRepository.deleteById(id)
    }

    /**
     * 当前用户信息
     *
     * @return [SysUser]
     */
    fun currentUserInfo(): SysUser = this.sysUserRepository.currentUserInfo()

    /**
     * 当前用户 ID
     *
     * @return [Long]
     */
    fun currentUserId(): Long = this.sysUserRepository.currentUserId()

    /** 修改密码 */
    fun changePassword(userId: Long, password: String) {
        val sysUser = this.sysUserRepository.findById(userId).orElseThrow { throw UserNotFoundException() }
        val newPassword = passwordEncoder.encode(password)
        this.sysUserRepository.update(
            new(SysUser::class).by(sysUser) {
                this.id = userId
                this.password = newPassword
            }
        )
    }

    fun findSysUserExportList(specification: SysUserListSpecification): List<SysUserExportView> {
        return this.sysUserRepository.findSysUserExportList(specification)
    }

    fun findSysUserByEmail(email: String): SysUserDetailView {
        return this.sysUserRepository.findByEmail(email) ?: throw ServiceException("用户不存在")
    }
}
