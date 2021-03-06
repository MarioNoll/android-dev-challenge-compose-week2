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

import androidx.annotation.FloatRange
import kotlin.time.Duration

internal data class CountdownState(
    @FloatRange(from = 0.0, to = 1.0)
    val progress: Float,
    val elapsed: Duration,
    val interval: Duration
) {
    init {
        require(interval >= elapsed)
    }

    companion object {
        val DEFAULT = CountdownState(
            progress = 0F,
            elapsed = Duration.ZERO,
            interval = Duration.ZERO
        )
    }
}
