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
package com.zhengchalei.xadmin.config.exceptions

import com.zhengchalei.xadmin.commons.R
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalException {
    private val logger = org.slf4j.LoggerFactory.getLogger(GlobalException::class.java)

    data class Error(val message: String)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): R<Boolean> {
        logger.error("未知错误", e)
        return R(errorMessage = "服务器开小差了", success = false, errorCode = 500)
    }

    @ExceptionHandler(InvalidTokenException::class)
    fun handleInvalidTokenException(e: InvalidTokenException): R<Boolean> {
        logger.warn("无效的令牌", e)
        return R(errorMessage = e.message, success = false, errorCode = 401)
    }

    @ExceptionHandler(ServiceException::class)
    fun handleServiceException(e: ServiceException): R<Boolean> {
        logger.warn("业务错误", e)
        return R(errorMessage = e.message, success = false, errorCode = 500)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException): R<Boolean> {
        logger.error("运行时错误", e)
        return R(errorMessage = "服务器开小差了", success = false, errorCode = 500)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): R<Boolean> {
        logger.error("参数错误", e)
        return R(errorMessage = "请求参数错误", success = false, errorCode = 500)
    }

    @ExceptionHandler(AuthorizationDeniedException::class)
    fun handleIllegalStateException(e: AuthorizationDeniedException): R<Boolean> {
        logger.error("权限不足,拒绝访问", e)
        return R(errorMessage = "权限不足,拒绝访问", success = false, errorCode = 403)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(e: UserNotFoundException): R<Boolean> {
        logger.error("用户不存在", e)
        return R(errorMessage = "用户不存在", success = false, errorCode = 401)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(e: AccessDeniedException): R<Boolean> {
        logger.error("权限不足", e)
        return R(errorMessage = "权限不足, 请联系管理员", success = false, errorCode = 403)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): R<Boolean> {
        logger.error("参数错误", e)
        return R(errorMessage = e.bindingResult.allErrors[0].defaultMessage, success = false, errorCode = 500)
    }
}
