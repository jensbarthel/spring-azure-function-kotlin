package com.github.jensbarthel.safk.aggregateservice.adapter.api

import com.github.jensbarthel.safk.aggregateservice.domain.logging.LoggerProvider
import com.github.jensbarthel.safk.aggregateservice.domain.logging.LoggerProvider.logger
import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.HttpMethod.GET
import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import com.microsoft.azure.functions.HttpStatus
import com.microsoft.azure.functions.annotation.AuthorizationLevel.FUNCTION
import com.microsoft.azure.functions.annotation.BindingName
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import org.springframework.cloud.function.adapter.azure.FunctionInvoker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class AggregateHandler : FunctionInvoker<String, AggregateResponse>() {
    @FunctionName("getAggregate")
    fun handleGetAggregate(
        @HttpTrigger(
            name = "httpRequestTrigger",
            route = "someAggregate/{id}",
            methods = [GET],
            authLevel = FUNCTION
        )
        request: HttpRequestMessage<String>,
        @BindingName("id") tenantId: String,
        context: ExecutionContext
    ): HttpResponseMessage {
        LoggerProvider.initLogger(context.logger)
        return request.createResponseBuilder(HttpStatus.OK).body(
            handleRequest(tenantId, context)
        ).build()
    }
}

@Configuration
class AggregateHandlerConfiguration {
    @Bean
    fun getAggregate() = { aggregateId: String ->
        logger.info("Received request for aggregate with id $aggregateId")
        AggregateResponse(
            id = aggregateId,
            type = "FUNKY_TYPE"
        )
    }
}

data class AggregateResponse(
    val id: String,
    val type: String,
)
