/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.ui.timer.items.actions.TimerAction
import com.example.androiddevchallenge.ui.timer.items.actions.TimerActionDisplayState
import com.example.androiddevchallenge.ui.timer.items.countdown.CountdownInteractor
import com.example.androiddevchallenge.ui.timer.items.countdown.CountdownState
import com.example.androiddevchallenge.ui.timer.items.countdown.CountdownViewState
import com.example.androiddevchallenge.ui.timer.items.countdown.formatCountdown
import com.example.androiddevchallenge.ui.timer.items.time_picker.TimePickerInteractor
import com.example.androiddevchallenge.ui.timer.items.time_picker.TimePickerListener
import com.example.androiddevchallenge.ui.timer.items.time_picker.TimePickerState
import com.example.androiddevchallenge.ui.timer.items.time_picker.picker.TimePickerViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.time.Duration

internal class TimerViewModel(
    private val timePickerInteractor: TimePickerInteractor = TimePickerInteractor(),
    private val countdownInteractor: CountdownInteractor = CountdownInteractor()
) : ViewModel(),
    TimePickerListener by timePickerInteractor {

    private val timerActionState = MutableStateFlow<TimerAction?>(null)
    private val isCountdownVisibleState = MutableStateFlow(false)
    private var timerJob: Job? = null

    private val _state = MutableStateFlow(TimerState.DEFAULT)
    val state: StateFlow<TimerState>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                isCountdownVisibleState,
                timePickerInteractor.state,
                countdownInteractor.state,
                timerActionState
            ) { isCountdownVisible, timePickerState, countdownState, _ ->

                val timerActionDisplayState = if (timePickerState.input == Duration.ZERO) {
                    TimerActionDisplayState.Hidden
                } else if (!isCountdownVisible) {
                    TimerActionDisplayState.Idle
                } else if (countdownState.progress == 1F) {
                    TimerActionDisplayState.Replay
                } else if (timerJob?.isActive == true) {
                    TimerActionDisplayState.Active
                } else {
                    TimerActionDisplayState.Paused
                }

                TimerState(
                    timePickerViewState = timePickerState.toViewState(),
                    countdownState = countdownState.toViewState(),
                    isCountdownVisible = isCountdownVisible,
                    timerActionDisplayState = timerActionDisplayState
                )
            }.collect {
                _state.value = it
            }
        }
    }

    fun onTimerAction(type: TimerAction) {
        when (type) {
            TimerAction.Start -> start(initialize = _state.value.isTimePickerVisible)
            TimerAction.Pause -> pause()
            TimerAction.Stop -> stop()
            TimerAction.Replay -> start(initialize = true)
        }

        timerActionState.value = type
    }

    fun showTimerPicker() {
        timerJob?.cancel()
        isCountdownVisibleState.value = false
    }

    private fun start(initialize: Boolean) {
        if (initialize) {
            countdownInteractor.initialize(timePickerInteractor.state.value.input)
        }
        isCountdownVisibleState.value = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            countdownInteractor.start()
        }
    }

    private fun stop() {
        timerJob?.cancel()
        showTimerPicker()
        timePickerInteractor.reset()
        countdownInteractor.reset()
    }

    private fun pause() {
        timerJob?.cancel()
    }

    private fun CountdownState.toViewState() = CountdownViewState(
        progress = (elapsed / interval).toFloat(),
        countdown = (interval - elapsed).formatCountdown()
    )

    private fun TimePickerState.toViewState() = TimePickerViewState(
        hours = input.inHours.format(),
        minutes = (input.inMinutes % 60).format(),
        seconds = (input.inSeconds % 60).format(),
        activeTimeUnit = activeTimeUnit
    )

    private fun Double.format(): String = toInt()
        .let { if (it < 10) "0$it" else it.toString() }
}
