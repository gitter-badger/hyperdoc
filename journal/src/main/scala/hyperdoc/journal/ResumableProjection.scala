package hyperdoc.journal

import java.io.{BufferedWriter, File, FileNotFoundException, FileWriter}

import hyperdoc.core.Hyperdoc

import scala.io.Source

class ResumableProjection(id: String)(implicit hyperdoc: Hyperdoc) {
  private val offsetDir = new File(s"${hyperdoc.config.getString("journal.resume.dir")}")
  private val offsetFile = new File(offsetDir, s"$id.offset")

  // create projection offsets file
  if (!offsetDir.exists()) {
    offsetDir.mkdirs()
  }

  var latestOffset: Long = 0L

  try {
    updateOffset(Source.fromFile(offsetFile).mkString.toInt)
  } catch {
    case e: FileNotFoundException => {
      offsetFile.createNewFile()
      saveProgress(0L)
    }
  }

  private def updateOffset(offset: Long): Unit = {
    latestOffset = offset
  }

  def saveProgress(offset: Long): Unit = {
    val bw = new BufferedWriter(new FileWriter(offsetFile))
    bw.write(offset.toString)
    bw.close()
    updateOffset(offset)
  }
}
