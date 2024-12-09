/*
 * 版权所有 © 2024 郑查磊.
 * 保留所有权利.
 *
 * 注意: 本文件受著作权法保护，未经授权不得复制或传播。
 */
package com.zhengchalei.xadmin.modules.generator.repository

import com.zhengchalei.xadmin.modules.generator.domain.TableColumn
import com.zhengchalei.xadmin.modules.generator.domain.dto.TableColumnDetailView
import com.zhengchalei.xadmin.modules.generator.domain.dto.TableColumnListSpecification
import com.zhengchalei.xadmin.modules.generator.domain.dto.TableColumnPageView
import com.zhengchalei.xadmin.modules.generator.domain.id
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.spring.repository.fetchSpringPage
import org.babyfish.jimmer.sql.kt.ast.expression.asc
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TableColumnRepository : KRepository<TableColumn, Long> {
    fun findDetailById(id: Long) =
        sql.createQuery(TableColumn::class) {
                where(table.id eq id)
                select(table.fetch(TableColumnDetailView::class))
            }
            .fetchOne()

    fun findPage(specification: TableColumnListSpecification, pageable: Pageable): Page<TableColumnPageView> =
        sql.createQuery(TableColumn::class) {
                orderBy(table.id.asc())
                where(specification)
                select(table.fetch(TableColumnPageView::class))
            }
            .fetchSpringPage(pageable)

    fun findList(specification: TableColumnListSpecification): List<TableColumnPageView> =
        sql.createQuery(TableColumn::class) {
                orderBy(table.id.asc())
                where(specification)
                select(table.fetch(TableColumnPageView::class))
            }
            .execute()
}
