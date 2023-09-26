package ru.data.common.models.logger

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import java.util.logging.Logger


object LogCustom {
    private lateinit var applicationId: String
    private var isShowLog = false

    fun init(applicationName: String, isLogOn: Boolean) {
        this.applicationId = applicationName
        this.isShowLog = isLogOn
        Napier.base(DebugAntilog())

        val antilogLogger = Logger.getLogger(DebugAntilog::class.java.name)
        antilogLogger.useParentHandlers = false
        logI("LogCustom", "init")
    }


    private val separator: CharSequence = ", "
    private val prefix: CharSequence = "[[ "
    private val postfix: CharSequence = " ]]"

    fun logI(vararg any: Any?, throwable: Throwable? = null) {
        if (!isShowLog) return
        Napier.i(
            throwable = throwable,
            tag = applicationId,
            message = any.joinToString(separator, prefix, postfix)
        )
    }

    fun logE(vararg any: Any?, throwable: Throwable? = null) {
        if (!isShowLog) return
        Napier.e(
            throwable = throwable,
            tag = applicationId,
            message = any.joinToString(separator, prefix, postfix)
        )
    }

    fun logD(vararg any: Any?, throwable: Throwable? = null) {
        if (!isShowLog) return
        Napier.d(
            throwable = throwable,
            tag = applicationId,
            message = any.joinToString(separator, prefix, postfix)
        )
    }

    fun logW(vararg any: Any?, throwable: Throwable? = null) {
        if (!isShowLog) return
        Napier.w(
            throwable = throwable,
            tag = applicationId,
            message = any.joinToString(separator, prefix, postfix)
        )
    }

    fun logV(vararg any: Any?, throwable: Throwable? = null) {
        if (!isShowLog) return
        Napier.v(
            throwable = throwable,
            tag = applicationId,
            message = any.joinToString(separator, prefix, postfix)
        )
    }

    fun logWtf(vararg any: Any?, throwable: Throwable? = null) {
        if (!isShowLog) return
        Napier.wtf(
            throwable = throwable,
            tag = applicationId,
            message = any.joinToString(separator, prefix, postfix)
        )
    }


    fun logForciblyI(vararg any: Any?, throwable: Throwable? = null) {
        Napier.i(
            throwable = throwable,
            tag = applicationId,
            message = any.joinToString(separator, prefix, postfix)
        )
    }

    fun logForciblyE(vararg any: Any?, throwable: Throwable? = null) {
        Napier.e(
            throwable = throwable,
            tag = applicationId,
            message = any.joinToString(separator, prefix, postfix)
        )
    }

    fun logForciblyD(vararg any: Any?, throwable: Throwable? = null) {
        Napier.d(
            throwable = throwable,
            tag = applicationId,
            message = any.joinToString(separator, prefix, postfix)
        )
    }

    fun logForciblyW(vararg any: Any?, throwable: Throwable? = null) {
        Napier.w(
            throwable = throwable,
            tag = applicationId,
            message = any.joinToString(separator, prefix, postfix)
        )
    }

    fun logForciblyV(vararg any: Any?, throwable: Throwable? = null) {
        Napier.v(
            throwable = throwable,
            tag = applicationId,
            message = any.joinToString(separator, prefix, postfix)
        )
    }

    fun logForciblyWtf(vararg any: Any?, throwable: Throwable? = null) {
        Napier.wtf(
            throwable = throwable,
            tag = applicationId,
            message = any.joinToString(separator, prefix, postfix)
        )
    }

}