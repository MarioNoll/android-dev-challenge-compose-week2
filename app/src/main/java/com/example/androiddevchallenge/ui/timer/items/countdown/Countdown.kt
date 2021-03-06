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

import android.media.MediaPlayer
import android.provider.Settings
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.time.milliseconds

@Composable
internal fun Countdown(
    state: CountdownViewState,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colors.primary
    val primaryVariant = MaterialTheme.colors.primaryVariant
    val secondaryVariant = MaterialTheme.colors.secondaryVariant
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)
    }

    BoxWithConstraints(
        modifier = modifier
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        var latestProgress by rememberSaveable { mutableStateOf(state.progress) }
        var drawComponents by remember { mutableStateOf(true) }

        DisposableEffect(state.progress to latestProgress) {
            var animJob: Job? = null
            if (state.progress == 1F && latestProgress != 1F) {
                mediaPlayer.start()

                animJob = scope.launch {
                    repeat(10) {
                        drawComponents = !drawComponents
                        delay(200.milliseconds)
                    }
                    latestProgress = 1F
                }
            } else {
                latestProgress = state.progress
            }

            onDispose {
                animJob?.cancel()
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    mediaPlayer.prepare()
                }
                drawComponents = true
                latestProgress = state.progress
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxHeight()
                .width(minOf(maxWidth, 300.dp)),
            onDraw = {
                val circleStrokeWidth = (if (maxWidth <= 300.dp) 32.dp else 64.dp).value
                val lineIndicatorLength = circleStrokeWidth * 0.4F
                val lineIndicatorMargin = 24.dp.value
                val radius =
                    (size.minDimension - circleStrokeWidth) / 2F - (lineIndicatorLength + lineIndicatorMargin)

                this.drawCircle(
                    color = secondaryVariant,
                    radius = radius,
                    style = Stroke(width = circleStrokeWidth)
                )

                val indicatorLineStartRadius = radius + circleStrokeWidth / 2F + lineIndicatorMargin
                val indicatorLineEndRadius = indicatorLineStartRadius + lineIndicatorLength

                if (drawComponents) {
                    (0..59).forEach { minute ->
                        val progress = minute / 60F
                        val angle = Math.toRadians(progress * 360.0 - 90).toFloat()
                        val alpha = if (progress <= state.progress) 1F else 0.5F

                        drawLine(
                            color = primaryVariant.copy(alpha = alpha),
                            start = Offset(
                                x = center.x + indicatorLineStartRadius * cos(angle),
                                y = center.y + indicatorLineStartRadius * sin(angle),
                            ),
                            end = Offset(
                                x = center.x + indicatorLineEndRadius * cos(angle),
                                y = center.y + indicatorLineEndRadius * sin(angle),
                            )
                        )
                    }
                }

                if (state.progress >= 0F) {
                    drawArc(
                        color = primary,
                        startAngle = 270F,
                        sweepAngle = 360 * state.progress,
                        useCenter = false,
                        topLeft = Offset(
                            x = center.x - radius,
                            y = center.y - radius
                        ),
                        size = Size(width = radius * 2, height = radius * 2),
                        style = Stroke(width = circleStrokeWidth)
                    )
                }
            }
        )

        if (drawComponents) {
            Text(
                text = state.countdown,
                style = MaterialTheme.typography.h4,
            )
        }
    }
}

@Preview("Countdown", widthDp = 360, heightDp = 640)
@Composable
private fun CountdownPreview() {
    MyTheme {
        Countdown(
            state = CountdownViewState.DEFAULT
        )
    }
}
