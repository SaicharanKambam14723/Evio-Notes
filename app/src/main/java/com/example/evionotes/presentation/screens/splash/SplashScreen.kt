package com.example.evionotes.presentation.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.evionotes.R
import com.example.evionotes.presentation.viewmodel.AuthState
import com.example.evionotes.presentation.viewmodel.AuthViewModel

@Composable
fun SplashScreen(
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val logoScale = remember { Animatable(2.0f) }
    val logoAlpha = remember { Animatable(0f) }
    val logoRotation = remember { Animatable(0f) }
    val glowAlpha = remember { Animatable(0f) }

    val authState by authViewModel.authState.collectAsState()
    var navigated by remember { mutableStateOf(false) }

    // Animation sequence
    LaunchedEffect(Unit) {
        logoAlpha.animateTo(1f, tween(800))
        logoScale.animateTo(
            1.1f,
            tween(800) { OvershootInterpolator(6f).getInterpolation(it) }
        )
        logoRotation.animateTo(15f, tween(600))
        logoRotation.animateTo(0f, tween(600, easing = FastOutSlowInEasing))
        repeat(2) {
            logoScale.animateTo(1.05f, tween(400))
            logoScale.animateTo(1f, tween(400))
        }
        glowAlpha.animateTo(0.5f, tween(500))
        glowAlpha.animateTo(0f, tween(500))
    }

    // Navigate once after animation finishes & user state resolved
    LaunchedEffect(authState) {
        if (!navigated && authState !is AuthState.Loading) {
            if (authState is AuthState.LoggedIn) {
                navController.navigate("home") { popUpTo("splash") { inclusive = true } }
            } else {
                navController.navigate("login") { popUpTo("splash") { inclusive = true } }
            }
            navigated = true
        }
    }

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
