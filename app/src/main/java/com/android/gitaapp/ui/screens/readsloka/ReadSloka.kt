package com.android.gitaapp.ui.screens.readsloka

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.gitaapp.R
import com.android.gitaapp.data.model.GitaVerse
import com.android.gitaapp.navigation.Dist
import com.android.gitaapp.ui.screens.home.GitaViewModel
import com.android.gitaapp.ui.theme.NotoSeriffontFamily
import com.android.gitaapp.ui.theme.fontFamily
import com.android.gitaapp.utils.AppUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadSloka(viewModel: GitaViewModel, ttsViewModel: TTSViewModel, navController: NavController, readSloka: Dist.ReadSloka) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val versesState = viewModel.verses.value

    val verseCount = remember { mutableStateOf(1) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchVerses(readSloka.chapterNumber, verseCount.value)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = Color(0xFFFEF2D8),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFEF2D8),
                    titleContentColor = Color.Black,
                    scrolledContainerColor = Color(0xFFFEF2D8)
                ),
                title = {
                    Text(
                        "Chapter ${readSloka.chapterNumber} Sloka ${verseCount.value}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontFamily = NotoSeriffontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    OutlinedButton(onClick = {
                        when (versesState) {
                            is AppUiState.Loading -> {
                                Toast.makeText(context, "Please wait", Toast.LENGTH_SHORT).show()
                            }

                            is AppUiState.Error -> {
                                Toast.makeText(context, "Error${versesState.message}", Toast.LENGTH_SHORT).show()
                            }

                            is AppUiState.Success -> {
                                ttsViewModel.speak(versesState.data.first().chinmay.hc)
                            }
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.voice),
                            contentDescription = "",
                            tint = Color(0xFFE6B250)
                        )
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = "listen Sloka",
                            color = Color.Black,
                            style = TextStyle(
                                fontFamily = NotoSeriffontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ScrollContent(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(), versesState
            )
            SlokaNavigationButtons(onForwardClick = {
                if (verseCount.value < 100) {
                    verseCount.value++
                    viewModel.fetchVerses(chapterNumber = 1, verseCount.value)
                }
            }, onBackwardClick = {
                if (verseCount.value > 1) {
                    verseCount.value--
                    viewModel.fetchVerses(chapterNumber = 1, verseCount.value)
                }
            })
        }
    }

}

@Composable
fun ScrollContent(modifier: Modifier = Modifier, versesState: AppUiState<List<GitaVerse>>) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = rememberLazyListState(),
        reverseLayout = false,
        flingBehavior = ScrollableDefaults.flingBehavior(),
        userScrollEnabled = true
    ) {
        when (versesState) {
            is AppUiState.Loading -> {
                item {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
            }

            is AppUiState.Error -> {
                item {
                    Text(
                        "Error: ${versesState.message}",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            is AppUiState.Success -> {
                val verse = versesState.data.firstOrNull()
                if (verse != null) {
                    item {
                        Text(
                            text = verse.slok, color = Color.Black, style = TextStyle(
                                fontFamily = fontFamily,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        )
                        Image(
                            modifier = Modifier.padding(top = 16.dp),
                            painter = painterResource(R.drawable.divder_ic),
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = "अनुवाद: ${verse.tej.ht}",
                            color = Color.Black,
                            style = TextStyle(
                                fontFamily = fontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(top = 16.dp),
                            color = Color.Black,
                            thickness = 0.5.dp
                        )

                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = verse.chinmay.hc ?: "No translation available",
                            color = Color.Black,
                            style = TextStyle(
                                fontFamily = fontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SlokaNavigationButtons(onForwardClick: () -> Unit, onBackwardClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .height(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFE6B250))
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.Center), onClick = onBackwardClick
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description",
                    tint = Color.White
                )
            }
        }

        Box(
            modifier = Modifier
                .height(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFE6B250))
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.Center), onClick = onForwardClick
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Localized description",
                    tint = Color.White
                )
            }
        }
    }
}


//@Preview
//@Composable
//fun PreviewScreen(modifier: Modifier = Modifier) {
//    ReadSloka(modifier = modifier, viewModel = GitaViewModel)
//}
