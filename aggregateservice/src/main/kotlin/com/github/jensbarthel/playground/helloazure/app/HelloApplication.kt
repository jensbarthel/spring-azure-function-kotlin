package com.github.jensbarthel.playground.helloazure.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ConfigurationPropertiesScan("com.github.jensbarthel.playground.helloazure")
@ComponentScan("com.github.jensbarthel.playground.helloazure")
class HelloApplication

@SuppressWarnings("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<HelloApplication>(*args)
}
