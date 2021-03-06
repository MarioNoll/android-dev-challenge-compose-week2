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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

internal fun DrawScope.drawDecorations() {
    val topLeftCircleRadius = size.width / 3F
    drawCircle(
        color = Color(0xFF632851),
        radius = topLeftCircleRadius,
        center = Offset(
            x = topLeftCircleRadius * 0.4F,
            y = topLeftCircleRadius * 0.3F
        )
    )

    val centerRightCircleRadius = size.height / 6F
    drawCircle(
        color = Color(0xFF73325F),
        radius = centerRightCircleRadius,
        style = Stroke(width = 78.dp.value),
        center = Offset(
            x = size.width + centerRightCircleRadius / 3F,
            y = center.y
        )
    )

    val centerLeftCircleRadius = size.height / 5F
    drawCircle(
        color = Color(0xFF6F2F5B),
        radius = centerLeftCircleRadius,
        center = Offset(
            x = centerLeftCircleRadius * 0.2F,
            y = center.y
        )
    )

    val bottomRightCircleRadius = size.height / 4F
    drawCircle(
        color = Color(0xFF702F5B),
        radius = bottomRightCircleRadius,
        center = Offset(
            x = center.x + bottomRightCircleRadius / 2F,
            y = size.height - bottomRightCircleRadius / 2F
        )
    )

    val bottomLeftCircleRadius = size.height / 6F
    drawCircle(
        color = Color(0xFF6F2E5B),
        radius = bottomLeftCircleRadius,
        center = Offset(
            x = bottomLeftCircleRadius / 2F,
            y = size.height - bottomLeftCircleRadius / 2F
        )
    )
}
