package com.squareup.workflow1.ui

import com.google.common.truth.Truth.assertThat
import com.squareup.workflow1.ui.ViewRegistry.Entry
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.test.assertFailsWith

@OptIn(WorkflowUiExperimentalApi::class)
internal class ViewRegistryTest {

  @OptIn(WorkflowUiExperimentalApi::class)
  @Test fun missingBindingMessage_isUseful() {
    val emptyReg = object : ViewRegistry {
      override val keys: Set<KClass<*>> = emptySet()
      override fun <RenderingT : Any> getEntryFor(
        renderingType: KClass<out RenderingT>
      ): Entry<RenderingT>? = null
    }

    val error = assertFailsWith<IllegalArgumentException> {
      emptyReg.buildView("render this, bud")
    }
    assertThat(error.message).isEqualTo(
      "A ViewFactory should have been registered to display " +
        "render this, bud, or that class should implement AndroidViewRendering.")
  }
}
