package org.gbif.ipt.utils

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.Reader
import java.io.Writer
import java.nio.charset.StandardCharsets

import org.apache.commons.io.FileUtils

@Throws(FileNotFoundException::class)
fun getUtf8Reader(file: File): Reader {
  val reader: Reader
  reader = BufferedReader(InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8))
  return reader
}

@Throws(IOException::class)
fun startNewUtf8File(file: File): Writer {
  try {
    FileUtils.touch(file)
  } catch (e: IOException) {
    // io error can happen on windows if last modification cannot be set
    // see http://commons.apache.org/io/api-1.4/org/apache/commons/io/FileUtils.html#touch(java.io.File)
    // we catch this and check if the file was created
    if (file.exists() && file.canWrite()) {
      // TODO: 12/05/2022 log exception
    } else {
      throw e
    }
  }
  return BufferedWriter(OutputStreamWriter(FileOutputStream(file, false), StandardCharsets.UTF_8))
}
