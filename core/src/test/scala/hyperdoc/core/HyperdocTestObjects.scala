package hyperdoc.core

/** Utility class for hyperdoc tests */
object HyperdocTestObjects {
  // test model names
  val TEST_MODEL_NAME = "testModel"
  val TEST_MODEL_VERSION = "1.0"
  val TEST_SCHEMA_NAME = "testSchema"
  val TEST_STRING_PROPERTY_NAME = "testStringProperty"
  val TEST_INTEGER_PROPERTY_NAME = "testIntegerProperty"
  val TEST_DOUBLE_PROPERTY_NAME = "testDoubleProperty"
  val TEST_BOOLEAN_PROPERTY_NAME = "testBooleanProperty"
  val TEST_REFERENCE_PROPERTY_NAME = "testReferenceProperty"

  // test model references
  val TEST_MODEL_REF = Model.generateModelRef(TEST_MODEL_NAME, TEST_MODEL_VERSION)
  val TEST_SCHEMA_REF = Model.generateSchemaRef(TEST_MODEL_REF, TEST_SCHEMA_NAME)
  val TEST_STRING_PROPERTY_REF = Model.generatePropertyRef(TEST_SCHEMA_REF, TEST_STRING_PROPERTY_NAME)
  val TEST_INTEGER_PROPERTY_REF = Model.generatePropertyRef(TEST_SCHEMA_REF, TEST_INTEGER_PROPERTY_NAME)
  val TEST_DOUBLE_PROPERTY_REF = Model.generatePropertyRef(TEST_SCHEMA_REF, TEST_DOUBLE_PROPERTY_NAME)
  val TEST_BOOLEAN_PROPERTY_REF = Model.generatePropertyRef(TEST_SCHEMA_REF, TEST_BOOLEAN_PROPERTY_NAME)
  val TEST_REFERENCE_PROPERTY_REF = Model.generatePropertyRef(TEST_SCHEMA_REF, TEST_REFERENCE_PROPERTY_NAME)

  // test model properties
  val TEST_STRING_PROPERTY_DEFINITION = PropertyDefinition(
    TEST_STRING_PROPERTY_REF,
    TEST_STRING_PROPERTY_NAME,
    StringPropertyType,
    Some("Test String Property"),
    Some("Test String Property"),
    Some(true),
    Some(false),
    None)
  val TEST_INTEGER_PROPERTY_DEFINITION = PropertyDefinition(
    TEST_INTEGER_PROPERTY_REF,
    TEST_INTEGER_PROPERTY_NAME,
    IntegerPropertyType,
    Some("Test Integer Property"),
    Some("Test Integer Property"),
    Some(true),
    Some(false),
    None)
  val TEST_DOUBLE_PROPERTY_DEFINITION = PropertyDefinition(
    TEST_DOUBLE_PROPERTY_REF,
    TEST_DOUBLE_PROPERTY_NAME,
    DoublePropertyType,
    Some("Test Double Property"),
    Some("Test Double Property"),
    Some(true),
    Some(false),
    None)
  val TEST_BOOLEAN_PROPERTY_DEFINITION = PropertyDefinition(
    TEST_BOOLEAN_PROPERTY_REF,
    TEST_BOOLEAN_PROPERTY_NAME,
    BooleanPropertyType,
    Some("Test Boolean Property"),
    Some("Test Boolean Property"),
    Some(true),
    Some(false),
    None)
  val TEST_REFERENCE_PROPERTY_DEFINITION = PropertyDefinition(
    TEST_REFERENCE_PROPERTY_REF,
    TEST_REFERENCE_PROPERTY_NAME,
    ReferencePropertyType,
    Some("Test Reference Property"),
    Some("Test Reference Property"),
    Some(true),
    Some(false),
    None)

  // test model schema
  val TEST_SCHEMA_DEFINITION = SchemaDefinition(
    TEST_SCHEMA_REF,
    "testSchema",
    List(TEST_STRING_PROPERTY_DEFINITION,
      TEST_INTEGER_PROPERTY_DEFINITION,
      TEST_DOUBLE_PROPERTY_DEFINITION,
      TEST_BOOLEAN_PROPERTY_DEFINITION,
      TEST_REFERENCE_PROPERTY_DEFINITION),
    Some("Test Schema"),
    Some("Test Schema"))

  // test model
  val TEST_MODEL_DEFINITION = ModelDefinition(
    TEST_MODEL_REF,
    "testModel",
    "1.0",
    List(TEST_SCHEMA_DEFINITION),
    Some("Test model"))

  val TEST_PARENT_NODE_REF = Node.generateNodeRef
  val TEST_CONTAINER_NODE_REF = Node.generateNodeRef
  val TEST_CONTENT_NODE_REF = Node.generateNodeRef
  val TEST_CONTAINER_NODE = ContainerNode(TEST_CONTAINER_NODE_REF, TEST_PARENT_NODE_REF, Nil, Map(), Nil)
  val TEST_CONTENT_NODE = ContentNode(TEST_CONTENT_NODE_REF, TEST_CONTAINER_NODE_REF, Nil, Map(), Some(TEST_CONTENT_NODE_REF))

  val TEST_BINARY_OBJECT_REF = ContentObject.generateObjectRef
  val TEST_TEXT_OBJECT_REF = ContentObject.generateObjectRef
  val TEST_BINARY_OBJECT = BinaryContentObject(TEST_BINARY_OBJECT_REF, 1, "")
  val TEST_TEXT_OBJECT = TextContentObject(TEST_TEXT_OBJECT_REF, 1, "", "")

  val TEST_USER_REF = Authority.generateUserRef("testUser")
  val TEST_GROUP_REF = Authority.generateGroupRef("testGroup")
  val TEST_USER = User(TEST_USER_REF, "testUser", null, Map(), Nil)
  val TEST_GROUP = Group(TEST_GROUP_REF, "testGroup", None)
}
