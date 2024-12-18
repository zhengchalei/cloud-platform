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
package com.zhengchalei.xadmin.config.jimmer.filter

import com.zhengchalei.xadmin.config.jimmer.entity.DataScopeEntity
import com.zhengchalei.xadmin.config.jimmer.entity.`createBy?`
import com.zhengchalei.xadmin.config.jimmer.filter.DataScope.*
import com.zhengchalei.xadmin.config.security.SecurityUtils
import com.zhengchalei.xadmin.modules.sys.domain.`department?`
import com.zhengchalei.xadmin.modules.sys.domain.id
import org.babyfish.jimmer.sql.EnumType
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.valueIn
import org.babyfish.jimmer.sql.kt.filter.KAssociationIntegrityAssuranceFilter
import org.babyfish.jimmer.sql.kt.filter.KFilterArgs
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class DataScopeFilter(private val jdbcTemplate: JdbcTemplate) : KAssociationIntegrityAssuranceFilter<DataScopeEntity> {

    /**
     * 根据当前用户的数据权限过滤查询范围
     *
     * 此函数重写了KFilterArgs<DataScopeAware>接口的filter方法，根据当前用户所属的数据权限类型， 动态修改查询条件，以限制查询结果范围这使得每个用户只能查询自己有权限看到的数据
     *
     * @param args 查询参数对象，包含查询所需的表格信息和条件构建器通过这个参数，我们可以添加额外的查询条件
     */
    override fun filter(args: KFilterArgs<DataScopeEntity>) {
        // 获取当前用户的数据权限范围
        val currentUserDataScope = SecurityUtils.getCurrentUserDataScope()

        // 根据不同的数据权限范围，采取不同的过滤策略
        when (currentUserDataScope) {
            // 如果当前用户可以查看所有数据，则不进行任何过滤
            ALL -> return
            // 如果当前用户只能查看自己的数据，获取当前用户的ID，并添加过滤条件
            SELF ->
                SecurityUtils.getCurrentUserIdOrNull()?.let { currentUserId ->
                    args.apply { where(table.`createBy?`.id eq currentUserId) }
                }
            // 如果当前用户只能查看本部门的数据，获取当前用户的部门ID，并添加过滤条件
            DEPARTMENT ->
                SecurityUtils.getCurrentUserDepartmentIdOrNull()?.let { currentUserDepartmentId ->
                    args.apply { where(table.`createBy?`.`department?`.id eq currentUserDepartmentId) }
                }
            // 如果当前用户可以查看本部门及子部门的数据，获取当前用户的部门ID，以及所有子部门的ID，并添加过滤条件
            DEPARTMENT_AND_SUB_DEPARTMENT ->
                SecurityUtils.getCurrentUserDepartmentIdOrNull()?.let { currentUserDepartmentId ->
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
                    val childrenIds = this.jdbcTemplate.queryForList(sql, Long::class.java, currentUserDepartmentId)
                    args.apply { where(table.`createBy?`.`department?`.id valueIn childrenIds) }
                }
            // 如果当前用户的数据权限是自定义的，根据自定义规则获取当前用户的部门，以及该部门有权限访问的数据范围，并添加过滤条件
            CUSTOM ->
                SecurityUtils.getCurrentUserDepartmentIdOrNull()?.let { currentUserDepartmentId ->
                    val list =
                        this.jdbcTemplate.queryForList(
                            "select data_scope_department_id from sys_department_data_scope where department_id = ?",
                            Long::class.java,
                            currentUserDepartmentId,
                        )
                    args.apply { where(table.`createBy?`.`department?`.id valueIn list) }
                }
        }
    }
}

/**
 * 定义数据可见范围。 此枚举类用于表示用户可以访问的数据范围，不同的枚举值代表不同的数据可见级别。
 *
 * @see EnumType 该注解用于指定持久化枚举类型的策略，使用枚举常量的名称。
 */
@EnumType(EnumType.Strategy.NAME)
enum class DataScope {
    /** 可以访问所有数据。 */
    ALL,

    /** 只能访问自己的数据。 */
    SELF,

    /** 本部门数据权限 */
    DEPARTMENT,

    /** 可以访问子部门的数据。 */
    DEPARTMENT_AND_SUB_DEPARTMENT,

    /** 自定义数据范围。 */
    CUSTOM;

    companion object {
        /**
         * 根据名称获取对应的 DataScope 枚举值。
         *
         * @param name 枚举值的名称
         * @return 对应的 DataScope 枚举值
         * @throws IllegalArgumentException 如果没有找到对应的枚举值
         */
        fun value(name: String): DataScope {
            return entries.find { it.name == name } ?: throw IllegalArgumentException("不存在的数据权限类型: $name")
        }
    }
}
