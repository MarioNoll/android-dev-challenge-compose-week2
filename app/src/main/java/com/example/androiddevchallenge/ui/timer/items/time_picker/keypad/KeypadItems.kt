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
package com.example.androiddevchallenge.ui.timer.items.time_picker.keypad

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R

@Composable
internal fun KeypadNumber(
    modifier: Modifier,
    number: Int,
    listener: (number: Int) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        ClickableKeypadItem(onClick = { listener(number) }) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.h5
            )
        }
    }
}

@Composable
internal fun KeypadDelete(modifier: Modifier, onDelete: () -> Unit) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        ClickableKeypadItem(onClick = onDelete) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.ic_backspace),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ClickableKeypadItem(onClick: () -> Unit, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                val size = minOf(constraints.maxHeight, constraints.maxWidth)
                layout(width = size, height = size) {
                    placeable.place(
                        position = IntOffset(
                            x = size / 2 - placeable.width / 2,
                            y = size / 2 - placeable.height / 2,
                        )
                    )
                }
            },
    ) {
        content()
    }
}
