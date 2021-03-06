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

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.time.Duration

internal fun Duration.formatCountdown(): String {
    val seconds = BigDecimal(inSeconds % 60)
        .roundDuration(ceilingZone = BigDecimal.ONE)
        .let { if (it >= 60) 0 else it }
        .formatDuration()

    val minutes = BigDecimal(inMinutes % 60)
        .roundDuration(ceilingZone = BigDecimal(1 / 60.0))
        .let { if (it >= 60) 0 else it }
        .formatDuration()

    val hours = BigDecimal(inHours)
        .roundDuration(ceilingZone = BigDecimal(1 / 60.0 / 60.0))
        .formatDuration(leadingZero = false)

    return "$hours:$minutes:$seconds"
}

private fun BigDecimal.roundDuration(ceilingZone: BigDecimal): Int {
    val ceil = setScale(0, RoundingMode.CEILING)
    val floor = setScale(0, RoundingMode.FLOOR)

    return (if (ceil - this <= ceilingZone) ceil else floor)
        .toInt()
}

private fun Int.formatDuration(leadingZero: Boolean = true) =
    if (leadingZero && this < 10) "0$this" else toString()
