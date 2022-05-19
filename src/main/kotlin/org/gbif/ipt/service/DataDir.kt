package org.gbif.ipt.service

import org.gbif.ipt.utils.CONFIG_DIR
import org.gbif.ipt.utils.TMP_DIR

import java.io.File
import java.io.IOException

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory

open class DataDir {

  private var dataDir: File? = null
  private var dataDirSettingFile: File? = null

  companion object {

    private val logger = LoggerFactory.getLogger(DataDir::class.java)

    fun buildFromLocationFile(dataDirSettingFile: File?): DataDir {
      val dd = DataDir()
      dd.dataDirSettingFile = dataDirSettingFile
      if (dataDirSettingFile != null && dataDirSettingFile.exists()) {
        val dataDirPath: String?
        try {
          dataDirPath = StringUtils.trimToNull(FileUtils.readFileToString(dataDirSettingFile, "UTF-8"))
          if (dataDirPath != null) {
            logger.info("IPT Data Directory configured at $dataDirPath")
            dd.dataDir = File(dataDirPath)
          }
        } catch (e: IOException) {
          logger.error(
            "Failed to read the IPT Data Directory location settings file in WEB-INF at " + dataDirSettingFile.absolutePath,
            e
          )
        }
      } else {
        logger.warn("IPT Data Directory location settings file in WEB-INF not found. Continue without data directory.")
      }
      return dd
    }
  }

  fun configFile(path: String): File? {
    return dataFile("$CONFIG_DIR/$path")
  }

  fun dataFile(path: String?): File? {
    checkNotNull(dataDir) { "No data dir has been configured yet" }
    val f = File(dataDir, path!!)
    assureParentExists(f)
    return f
  }

  fun isConfigured(): Boolean {
    return dataDir != null
        && dataDir!!.isDirectory
        && dataDir!!.list() != null
        && dataDir!!.list().isEmpty()
  }

  fun tmpFile(path: String): File? {
    return dataFile("$TMP_DIR/$path")
  }

  @Throws(IOException::class)
  fun clearTmp() {
    val tmpDir: File? = tmpFile("")
    FileUtils.forceMkdir(tmpDir)
    FileUtils.cleanDirectory(tmpDir)
    logger.debug("Cleared temporary folder")
  }

  private fun assureParentExists(f: File?) {
    if (f != null && !f.parentFile.exists()) {
      f.parentFile.mkdirs()
    }
  }
}
