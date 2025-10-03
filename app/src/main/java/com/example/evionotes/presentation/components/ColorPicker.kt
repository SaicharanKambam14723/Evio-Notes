package com.example.evionotes.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.evionotes.presentation.theme.NoteBlue
import com.example.evionotes.presentation.theme.NoteGray
import com.example.evionotes.presentation.theme.NoteGreen
import com.example.evionotes.presentation.theme.NoteOrange
import com.example.evionotes.presentation.theme.NotePink
import com.example.evionotes.presentation.theme.NotePurple
import com.example.evionotes.presentation.theme.NoteRed
import com.example.evionotes.presentation.theme.NoteTeal
import com.example.evionotes.presentation.theme.NoteYellow
import com.example.evionotes.presentation.theme.PrimaryBlue
import com.example.evionotes.presentation.theme.SecondaryBlue

@Composable
fun ColorPicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        Color.White,
        NoteYellow,
        NoteOrange,
        NotePink,
        NotePurple,
        NoteBlue,
        NoteGreen,
        NoteTeal,
        NoteRed,
        NoteGray,
        PrimaryBlue,
        SecondaryBlue
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(colors) { specificColor ->
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(specificColor)
                    .border(
                        width = if(selectedColor == specificColor) 3.dp else 1.dp,
                        color = if(selectedColor == specificColor) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outline
                        },
                        shape = CircleShape
                    )
                    .clickable {
                        onColorSelected(specificColor)
                    }
            )
        }
    }
}