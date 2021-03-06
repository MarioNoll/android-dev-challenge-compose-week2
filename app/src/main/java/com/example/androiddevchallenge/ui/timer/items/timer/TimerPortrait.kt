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
package com.example.androiddevchallenge.ui.timer.items.timer

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.timer.TimerState
import com.example.androiddevchallenge.ui.timer.items.actions.TimerActionListener
import com.example.androiddevchallenge.ui.timer.items.actions.TimerActions
import com.example.androiddevchallenge.ui.timer.items.countdown.Countdown
import com.example.androiddevchallenge.ui.timer.items.header.TimerHeader
import com.example.androiddevchallenge.ui.timer.items.header.TimerHeaderListener
import com.example.androiddevchallenge.ui.timer.items.time_picker.TimePickerListener
import com.example.androiddevchallenge.ui.timer.items.time_picker.picker.TimePicker
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

@Composable
internal fun TimerPortrait(
    viewState: TimerState,
    pickerListener: TimePickerListener,
    actionListener: TimerActionListener,
    headerListener: TimerHeaderListener
) {
    Column {

        Box(
            modifier = Modifier
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            TimerHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                showTimerPickerButton = viewState.isCountdownVisible,
                listener = headerListener
            )
        }

        val actionButtonsBoxHeight = 108.dp

        BoxWithConstraints {
            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(minOf(400.dp, maxHeight - actionButtonsBoxHeight)),
                targetState = viewState.isCountdownVisible
            ) { showCountdown ->

                if (showCountdown) {
                    Countdown(
                        modifier = Modifier.fillMaxSize(),
                        state = viewState.countdownState
                    )
                } else {
                    TimePicker(
                        modifier = Modifier.fillMaxSize(),
                        viewState = viewState.timePickerViewState,
                        listener = pickerListener
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .height(actionButtonsBoxHeight)
                .padding(32.dp),
            contentAlignment = Alignment.Center,
        ) {

            TimerActions(
                modifier = Modifier.fillMaxWidth(),
                displayState = viewState.timerActionDisplayState,
                listener = actionListener
            )
        }
    }
}

@Preview("Timer Landscape", widthDp = 360, heightDp = 640)
@Composable
private fun TimerLandscapePreview() {
    MyTheme {
        ProvideWindowInsets {
            TimerPortrait(
                viewState = TimerState.DEFAULT,
                pickerListener = TimePickerListener.NONE,
                actionListener = {},
                headerListener = {}
            )
        }
    }
}
