package com.example.evionotes.ui.notes.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.elements.MarkdownText
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun NoteCard(
    note: NoteUiModel,
    onClick: () -> Unit,
    onViewImages: (List<Int>) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(14.dp)
        ) {
            if(note.imageUrls.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    note.imageUrls.take(3).forEach { imgRes ->
                        Image(
                            painter = painterResource(id = imgRes),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                    if(note.imageUrls.size > 3) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .clickable {
                                    onViewImages(note.imageUrls)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+${note.imageUrls.size - 3}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            note.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
            when(note.type) {
                NoteType.Checklist -> {
                    note.checklistItems?.forEach {
                        (label, isChecked) -> run {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = null
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
                NoteType.Markdown -> MarkdownText(note.content.orEmpty())
                NoteType.Quote -> QuoteText(note.content.orEmpty())
                NoteType.Highlight -> HighlightedText(note.content.orEmpty())
                NoteType.Strikethrough -> StrikethroughText(note.content.orEmpty())
                NoteType.Code -> CodeText(note.content.orEmpty())
                NoteType.Image -> {
                    note.content?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                NoteType.Mixed, NoteType.Regular -> {
                    note.content?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            note.timestamp?.let {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = formatTimeStamp(it),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun MarkdownText(text: String) {
    BasicMarkdownText(text)
}

@Composable
fun QuoteText(text: String) {
    Text(
        text = "❝ $text ❞",
        style = MaterialTheme.typography.bodyMedium,
        fontStyle = FontStyle.Italic
    )
}

@Composable
fun HighlightedText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.run {
            background(Color(0xFFEEF7C5))
                .padding(4.dp)
        }
    )
}

@Composable
fun StrikethroughText(text: String) {
    Text(
        text = text,
        textDecoration = TextDecoration.LineThrough,
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray
    )
}

@Composable
fun CodeText(text: String) {
    Text(
        text = text,
        fontFamily = FontFamily.Monospace,
        style = MaterialTheme.typography.bodyMedium,
        color = Color(0xFF62D3F9),
        modifier = Modifier
            .background(Color(0xFF282C34))
            .padding(6.dp)
    )
}

fun formatTimeStamp(ts: Long): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")
        .withZone(ZoneId.systemDefault()) // or ZoneId.of("UTC") if needed
    return formatter.format(Instant.ofEpochMilli(ts))
}