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
package com.zhengchalei.xadmin.modules.sys.domain

import com.zhengchalei.xadmin.config.jimmer.entity.DataScopeEntity
import java.time.LocalDateTime
import org.babyfish.jimmer.sql.*

@Entity
@Table(name = "sys_login_log")
interface SysLoginLog : DataScopeEntity {

    val username: String

    val password: String

    val ip: String

    val address: String?

    val loginTime: LocalDateTime

    val status: Boolean

    val errorMessage: String?
}
