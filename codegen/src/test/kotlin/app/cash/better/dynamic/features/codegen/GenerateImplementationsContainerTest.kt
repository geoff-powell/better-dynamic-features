package app.cash.better.dynamic.features.codegen

import app.cash.better.dynamic.features.codegen.api.FeatureApi
import app.cash.better.dynamic.features.codegen.api.FeatureImplementation
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GenerateImplementationsContainerTest {
  @Test
  fun `codegen for an api and implementations`() {
    val api = FeatureApi("test", "TestApi")
    val implementations = listOf(
      FeatureImplementation("test.TestImplementation1", api),
      FeatureImplementation("test.TestImplementation2", api),
    )

    val spec = generateImplementationsContainer(forApi = api, implementations)
    assertThat(spec.toString())
      .isEqualTo(
        """
        package test

        import app.cash.better.`dynamic`.features.ExperimentalDynamicFeaturesApi
        import app.cash.better.`dynamic`.features.ImplementationsContainer
        import kotlin.OptIn
        import kotlin.collections.List

        @OptIn(ExperimentalDynamicFeaturesApi::class)
        public object TestApiImplementationsContainer : ImplementationsContainer<TestApi> {
          public override val implementations: List<TestApi> = buildList {
          add(Class.forName("test.TestImplementation1").getDeclaredConstructor().newInstance() as TestApi)
          add(Class.forName("test.TestImplementation2").getDeclaredConstructor().newInstance() as TestApi)
          }

        }

        """.trimIndent(),
      )
  }
}