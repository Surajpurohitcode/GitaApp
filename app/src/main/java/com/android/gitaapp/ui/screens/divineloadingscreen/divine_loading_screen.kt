package com.android.gitaapp.ui.screens.divineloadingscreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.gitaapp.R
import kotlinx.coroutines.delay

@Composable
fun DivineLoadingScreen(onFinished: () -> Unit) {

    LaunchedEffect(Unit) {
        delay(8000) // 8 seconds
        onFinished()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C2A6C))
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        RotatingImage()
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            text = "\"You have the right to perform your duty, but not to the fruits of your actions.\"(Bhagavad Gita – Chapter 2, Verse 47)",
            color = Color.White,
            style = TextStyle(
                fontSize = 24.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold
            )
        )
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter), // This centers the image inside the Box
            painter = painterResource(id = R.drawable.intro_screen_bottom_img),
            contentDescription = ""
        )
    }
}

@Composable
fun RotatingImage() {
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteRotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotationAnim"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .rotate(rotation)
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.into_screen_bg_img),
            contentDescription = ""
        )
    }
}

