package com.example.evionotes.ui.splash

import android.annotation.SuppressLint
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.evionotes.R

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val scale = remember { Animatable(0.7f) }
    val alpha = remember { Animatable(0f) }
    val textOffset = remember { Animatable(40f) }
    val bgColor by animateColorAsState(
        targetValue = if(alpha.value > 0.5f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
        animationSpec = tween(durationMillis = 800)
    )

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 700, easing = {
                OvershootInterpolator(3f).getInterpolation(it)
            })
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 700)
        )
        textOffset.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 700, delayMillis = 250)
        )

        kotlinx.coroutines.delay(1200)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .background(bgColor)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .scale(scale.value)
                    .size(140.dp)
                    .alpha(alpha.value)
            )
            Spacer(modifier = Modifier.size(24.dp))
            Text(
                text = R.string.app_name.toString(),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .alpha(alpha.value)
                    .offset(y = textOffset.value.dp)
            )
        }
    }
}