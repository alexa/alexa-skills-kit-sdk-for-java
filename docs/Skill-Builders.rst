=====
Skill Builders
=====

The SDK includes two Skill builders that help you construct Skill
instances.

The
`CustomSkillBuilder <http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/builder/CustomSkillBuilder.html>`__
is included with the core SDK module and allows you to configure and
build a complete instance of a Skill. This includes builder methods for
adding request handlers, exception handlers, setting your skill ID, and
providing implementations of ``ApiClient`` and ``PersistenceAdapter``.

The
`StandardSkillBuilder <http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/builder/StandardSkillBuilder.html>`__
is included with the standard SDK distribution and additionally provides
builder methods for features included in the standard distribution. This
includes the Apache HTTP Client API Client plugin as well as the
DynamoDBPersistenceAdapter. For example, instead of providing the Dynamo
adapter explicitly, the builder has methods such as ``withTableName`` to
provide this configuration directly and automatically configure the
adapter. The Apache API client is also automatically configured when
using the standard builder.
