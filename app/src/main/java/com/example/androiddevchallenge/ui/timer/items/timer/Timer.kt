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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.timer.TimerState
import com.example.androiddevchallenge.ui.timer.items.actions.TimerActionListener
import com.example.androiddevchallenge.ui.timer.items.header.TimerHeaderListener
import com.example.androiddevchallenge.ui.timer.items.time_picker.TimePickerListener
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

@Composable
internal fun Timer(
    viewState: TimerState,
    pickerListener: TimePickerListener,
    actionListener: TimerActionListener,
    headerListener: TimerHeaderListener
) {
    Surface {
        val statusBarPadding = LocalDensity.current.run {
            LocalWindowInsets.current.statusBars.top.toDp()
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.secondary)
                .padding(top = statusBarPadding)
                .drawBehind { drawDecorations() },
            contentAlignment = Alignment.Center,
        ) {

            if (maxWidth > maxHeight) {
                TimerLandscape(
                    viewState = viewState,
                    pickerListener = pickerListener,
                    actionListener = actionListener,
                    headerListener = headerListener
                )
            } else {
                TimerPortrait(
                    viewState = viewState,
                    pickerListener = pickerListener,
                    actionListener = actionListener,
                    headerListener = headerListener
                )
            }
        }
    }
}

@Preview("Timer View", widthDp = 360, heightDp = 640)
@Composable
private fun TimerViewPreview() {
    MyTheme {
        ProvideWindowInsets {
            Timer(
                viewState = TimerState.DEFAULT,
                pickerListener = TimePickerListener.NONE,
                actionListener = {},
                headerListener = {}
            )
        }
    }
}
