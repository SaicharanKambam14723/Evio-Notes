package com.example.evionotes.ui.notes.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageGalleryScreen(
    imageIds: List<Int>,
    onBackClick: () -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { imageIds.size }
    )

    BackHandler {
        onBackClick()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "${pagerState.currentPage + 1} / ${imageIds.size}",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                AsyncImage(
                    model = imageIds[page],
                    contentDescription = "Image $page",
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ImageGalleryScreen(
//    imageIds: List<Int>,
//    onBackClick: () -> Unit
//    ) {
//    val pagerState = rememberPagerState()
//
//    BackHandler {
//        onBackClick()
//    }
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = Color.Black
//    ) {
//        Column(modifier = Modifier.fillMaxSize()) {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "${pagerState.currentPage + 1} / ${imageIds.size}",
//                        color = Color.White
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = onBackClick) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = "Back",
//                            tint = Color.White
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
//            )
//
//            HorizontalPager(
//                pageCount = imageIds.size,
//                state = pagerState,
//                modifier = Modifier
//                    .fillMaxSize()
//            ) { page ->
//                AsyncImage(
//                    model = imageIds[page], // This could be a URI or resource ID; adjust accordingly
//                    contentDescription = "Image $page",
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.Black)
//                        .align(Alignment.CenterHorizontally)
//                )
//            }
//        }
//    }
//
//}