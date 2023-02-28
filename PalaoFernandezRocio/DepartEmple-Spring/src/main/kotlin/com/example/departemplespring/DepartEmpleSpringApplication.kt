package com.example.departemplespring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class DepartEmpleSpringApplication

fun main(args: Array<String>) {
    runApplication<DepartEmpleSpringApplication>(*args)
}
