package com.example.evionotes.ui.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.evionotes.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    // Animatables
    val logoScale = remember { Animatable(2.0f) } // Start zoomed in
    val logoAlpha = remember { Animatable(0f) }
    val logoRotation = remember { Animatable(0f) }
    val glowAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Fade in
        logoAlpha.animateTo(targetValue = 1f, animationSpec = tween(800))

        // Zoom out with overshoot
        logoScale.animateTo(
            targetValue = 1.1f,
            animationSpec = tween(800, easing = { OvershootInterpolator(6f).getInterpolation(it) })
        )

        // Rotation animation
        logoRotation.animateTo(15f, tween(600))
        logoRotation.animateTo(0f, tween(600, easing = FastOutSlowInEasing))

        // Subtle pulse (finite)
        repeat(2) {
            logoScale.animateTo(
                targetValue = 1.05f,
                animationSpec = tween(400, easing = LinearEasing)
            )
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(400, easing = LinearEasing)
            )
        }

        // Last glow pulse at the end
        glowAlpha.animateTo(0.5f, tween(500))
        glowAlpha.animateTo(0f, tween(500))

        delay(1000) // wait before navigating
        onTimeout()  // navigate to next screen
    }

    // Main layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        // Glow behind logo
        Surface(
            modifier = Modifier
                .size(180.dp)
                .alpha(glowAlpha.value),
            shape = CircleShape,
            color = Color(0xFF5CB9EC).copy(alpha = 0.3f)
        ) {}

        // Logo with all combined animations
        Image(
            painter = painterResource(id = R.drawable.logo_nobg),
            contentDescription = "App Logo",
            modifier = Modifier
                .scale(logoScale.value)
                .rotate(logoRotation.value)
                .size(160.dp)
                .alpha(logoAlpha.value)
        )
    }
}
