@file:Suppress("DEPRECATION")

package com.squareup.sample.compose.inlinerendering

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.workflow1.ui.WorkflowLayout
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.renderWorkflowIn
import kotlinx.coroutines.flow.StateFlow

/**
 * A workflow that returns an anonymous `ComposeRendering`.
 */
class InlineRenderingActivity : AppCompatActivity() {
  @OptIn(WorkflowUiExperimentalApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val model: HelloBindingModel by viewModels()
    setContentView(
      WorkflowLayout(this).apply {
        start(renderings = model.renderings)
      }
    )
  }

  class HelloBindingModel(savedState: SavedStateHandle) : ViewModel() {
    @OptIn(WorkflowUiExperimentalApi::class)
    val renderings: StateFlow<Any> by lazy {
      renderWorkflowIn(
        workflow = InlineRenderingWorkflow,
        scope = viewModelScope,
        savedStateHandle = savedState
      )
    }
  }
}
