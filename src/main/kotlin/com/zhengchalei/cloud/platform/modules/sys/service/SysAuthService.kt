package com.zhengchalei.cloud.platform.modules.sys.service

import com.zhengchalei.cloud.platform.config.IP2RegionService
import com.zhengchalei.cloud.platform.config.LoginFailException
import com.zhengchalei.cloud.platform.config.ServiceException
import com.zhengchalei.cloud.platform.config.VirtualThreadExecutor
import com.zhengchalei.cloud.platform.config.security.JwtProvider
import com.zhengchalei.cloud.platform.config.security.SecurityUtils
import com.zhengchalei.cloud.platform.config.security.TenantAuthenticationToken
import com.zhengchalei.cloud.platform.modules.sys.domain.SysLoginLog
import com.zhengchalei.cloud.platform.modules.sys.domain.SysUser
import com.zhengchalei.cloud.platform.modules.sys.domain.by
import com.zhengchalei.cloud.platform.modules.sys.repository.SysLoginLogRepository
import java.time.LocalDateTime
import org.babyfish.jimmer.kt.makeIdOnly
import org.babyfish.jimmer.kt.new
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

/**
 * 系统授权服务
 *
 * @param [sysLoginLogRepository] 系统登录日志存储库
 * @param [authenticationManager] 认证管理器
 * @param [jwtProvider] jwt 提供商
 * @constructor 创建[SysAuthService]
 * @author 郑查磊
 * @date 2024-08-17
 */
@Service
class SysAuthService(
    val sysLoginLogRepository: SysLoginLogRepository,
    val authenticationManager: AuthenticationManager,
    val jwtProvider: JwtProvider,
    val userService: SysUserService,
    val ip2RegionService: IP2RegionService,
    val virtualThreadExecutor: VirtualThreadExecutor,
) {
    private val log = LoggerFactory.getLogger(SysAuthService::class.java)

    /**
     * 登录
     *
     * @param [username] 用户名
     * @param [password] 密码
     * @param [captcha] 验证码
     * @param [tenant] 租户
     * @param [ip] ip
     * @return [String]
     */
    fun login(
        username: String,
        password: String,
        captcha: String,
        tenant: String,
        ip: String,
    ): String {
        try {
            val authentication: TenantAuthenticationToken =
                authenticationManager.authenticate(
                    TenantAuthenticationToken(
                        username = username,
                        password = password,
                        captcha = captcha,
                        tenant = tenant,
                    )
                ) as TenantAuthenticationToken
            SecurityContextHolder.getContext().authentication = authentication
            val token: String = jwtProvider.createAccessToken(authentication)
            return token
        } catch (e: ServiceException) {
            log.error("登录失败", e)
            saveLoginLog(username, password, false, e.message, ip, tenant)
            throw e
        } catch (e: Exception) {
            log.error("登录失败", e)
            saveLoginLog(username, password, false, e.message, ip, tenant)
            throw LoginFailException()
        } finally {
            saveLoginLog(username, "", true, null, ip, tenant)
        }
    }

    /**
     * 保存登录日志
     *
     * @param [username] 用户名
     * @param [password] 密码
     * @param [status] 地位
     * @param [errorMessage] 错误信息
     * @param [ip] ip
     * @param [tenant] 租户
     */
    private fun saveLoginLog(
        username: String,
        password: String,
        status: Boolean,
        errorMessage: String?,
        ip: String,
        tenant: String,
    ) {
        this.virtualThreadExecutor.submit {
            val address = this.ip2RegionService.search(ip)
            this.sysLoginLogRepository.insert(
                new(SysLoginLog::class).by {
                    this.username = username
                    this.password = password
                    this.status = status
                    this.errorMessage = errorMessage
                    this.loginTime = LocalDateTime.now()
                    this.ip = ip
                    this.address = address
                    this.tenant = tenant
                    this.sysUser =
                        if (status) makeIdOnly(SysUser::class, userService.currentUserId())
                        else null
                }
            )
        }
    }

    fun switchTenant(tenant: String): String {
        return SecurityUtils.switchTenant(tenant, jwtProvider)
    }

    fun logout() {
        // TODO 期望值， 如果有Redis,使用Redis 存储JWT -》 User
        // 否则为 DB PG 存储
    }
}
