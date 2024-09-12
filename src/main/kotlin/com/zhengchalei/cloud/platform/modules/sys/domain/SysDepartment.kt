/*
 * 版权所有 © 2024 郑查磊.
 * 保留所有权利.
 *
 * 注意: 本文件受著作权法保护，未经授权不得复制或传播。
 */
package com.zhengchalei.cloud.platform.modules.sys.domain

import com.zhengchalei.cloud.platform.config.jimmer.TenantAware
import org.babyfish.jimmer.sql.*

@Entity
@Table(name = "sys_department")
interface SysDepartment : TenantAware {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long

    val name: String

    val sort: Int

    val status: Boolean

    val description: String?

    @ManyToOne val parent: SysDepartment?

    @OneToMany(mappedBy = "parent") val children: List<SysDepartment>
}
