package com.squareup.workflow1.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KClass

/**
 * A [ScreenViewFactory] that creates [View]s that need to be generated from code.
 * (Use [ScreenViewRunner] to work with XML layout resources.)
 *
 *    data class MyView(): AndroidViewRendering<MyView> {
 *      val viewFactory = BuilderViewFactory(
 *          type = MyScreen::class,
 *          viewConstructor = { initialRendering, _, context, _ ->
 *            MyFrame(context).apply {
 *              layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
 *              bindShowRendering(initialRendering, ::update)
 *            }
 *      )
 *    }
 *
 *    private class MyFrame(context: Context) : FrameLayout(context, attributeSet) {
 *      private fun update(rendering:  MyView) { ... }
 *    }
 */
@WorkflowUiExperimentalApi
public class ManualScreenViewFactory<RenderingT : Screen>(
  override val type: KClass<RenderingT>,
  private val viewConstructor: (
    initialRendering: RenderingT,
    initialViewEnvironment: ViewEnvironment,
    contextForNewView: Context,
    container: ViewGroup?
  ) -> View
) : ScreenViewFactory<RenderingT> {
  override fun buildView(
    initialRendering: RenderingT,
    initialViewEnvironment: ViewEnvironment,
    contextForNewView: Context,
    container: ViewGroup?
  ): View = viewConstructor(initialRendering, initialViewEnvironment, contextForNewView, container)
}
