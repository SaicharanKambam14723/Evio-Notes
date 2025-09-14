package com.example.evionotes.ui.notes.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private fun parseBasicMarkdown(mdText: String) = buildAnnotatedString {
    val lines = mdText.lines()
    lines.forEach { line ->
        when {
            line.startsWith("- ") || line.startsWith("* ") -> {
                append("• ")
                append(line.drop(2))
                append("\n")
            }
            line.startsWith("**") && line.endsWith("**") -> {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append(line.removeSurrounding("**"))
                pop()
                append("\n")
            }
            line.startsWith("*") && line.endsWith("*") -> {
                pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                append(line.removeSurrounding("*"))
                pop()
                append("\n")
            }
            line.startsWith("`") && line.endsWith("`") -> {
                pushStyle(
                    SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        background = Color(0xFFE0E0E0),
                        color = Color.Black
                    )
                )
                append(line.removeSurrounding("`"))
                pop()
                append("\n")
            }
            else -> {
                append(line)
                append("\n")
            }
        }
    }
}

@Composable
fun BasicMarkdownText(text: String, modifier: Modifier = Modifier) {
    val annotatedString = parseBasicMarkdown(text)
    Column(
        modifier = modifier
            .padding(8.dp)
    ) {
        Text(
            text = annotatedString,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}