package nz.bradcampbell.fourletters.core

import nz.bradcampbell.fourletters.R
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

public class ActionCreator @Inject constructor(val store: Store, val wordRepository: WordRepository) {

    public fun initiateGame() {
        store.dispatch(Action.Navigate(Page(R.layout.game)))
        wordRepository.getRandomWord()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                store.dispatch(Action.InitGame( it ))
            }
    }

    public fun playAgain() {
        wordRepository.getRandomWord()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                store.dispatch(Action.Back)
                store.dispatch(Action.Navigate(Page(R.layout.game)))
                store.dispatch(Action.InitGame( it ))
            }
    }

    public fun tick() {
        store.dispatch(Action.Tick)
        checkLose()
    }

    public fun back() = store.dispatch(Action.Back)

    public fun leftLetterPressed() {
        letterPressed(Action.LeftPressed)
    }

    public fun topLetterPressed() {
        letterPressed(Action.TopPressed)
    }

    public fun rightLetterPressed() {
        letterPressed(Action.RightPressed)
    }

    public fun bottomLetterPressed() {
        letterPressed(Action.BottomPressed)
    }

    private fun letterPressed(action: Action) {
        store.dispatch(action)
        checkWin()
    }

    private fun checkWin() {
        val gameState = store.state.gameState!!
        val answer = gameState.answer
        if (answer.size == 4) {
            val stringAnswer = answer.map { it.letter }.joinToString("")
            if (gameState.possibleAnswers.contains(stringAnswer)) {
                store.dispatch(Action.BumpScore)
                wordRepository.getRandomWord()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        store.dispatch(Action.NextGame( it, 15000 ))
                    }
            } else {
                store.dispatch(Action.ResetGame)
            }
        }
    }

    private fun checkLose() {
        var gameState = store.state.gameState
        gameState?.let {
            if (gameState.timeRemaining <= 0) {
                store.dispatch(Action.Navigate(Page(R.layout.lose), false))
            }
        }
    }
}