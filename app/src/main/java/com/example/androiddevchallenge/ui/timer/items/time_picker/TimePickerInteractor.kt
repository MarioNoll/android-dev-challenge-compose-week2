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
package com.example.androiddevchallenge.ui.timer.items.time_picker

import com.example.androiddevchallenge.ui.timer.items.time_picker.picker.KeypadTimeUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.hours
import kotlin.time.minutes
import kotlin.time.seconds
import kotlin.time.toDuration

internal class TimePickerInteractor : TimePickerListener {

    private val _state = MutableStateFlow(TimePickerState.DEFAULT)

    val state: StateFlow<TimePickerState>
        get() = _state

    fun reset() {
        _state.value = TimePickerState.DEFAULT
    }

    override fun onNumberClicked(number: Int) {
        updateInput { selected, remainder, current, durationUnit ->
            if (selected < 10.toDuration(durationUnit)) {
                selected * 10 + number.toDuration(durationUnit) + remainder
            } else {
                current
            }
        }
    }

    override fun onKeypadDeleteClicked() {
        updateInput { selected, remainder, _, durationUnit ->
            remainder + selected.let {
                if (it >= 10.toDuration(durationUnit)) {
                    (it.toDouble(durationUnit).toInt() / 10)
                        .toDuration(durationUnit)
                } else {
                    Duration.ZERO
                }
            }
        }
    }

    override fun onTimeUnitSelected(timeUnit: KeypadTimeUnit) {
        _state.value = _state.value.copy(
            activeTimeUnit = timeUnit
        )
    }

    private fun updateInput(
        update: (
            selected: Duration,
            remainder: Duration,
            current: Duration,
            keypadDurationUnit: DurationUnit
        ) -> Duration
    ) {
        val (input, activeTimeUnit) = state.value

        val hours = input.inHours.toInt().hours
        val minutes = (input.inMinutes % 60).toInt().minutes
        val seconds = (input.inSeconds % 60).toInt().seconds

        val remainder = when (activeTimeUnit) {
            KeypadTimeUnit.Hours -> minutes + seconds
            KeypadTimeUnit.Minutes -> hours + seconds
            KeypadTimeUnit.Seconds -> hours + minutes
        }

        val selectedDuration = when (activeTimeUnit) {
            KeypadTimeUnit.Hours -> hours
            KeypadTimeUnit.Minutes -> minutes
            KeypadTimeUnit.Seconds -> seconds
        }

        val keypadDurationUnit = when (activeTimeUnit) {
            KeypadTimeUnit.Hours -> DurationUnit.HOURS
            KeypadTimeUnit.Minutes -> DurationUnit.MINUTES
            KeypadTimeUnit.Seconds -> DurationUnit.SECONDS
        }

        val inputUpdated = update(
            selectedDuration,
            remainder,
            input,
            keypadDurationUnit
        )

        val activeTimeUnitUpdated = if (activeTimeUnit == KeypadTimeUnit.Seconds &&
            inputUpdated.inMinutes.toInt() > input.inMinutes.toInt()
        ) {
            KeypadTimeUnit.Minutes
        } else if (activeTimeUnit == KeypadTimeUnit.Minutes &&
            inputUpdated.inHours.toInt() > input.inHours.toInt()
        ) {
            KeypadTimeUnit.Hours
        } else {
            activeTimeUnit
        }

        _state.value = TimePickerState(
            input = inputUpdated,
            activeTimeUnit = activeTimeUnitUpdated
        )
    }
}
