package com.xeno761.noteitdown.presentation.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.xeno761.noteitdown.R
import com.xeno761.noteitdown.presentation.component.TextOptionButton

enum class ExportType {
    TXT,
    MARKDOWN,
    HTML
}

@Composable
fun ExportDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (ExportType) -> Unit
) = AlertDialog(
    title = {
        Text(text = stringResource(R.string.export_as))
    },
    text = {
        Column(modifier = Modifier.fillMaxWidth()) {
            TextOptionButton(text = "TXT") {
                onConfirm(ExportType.TXT)
            }

            TextOptionButton(text = "MARKDOWN") {
                onConfirm(ExportType.MARKDOWN)
            }

            TextOptionButton(text = "HTML") {
                onConfirm(ExportType.HTML)
            }
        }
    },
    onDismissRequest = onDismissRequest,
    confirmButton = {}
)

@Composable
@Preview
fun ExportDialogPreview() {
    ExportDialog(
        onDismissRequest = {},
        onConfirm = {}
    )
}