/*
 * 版权所有 © 2024 郑查磊.
 * 保留所有权利.
 *
 * 注意: 本文件受著作权法保护，未经授权不得复制或传播。
 */
package com.zhengchalei.cloud.platform.modules.sys.domain

import java.time.LocalDateTime
import org.babyfish.jimmer.sql.*

@Entity
@Table(name = "sys_tenant")
interface SysTenant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long

    val name: String

    val code: String

    val status: Boolean

    val description: String?

    val contactName: String?

    val contactPhone: String?

    val domain: String?

    val logo: String?

    val address: String?

    val createTime: LocalDateTime

    val updateTime: LocalDateTime
}
