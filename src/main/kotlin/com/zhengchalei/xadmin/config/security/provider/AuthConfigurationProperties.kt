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
package com.zhengchalei.xadmin.config.security.provider

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("auth")
class AuthConfigurationProperties {

    var tokenType = AuthTokenType.JWT

    var store: StoreType = StoreType.MEMORY

    var jwt: JwtConfigurationProperties = JwtConfigurationProperties()
}

class JwtConfigurationProperties {
    var secret: String = "nnaWuft6pSSKVkcuzlmqBWi3vO4Cin44"

    // 单位秒 默认 24 小时
    var expired: Long = 60 * 60 * 24
}

enum class StoreType {
    MEMORY,
    REDIS,
    DATABASE,
}

enum class AuthTokenType {
    JWT,
    Stateful,
}
