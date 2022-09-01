package com.github.jensbarthel.safk.aggregateservice.adapter.api

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
import org.springframework.messaging.Message

class AggregateHandler : FunctionInvoker<String, String>() {
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
        return request.createResponseBuilder(HttpStatus.OK).body(
            handleRequest(tenantId, context)
        ).build()
    }
}

@Configuration
class AggregateHandlerConfiguration {
    @Bean
    fun getAggregate() = { message: Message<String> ->
        val context = message.headers["executionContext"] as ExecutionContext
        val aggregateId = message.payload
        context.logger.info("Received request for aggregate with id $aggregateId")
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
