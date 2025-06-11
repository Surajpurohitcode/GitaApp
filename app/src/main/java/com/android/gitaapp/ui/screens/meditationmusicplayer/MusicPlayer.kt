package com.android.gitaapp.ui.screens.meditationmusicplayer

import android.media.Image
import android.net.Uri
import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import com.android.gitaapp.R
import com.android.gitaapp.ui.theme.NotoSeriffontFamily
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayer(navController: NavController) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    val controlButtonSize = 64.dp
    val iconSize = 32.dp
    val buttonColor = Color(0xFFDFAD47)

    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build()
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    val isPlaying = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(playWhenReady: Boolean) {
                isPlaying.value = playWhenReady
            }
        })
    }

    val currentPosition = remember {
        mutableLongStateOf(0)
    }

    val totalDuration = remember {
        mutableLongStateOf(0)
    }

    // Setup media and prepare player
    LaunchedEffect(Unit) {
        val uri = Uri.parse("android.resource://${context.packageName}/${R.raw.demo_music}")
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    // Update position and duration every second
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentPosition.longValue = player.currentPosition
            sliderPosition = player.currentPosition.toFloat()
            if (player.duration > 0) {
                totalDuration.longValue = player.duration
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFEF2D8))
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.navigateUp() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            Text(
                modifier = Modifier.padding(start = 6.dp),
                text = "Now Playing",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = NotoSeriffontFamily
                )
            )

        }
        // Content below the toolbar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 35.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Your screen content here
            Image(
                modifier = Modifier
                    .width(256.dp)
                    .height(256.dp)
                    .clip(RoundedCornerShape(18.dp)),
                painter = painterResource(R.drawable.music_preview_img),
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "Serenity Flow",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = NotoSeriffontFamily
                )
            )

            Text(
                modifier = Modifier.padding(top = 6.dp),
                text = "Anya Sharma",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = NotoSeriffontFamily
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatMillis(player.currentPosition),
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = NotoSeriffontFamily
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Slider(
                    modifier = Modifier.weight(1f),
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                        player.seekTo(it.toLong())
                    },
                    valueRange = 0f..(totalDuration.longValue.toFloat()),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Black,
                        activeTrackColor = Color(0xFFDFAD47),
                        inactiveTrackColor = Color(0xFFFFEDC5)
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = formatMillis(totalDuration.longValue),
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = NotoSeriffontFamily
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Previous Button
                Box(
                    modifier = Modifier
                        .size(controlButtonSize)
                        .clip(CircleShape)
                        .background(buttonColor)
                        .clickable { /* Handle previous */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous",
                        tint = Color.White,
                        modifier = Modifier.size(iconSize)
                    )
                }

                Spacer(modifier = Modifier.width(32.dp))

                // Play/Pause Button
                Box(
                    modifier = Modifier
                        .size(controlButtonSize)
                        .clip(CircleShape)
                        .background(buttonColor)
                        .clickable {
                            if (isPlaying.value) player.pause() else player.play()
                        }, contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow, // Change to Pause when playing
                        contentDescription = "Play/Pause",
                        tint = Color.White,
                        modifier = Modifier.size(iconSize)
                    )
                }

                Spacer(modifier = Modifier.width(32.dp))

                // Next Button
                Box(
                    modifier = Modifier
                        .size(controlButtonSize)
                        .clip(CircleShape)
                        .background(buttonColor)
                        .clickable { /* Handle next */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }

        }
    }

}

fun formatMillis(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}