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
package com.example.androiddevchallenge.ui.timer.items.countdown

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration
import kotlin.time.TimeSource

internal class CountdownInteractor {
    private val _state = MutableStateFlow(CountdownState.DEFAULT)

    val state: StateFlow<CountdownState>
        get() = _state

    fun initialize(interval: Duration) {
        _state.value = CountdownState.DEFAULT.copy(
            interval = interval
        )
    }

    fun reset() {
        _state.value = CountdownState.DEFAULT
    }

    suspend fun start() {
        val started = TimeSource.Monotonic.markNow()
        val initialCountdown = _state.value
        val interval = initialCountdown.interval
        val elapsedInitial = initialCountdown.elapsed

        while (_state.value.elapsed < interval) {

            val remaining = (interval - (started.elapsedNow() + elapsedInitial))
                .coerceAtLeast(Duration.ZERO)

            val elapsed = interval - remaining

            _state.value = state.value.copy(
                progress = (elapsed / interval).toFloat(),
                elapsed = elapsed
            )

            delay(16)
        }
    }
}
