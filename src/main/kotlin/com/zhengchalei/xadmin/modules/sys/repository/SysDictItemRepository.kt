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
package com.zhengchalei.xadmin.modules.sys.repository

import com.zhengchalei.xadmin.modules.sys.domain.SysDictItem
import com.zhengchalei.xadmin.modules.sys.domain.dto.SysDictItemDetailView
import com.zhengchalei.xadmin.modules.sys.domain.dto.SysDictItemListSpecification
import com.zhengchalei.xadmin.modules.sys.domain.dto.SysDictItemPageView
import com.zhengchalei.xadmin.modules.sys.domain.id
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.spring.repository.fetchSpringPage
import org.babyfish.jimmer.sql.kt.ast.expression.asc
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface SysDictItemRepository : KRepository<SysDictItem, Long> {
    fun findDetailById(id: Long) =
        sql.createQuery(SysDictItem::class) {
                orderBy(table.id.asc())
                where(table.id eq id)
                select(table.fetch(SysDictItemDetailView::class))
            }
            .fetchOne()

    fun findPage(specification: SysDictItemListSpecification, pageable: Pageable): Page<SysDictItemPageView> =
        sql.createQuery(SysDictItem::class) {
                orderBy(table.id.asc())
                where(specification)
                select(table.fetch(SysDictItemPageView::class))
            }
            .fetchSpringPage(pageable)

    fun findList(specification: SysDictItemListSpecification): List<SysDictItemPageView> =
        sql.createQuery(SysDictItem::class) {
                orderBy(table.id.asc())
                where(specification)
                select(table.fetch(SysDictItemPageView::class))
            }
            .execute()
}
