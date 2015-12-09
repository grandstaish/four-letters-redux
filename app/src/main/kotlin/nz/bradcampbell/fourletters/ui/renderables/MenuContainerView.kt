package nz.bradcampbell.fourletters.ui.renderables

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import nz.bradcampbell.fourletters.App
import nz.bradcampbell.fourletters.R
import nz.bradcampbell.fourletters.redux.action.ActionCreator
import nz.bradcampbell.fourletters.redux.state.State
import nz.bradcampbell.fourletters.ui.Renderable
import javax.inject.Inject

class MenuContainerView(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs), Renderable {
    @Inject lateinit var actionCreator: ActionCreator

    init {
        val app = context?.applicationContext as App
        app.getAppComponent().inject(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById(R.id.playButton).setOnClickListener {
            actionCreator.initiateGame()
        }
    }

    override fun render(state: State) {

    }
}