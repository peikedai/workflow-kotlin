@file:Suppress("DEPRECATION")

package com.squareup.sample.compose.nestedrenderings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.squareup.sample.compose.databinding.LegacyViewBinding
import com.squareup.sample.compose.nestedrenderings.RecursiveWorkflow.LegacyRendering
import com.squareup.workflow1.ui.LayoutRunner
import com.squareup.workflow1.ui.LayoutRunner.Companion.bind
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.ViewFactory
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.compose.tooling.Preview

/**
 * A [LayoutRunner] that renders [LegacyRendering]s using the legacy view framework.
 */
@OptIn(WorkflowUiExperimentalApi::class)
class LegacyRunner(private val binding: LegacyViewBinding) : LayoutRunner<LegacyRendering> {

  override fun showRendering(
    rendering: LegacyRendering,
    viewEnvironment: ViewEnvironment
  ) {
    binding.stub.update(rendering.rendering, viewEnvironment)
  }

  companion object : ViewFactory<LegacyRendering> by bind(
    LegacyViewBinding::inflate, ::LegacyRunner
  )
}

@OptIn(WorkflowUiExperimentalApi::class)
@Preview(widthDp = 200, heightDp = 150, showBackground = true)
@Composable private fun LegacyRunnerPreview() {
  LegacyRunner.Preview(
    rendering = LegacyRendering("child"),
    placeholderModifier = Modifier.fillMaxSize()
  )
}
