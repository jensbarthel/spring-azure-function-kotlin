package com.github.jensbarthel.safk.aggregateservice.domain.logging

import java.util.logging.Logger

object LoggerProvider {
    private lateinit var internalLogger: Logger

    fun initLogger(logger: Logger) {
        internalLogger = logger
    }

    val logger get() = internalLogger
}