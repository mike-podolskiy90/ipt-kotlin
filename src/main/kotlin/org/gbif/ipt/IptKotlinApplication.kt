package org.gbif.ipt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IptKotlinApplication

fun main(args: Array<String>) {
  runApplication<IptKotlinApplication>(*args)
}
