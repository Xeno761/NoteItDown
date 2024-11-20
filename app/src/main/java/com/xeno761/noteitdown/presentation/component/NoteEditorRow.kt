package com.xeno761.noteitdown.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Redo
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.outlined.AddChart
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.DataArray
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.FormatBold
import androidx.compose.material.icons.outlined.FormatItalic
import androidx.compose.material.icons.outlined.FormatPaint
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.FormatStrikethrough
import androidx.compose.material.icons.outlined.FormatUnderlined
import androidx.compose.material.icons.outlined.HorizontalRule
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.TableChart
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xeno761.noteitdown.R
import com.xeno761.noteitdown.presentation.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconButtonWithTooltip(
    enabled: Boolean = true,
    imageVector: ImageVector? = null,
    painter: Int? = null,
    contentDescription: String,
    shortCutDescription: String? = null,
    onClick: () -> Unit
) = TooltipBox(
    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
    tooltip = {
        if (shortCutDescription != null) {
            PlainTooltip(
                content = { Text(shortCutDescription) }
            )
        }
    },
    state = rememberTooltipState(),
    focusable = false,
    enableUserInput = true
) {
    IconButton(onClick = onClick, enabled = enabled) {
        if (imageVector != null) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription
            )
        } else {
            Icon(
                painter = painterResource(id = painter!!),
                contentDescription = contentDescription
            )
        }
    }
}

