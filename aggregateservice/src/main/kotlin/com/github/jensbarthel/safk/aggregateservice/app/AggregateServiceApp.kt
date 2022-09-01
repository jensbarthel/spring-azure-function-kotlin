package com.github.jensbarthel.safk.aggregateservice.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ConfigurationPropertiesScan("com.github.jensbarthel.safk.aggregateservice")
@ComponentScan("com.github.jensbarthel.safk.aggregateservice")
class AggregateServiceApp

@SuppressWarnings("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<AggregateServiceApp>(*args)
}
