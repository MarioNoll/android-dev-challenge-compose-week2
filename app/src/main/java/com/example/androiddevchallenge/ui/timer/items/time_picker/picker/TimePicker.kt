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
package com.example.androiddevchallenge.ui.timer.items.time_picker.picker

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.timer.items.time_picker.TimePickerListener
import com.example.androiddevchallenge.ui.timer.items.time_picker.keypad.Keypad

@Composable
internal fun TimePicker(
    modifier: Modifier = Modifier,
    viewState: TimePickerViewState,
    listener: TimePickerListener
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            TimeUnitSelector(
                time = viewState.hours,
                typeKeypad = KeypadTimeUnit.Hours,
                active = viewState.activeTimeUnit,
                listener = listener
            )

            Separator()

            TimeUnitSelector(
                time = viewState.minutes,
                typeKeypad = KeypadTimeUnit.Minutes,
                active = viewState.activeTimeUnit,
                listener = listener
            )

            Separator()

            TimeUnitSelector(
                time = viewState.seconds,
                typeKeypad = KeypadTimeUnit.Seconds,
                active = viewState.activeTimeUnit,
                listener = listener
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Keypad(listener = listener)
    }
}

@Composable
private fun TimeUnitSelector(
    time: String,
    typeKeypad: KeypadTimeUnit,
    active: KeypadTimeUnit?,
    listener: TimePickerListener
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                listener.onTimeUnitSelected(typeKeypad)
            },
            color = if (active == typeKeypad) MaterialTheme.colors.primary else Color.Unspecified,
            text = time,
            style = MaterialTheme.typography.h3
        )

        Text(
            modifier = Modifier.alpha(0.5F),
            text = stringResource(typeKeypad.text)
        )
    }
}

@Composable
private fun Separator() {
    Spacer(modifier = Modifier.width(16.dp))
    Text(text = ":")
    Spacer(modifier = Modifier.width(16.dp))
}

private val KeypadTimeUnit.text: Int
    @StringRes get() = when (this) {
        KeypadTimeUnit.Hours -> R.string.time_short_hours
        KeypadTimeUnit.Minutes -> R.string.time_short_minutes
        KeypadTimeUnit.Seconds -> R.string.time_short_seconds
    }

internal enum class KeypadTimeUnit {
    Hours,
    Minutes,
    Seconds
}

@Preview("Time Selector", widthDp = 360, heightDp = 640)
@Composable
private fun TimeSelectorPreview() {
    MyTheme {
        TimePicker(
            viewState = TimePickerViewState.DEFAULT,
            listener = TimePickerListener.NONE
        )
    }
}
