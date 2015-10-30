package hyperdoc.core.bootstrap

import java.io.File

import hyperdoc.core.Hyperdoc
import hyperdoc.core.utils._

import scala.collection.JavaConversions._
import scala.util.{Failure, Success, Try}

/** Model bootstrap */
class ModelBootstrap(implicit hyperdoc: Hyperdoc) extends HyperdocBootstrap {
  def bootstrap: Try[Unit] = {
    // read model files
    val files = hyperdoc.config.getStringList("models").toList map ((modelPath: String) => {
      try {
        Success(new File(getClass.getClassLoader.getResource(modelPath).getFile))
      } catch {
        case e: Throwable => Failure(e)
      }
    })

    val result = files.sequence match {
      case Success(modelFiles) => {
        val importResult = modelFiles map ((modelFile: File) => for {
          modelDefinition1 <- ModelParser.importFromJsonFile(modelFile)
          modelDefinition2 <- hyperdoc.modelStore.saveModel(modelDefinition1, true)
        } yield modelDefinition2)
        importResult.sequence
      }
      case Failure(e) => Failure(e)
    }

    result match {
      case Success(_) => Success(())
      case Failure(e) => Failure(e)
    }
  }
}