@Composable
fun NoteEditorRow(
    isMarkdown: Boolean,
    canUndo: Boolean,
    canRedo: Boolean,
    onEdit: (String) -> Unit,
    onTableButtonClick: () -> Unit,
    onScanButtonClick: () -> Unit,
    onTaskButtonClick: () -> Unit,
    onLinkButtonClick: () -> Unit
) {

    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {

        HorizontalDivider(
            Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant,
            thickness = 2.dp
        )

        Row(
            Modifier
                .fillMaxWidth()
                .height(40.dp)
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButtonWithTooltip(
                enabled = canUndo,
                imageVector = Icons.AutoMirrored.Outlined.Undo,
                contentDescription = stringResource(id = R.string.undo),
                shortCutDescription = "Undo"
            ) {
                onEdit(Constants.Editor.UNDO)
            }

            IconButtonWithTooltip(
                enabled = canRedo,
                imageVector = Icons.AutoMirrored.Outlined.Redo,
                contentDescription = stringResource(id = R.string.redo),
                shortCutDescription = "Redo"
            ) {
                onEdit(Constants.Editor.REDO)
            }

            if (isMarkdown) {

                IconButtonWithTooltip(
                    imageVector = Icons.Outlined.Title,
                    contentDescription = "Heading Level"
                ) {
                    isExpanded = !isExpanded
                }

                AnimatedVisibility(visible = isExpanded) {
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {

                        IconButtonWithTooltip(
                            painter = R.drawable.format_h1,
                            contentDescription = "H1",
                            shortCutDescription = "Size 1"
                        ) {
                            onEdit(Constants.Editor.H1)
                        }

                        IconButtonWithTooltip(
                            painter = R.drawable.format_h2,
                            contentDescription = "H2",
                            shortCutDescription = "Size 2"
                        ) {
                            onEdit(Constants.Editor.H2)
                        }

                        IconButtonWithTooltip(
                            painter = R.drawable.format_h3,
                            contentDescription = "H3",
                            shortCutDescription = "Size 3"
                        ) {
                            onEdit(Constants.Editor.H3)
                        }

                        IconButtonWithTooltip(
                            painter = R.drawable.format_h4,
                            contentDescription = "H4",
                            shortCutDescription = "Size 4"
                        ) {
                            onEdit(Constants.Editor.H4)
                        }

                        IconButtonWithTooltip(
                            painter = R.drawable.format_h5,
                            contentDescription = "H5",
                            shortCutDescription = "Size 5"
                        ) {
                            onEdit(Constants.Editor.H5)
                        }

                        IconButtonWithTooltip(
                            painter = R.drawable.format_h6,
                            contentDescription = "H6",
                            shortCutDescription = "Size 6"
                        ) {
                            onEdit(Constants.Editor.H6)
                        }
                    }
                }

                IconButtonWithTooltip(
                    imageVector = Icons.Outlined.FormatBold,
                    contentDescription = stringResource(id = R.string.bold),
                    shortCutDescription = "Bold"
                ) {
                    onEdit(Constants.Editor.BOLD)
                }

                IconButtonWithTooltip(
                    imageVector = Icons.Outlined.FormatItalic,
                    contentDescription = stringResource(id = R.string.italic),
                    shortCutDescription = "Italic"
                ) {
                    onEdit(Constants.Editor.ITALIC)
                }

                IconButtonWithTooltip(
                    imageVector = Icons.Outlined.FormatUnderlined,
                    contentDescription = stringResource(id = R.string.underline),
                    shortCutDescription = "Underline"
                ) {
                    onEdit(Constants.Editor.UNDERLINE)
                }

                IconButtonWithTooltip(
                    imageVector = Icons.Outlined.FormatStrikethrough,
                    contentDescription = stringResource(id = R.string.strikethrough),
                    shortCutDescription = "Strikethrough"
                ) {
                    onEdit(Constants.Editor.STRIKETHROUGH)
                }

                IconButtonWithTooltip(
                    imageVector = Icons.Outlined.FormatPaint,
                    contentDescription = stringResource(id = R.string.mark),
                    shortCutDescription = "Mark"
                ) {
                    onEdit(Constants.Editor.MARK)
                }

                IconButtonWithTooltip(
                    imageVector = Icons.Outlined.Code,
                    contentDescription = stringResource(id = R.string.code),
                    shortCutDescription = "Code"
                ) {
                    onEdit(Constants.Editor.INLINE_CODE)
                }

                IconButtonWithTooltip(
                    imageVector = Icons.Outlined.DataArray,
                    contentDescription = "Brackets"
                ) {
                    onEdit(Constants.Editor.INLINE_BRACKETS)
                }

                IconButtonWithTooltip(
                    imageVector = Icons.Outlined.DataObject,
                    contentDescription = "Braces"
                ) {
                    onEdit(Constants.Editor.INLINE_BRACES)
                }

                IconButtonWithTooltip(
                    painter = R.drawable.function,
                    contentDescription = stringResource(id = R.string.math),
                    shortCutDescription = "Math"
                ) {
                    onEdit(Constants.Editor.INLINE_MATH)
                }

                IconButtonWithTooltip(
                    imageVector = Icons.Outlined.FormatQuote,
                    contentDescription = stringResource(id = R.string.quote),
                    shortCutDescription = "Quote"
                ) {
                    onEdit(Constants.Editor.QUOTE)
                }

                IconButtonWithTooltip(
                    imageVector = Icons.Outlined.HorizontalRule,
                    contentDescription = stringResource(id = R.string.horizontal_rule),
                    shortCutDescription = "Horizontal Rule"
                ) {
                    onEdit(Constants.Editor.RULE)
                }

                IconButtonWithTooltip(
                    imageVector = Icons.Outlined.TableChart,
                    contentDescription = stringResource(id = R.string.table),
                    shortCutDescription = "Table",
                    onClick = onTableButtonClick
                )

                IconButtonWithTooltip(
                    imageVector = Icons.Outlined.AddChart,
                    contentDescription = stringResource(id = R.string.mermaid_diagram),
                    shortCutDescription = "Mermaid Diagram"
                ) {
                    onEdit(Constants.Editor.DIAGRAM)
                }
            }

            IconButtonWithTooltip(
                imageVector = Icons.Outlined.CheckBox,
                contentDescription = stringResource(id = R.string.task_list),
                shortCutDescription = "Tasks",
                onClick = onTaskButtonClick
            )

            IconButtonWithTooltip(
                imageVector = Icons.Outlined.Link,
                contentDescription = stringResource(id = R.string.link),
                shortCutDescription = "Link",
                onClick = onLinkButtonClick
            )

            IconButtonWithTooltip(
                imageVector = Icons.Outlined.DocumentScanner,
                contentDescription = stringResource(id = R.string.scan),
                shortCutDescription = "Scan",
                onClick = onScanButtonClick
            )
        }
    }
}
