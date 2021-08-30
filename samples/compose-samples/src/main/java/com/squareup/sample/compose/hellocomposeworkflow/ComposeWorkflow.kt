package com.squareup.sample.compose.hellocomposeworkflow

import androidx.compose.runtime.Composable
import com.squareup.workflow1.RenderContext
import com.squareup.workflow1.Sink
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.Workflow
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.compose.ComposeRendering

/**
 * A stateless [Workflow] that [renders][RenderingContent] itself as a [Composable] function.
 * Effectively defines an inline [ComposeRendering].
 *
 * This workflow does not have access to a [RenderContext] since render contexts are only valid
 * during render passes, and this workflow's [RenderingContent] method is invoked after the render
 * pass, when view bindings are being shown.
 *
 * While this workflow is "stateless" in the usual workflow sense (it doesn't have a `StateT` type),
 * since [RenderingContent] is a Composable function, it can use all the usual Compose facilities
 * for state management.
 *
 * # CAVEAT
 *
 * We don't know if this is a useful/helpful pattern, or a pattern that will be confusing and cause
 * trouble. However, it seems interesting, so we wanted to be able to point to it if a use case
 * comes up.
 */
@WorkflowUiExperimentalApi
public abstract class ComposeWorkflow<in PropsT, out OutputT : Any> :
  Workflow<PropsT, OutputT, ComposeRendering> {

  /**
   * Renders [props] by emitting Compose UI. This function will be called to update the UI whenever
   * the [props] or [viewEnvironment] change.
   *
   * @param props The data to render.
   * @param outputSink A [Sink] that can be used from UI event handlers to send outputs to this
   * workflow's parent.
   * @param viewEnvironment The [ViewEnvironment] passed down through the `ViewBinding` pipeline.
   */
  @Composable public abstract fun RenderingContent(
    props: PropsT,
    outputSink: Sink<OutputT>,
    viewEnvironment: ViewEnvironment
  )

  override fun asStatefulWorkflow(): StatefulWorkflow<PropsT, *, OutputT, ComposeRendering> =
    ComposeWorkflowImpl(this)
}

/**
 * Returns a [ComposeWorkflow] that renders itself using the given [render] function.
 */
@WorkflowUiExperimentalApi
public inline fun <PropsT, OutputT : Any> Workflow.Companion.composed(
  crossinline render: @Composable (
    props: PropsT,
    outputSink: Sink<OutputT>,
    environment: ViewEnvironment
  ) -> Unit
): ComposeWorkflow<PropsT, OutputT> = object : ComposeWorkflow<PropsT, OutputT>() {
  @Composable override fun RenderingContent(
    props: PropsT,
    outputSink: Sink<OutputT>,
    viewEnvironment: ViewEnvironment
  ) {
    render(props, outputSink, viewEnvironment)
  }
}