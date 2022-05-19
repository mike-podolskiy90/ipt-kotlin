package org.gbif.ipt.config

import org.gbif.ipt.service.DataDir

import java.io.File
import java.io.IOException

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.util.ResourceUtils

@Lazy
@Configuration
class DataDirConfig {

  private val logger = LoggerFactory.getLogger(DataDirConfig::class.java)

  @Bean
  fun dataDir(): DataDir {
    val dataDirSettingFile: File = ResourceUtils.getFile("classpath:datadir.location")
    val dd = DataDir.buildFromLocationFile(dataDirSettingFile)

    try {
      if (dd.isConfigured()) {
        dd.clearTmp()
      }
    } catch (e: IOException) {
      logger.warn("Couldn't clear temporary data dir folder", e)
    }

    return dd
  }
}
