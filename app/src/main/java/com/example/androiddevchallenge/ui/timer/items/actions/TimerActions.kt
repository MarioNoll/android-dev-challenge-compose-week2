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
package com.example.androiddevchallenge.ui.timer.items.actions

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R

internal typealias TimerActionListener = (type: TimerAction) -> Unit

@Composable
internal fun TimerActions(
    modifier: Modifier,
    displayState: TimerActionDisplayState,
    listener: TimerActionListener
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        val firstButtonType = when (displayState) {
            TimerActionDisplayState.Paused,
            TimerActionDisplayState.Hidden,
            TimerActionDisplayState.Idle -> TimerAction.Start
            TimerActionDisplayState.Active -> TimerAction.Pause
            TimerActionDisplayState.Replay -> TimerAction.Replay
        }

        if (displayState != TimerActionDisplayState.Hidden) {

            TimerActionButton(type = firstButtonType, listener)
        }

        AnimatedVisibility(
            visible = displayState == TimerActionDisplayState.Paused
        ) {
            Row {
                Spacer(modifier = Modifier.size(32.dp))
                TimerActionButton(type = TimerAction.Stop, listener)
            }
        }
    }
}

@Composable
private fun TimerActionButton(
    type: TimerAction,
    listener: TimerActionListener
) {
    IconButton(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colors.primary),
        onClick = { listener(type) }
    ) {
        Icon(
            painter = painterResource(type.icon),
            contentDescription = null,
            tint = Color.White
        )
    }
}

private val TimerAction.icon: Int
    @DrawableRes get() = when (this) {
        TimerAction.Start -> R.drawable.ic_play_arrow
        TimerAction.Pause -> R.drawable.ic_pause
        TimerAction.Stop -> R.drawable.ic_stop
        TimerAction.Replay -> R.drawable.ic_replay
    }
