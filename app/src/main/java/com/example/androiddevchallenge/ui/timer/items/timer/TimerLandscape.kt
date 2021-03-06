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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
internal fun TimerLandscape(
    viewState: TimerState,
    pickerListener: TimePickerListener,
    actionListener: TimerActionListener,
    headerListener: TimerHeaderListener
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Row {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1F),
                verticalArrangement = Arrangement.Center
            ) {

                Row(
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    TimerHeader(
                        modifier = Modifier
                            .height(80.dp)
                            .fillMaxWidth(),
                        showTimerPickerButton = viewState.isCountdownVisible,
                        listener = headerListener
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    TimerActions(
                        modifier = Modifier.fillMaxWidth(),
                        displayState = viewState.timerActionDisplayState,
                        listener = actionListener
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1F),
                verticalArrangement = Arrangement.Center,
            ) {

                Crossfade(
                    modifier = Modifier
                        .fillMaxSize(),
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
        }
    }
}

@Preview("Timer Landscape", widthDp = 640, heightDp = 360)
@Composable
private fun TimerLandscapePreview() {
    MyTheme {
        ProvideWindowInsets {
            TimerLandscape(
                viewState = TimerState.DEFAULT,
                pickerListener = TimePickerListener.NONE,
                actionListener = {},
                headerListener = {}
            )
        }
    }
}
