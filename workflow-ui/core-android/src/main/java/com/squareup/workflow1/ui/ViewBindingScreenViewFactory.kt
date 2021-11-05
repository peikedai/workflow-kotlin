package com.squareup.workflow1.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass

@WorkflowUiExperimentalApi
@PublishedApi
internal class ViewBindingScreenViewFactory<BindingT : ViewBinding, RenderingT : Screen>(
  override val type: KClass<RenderingT>,
  private val bindingInflater: ViewBindingInflater<BindingT>,
  private val runnerConstructor: (BindingT) -> ScreenViewRunner<RenderingT>
) : ScreenViewFactory<RenderingT> {
  override fun buildView(
    initialRendering: RenderingT,
    initialViewEnvironment: ViewEnvironment,
    contextForNewView: Context,
    container: ViewGroup?
  ): View =
    bindingInflater(contextForNewView.viewBindingLayoutInflater(container), container, false)
      .also { binding ->
        val runner = runnerConstructor(binding)
        binding.root.bindShowRendering(
          initialRendering,
          initialViewEnvironment
        ) { rendering, environment ->
          runner.showRendering(rendering, environment)
        }
      }
      .root
}
