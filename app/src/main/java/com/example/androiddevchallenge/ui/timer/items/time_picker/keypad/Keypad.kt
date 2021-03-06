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

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.timer.items.time_picker.TimePickerListener

@Composable
internal fun Keypad(listener: TimePickerListener) {
    BoxWithConstraints {
        val keypadItemSizeModifier = Modifier.size(
            height = minOf(maxHeight / 4, 100.dp),
            width = 88.dp.coerceAtMost(maxWidth / 3)
        )

        Column {
            (1..9)
                .chunked(3)
                .forEach { chunk ->
                    Row {
                        chunk.forEach { number ->
                            KeypadNumber(
                                modifier = keypadItemSizeModifier,
                                number = number,
                                listener = listener::onNumberClicked
                            )
                        }
                    }
                }

            ConstraintLayout {
                val (zero, backspace) = createRefs()

                KeypadDelete(
                    modifier = keypadItemSizeModifier
                        .constrainAs(backspace) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(zero.end)
                        },
                    onDelete = listener::onKeypadDeleteClicked
                )

                KeypadNumber(
                    modifier = keypadItemSizeModifier
                        .constrainAs(zero) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    number = 0,
                    listener = listener::onNumberClicked
                )
            }
        }
    }
}

@Preview("Keypad", widthDp = 360, heightDp = 640)
@Composable
private fun KeypadPreview() {
    MyTheme {
        Keypad(
            listener = TimePickerListener.NONE
        )
    }
}
