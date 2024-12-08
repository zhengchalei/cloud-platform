/*
 * 版权所有 © 2024 郑查磊.
 * 保留所有权利.
 *
 * 注意: 本文件受著作权法保护，未经授权不得复制或传播。
 */
package com.zhengchalei.xadmin

import com.zhengchalei.xadmin.config.virtualThread.VirtualThreadExecutor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class DemoVTController(private val virtualThreadExecutor: VirtualThreadExecutor) {

    @GetMapping("/vt")
    fun businessVirtualThreadExecutor() {
        virtualThreadExecutor.execute { println("当前用户信息: ${SecurityContextHolder.getContext().authentication.name}") }
    }
}
