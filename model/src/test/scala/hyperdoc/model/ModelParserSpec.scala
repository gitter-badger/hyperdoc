package hyperdoc.model

import java.io.File

import org.scalatest.TryValues._
import org.scalatest.{FlatSpec, Matchers}

/**
 * Tests for [[hyperdoc.model.ModelParser]]
 */
class ModelParserSpec extends FlatSpec with Matchers {
  /**
   * Test a valid model.
   *
   * When importing the 'testModel.json' file, the output should be equal to the model hardcoded above.
   */
  "A valid model" should s"be imported successfully" in {
    // read file from classpath
    val file = getClass.getClassLoader.getResource("hyperdoc/model/testModel.json").getFile

    // import valid model
    val result = ModelParser.importFromJsonFile(new File(file))
    result.isSuccess shouldEqual true

    // imported model should be equal to the model hardcoded
    //    val modelDefinition = result.get
    //    modelDefinition shouldEqual HyperdocTestObjects.TEST_MODEL_DEFINITION
  }

  /**
   * Test an invalid model.
   *
   * When importing the 'invalidTestModel.json' file, an exception should thrown complaining
   * about an invalid property type.
   */
  "An invalid model" should s"raise an exception when imported" in {
    val file = getClass.getClassLoader.getResource("hyperdoc/model/invalidTestModel.json").getFile

    // import invalid model
    ModelParser.importFromJsonFile(new File(file)).failure.exception should have message
      "Property 'hyperdoc://model/invalidTestModel/1.0/testSchema#testStringProperty' has an invalid type 'invalid'"
  }

  /**
   * Test a malformed json model.
   *
   * When importing a malformed json model, an exception should thrown complaining
   * about an invalid JSON.
   */
  "An malformed json model" should s"raise an exception when imported" in {
    val json =
      """
    { "name" : "TestModel"
      """

    // import invalid model
    ModelParser.importFromJson(json).failure.exception should have message
      "JSON terminates unexpectedly"
  }
}