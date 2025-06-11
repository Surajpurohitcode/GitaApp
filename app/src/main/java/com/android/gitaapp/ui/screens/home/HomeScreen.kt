package com.android.gitaapp.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.gitaapp.R
import com.android.gitaapp.data.model.GitaChapter
import com.android.gitaapp.data.model.GitaVerse
import com.android.gitaapp.navigation.Dist
import com.android.gitaapp.ui.theme.NotoSeriffontFamily
import com.android.gitaapp.ui.theme.fontFamily
import com.android.gitaapp.utils.AppUiState
import com.valentinilk.shimmer.shimmer
import kotlin.random.Random

@Composable
fun HomeScreen(
    viewModel: GitaViewModel, navController: NavController
) {

    val chaptersState = viewModel.chapters.value
    val versesState = viewModel.verses.value

    val dailySolkaChapterNumber = remember { mutableStateOf(Random.nextInt(1, 18)) }

    LaunchedEffect(Unit) {
        viewModel.fetchChapters()
        viewModel.fetchVerses(dailySolkaChapterNumber.value, 1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFEF2D8))
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Namsate", color = Color.Black, style = TextStyle(
                        fontFamily = fontFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "May the wisdom of the\nGita guide your day.",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = NotoSeriffontFamily
                    )
                )
            }
            Image(
                modifier = Modifier.align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.om_symbol),
                contentDescription = ""
            )
        }
        DailyShlokaCard(versesState, {
            navController.navigate(Dist.ReadSloka(dailySolkaChapterNumber.value))
        })
        ExploreChapterSection(chaptersState, navController)
        ListenGitaCard()
        MeditationSection(navController)

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .clickable {
                    navController.navigate(Dist.ChatScreen)
                },
            painter = painterResource(R.drawable.ask_krishna_card_bg),
            contentDescription = ""
        )

        HomeFooter()

    }
}

@Composable
fun DailyShlokaCard(versesState: AppUiState<List<GitaVerse>>, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF8E5)
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE6B250)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.om_symbol),
                        contentDescription = "",
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(start = 6.dp),
                    text = "Today’s Shloka",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = NotoSeriffontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            when (versesState) {
                is AppUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }

                is AppUiState.Error -> {
                    Text(
                        "Error: ${versesState.message} ?: No Sloka Found!",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                is AppUiState.Success -> {
                    val verse = versesState.data.firstOrNull()
                    if (verse != null) {
                        Text(
                            modifier = Modifier
                                .width(250.dp)
                                .padding(top = 16.dp)
                                .align(Alignment.CenterHorizontally),
                            text = "${verse.slok}",
                            color = Color.Black,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                fontFamily = NotoSeriffontFamily,
                            )
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = {
                    onClick()
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Read More", color = Color.Black, style = TextStyle(
                        fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                    )
                )
            }

        }
    }
}

@Composable
fun ExploreChapterSection(chaptersState: Result<List<GitaChapter>>, navController: NavController) {

    val chapters = chaptersState.getOrNull() ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Explore Gita Chapter", color = Color.Black, style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = NotoSeriffontFamily
                )
            )
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {},
            ) {
                Image(
                    painter = painterResource(R.drawable.arrow_forward_ic), contentDescription = ""
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = rememberLazyListState(),
            reverseLayout = false,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            flingBehavior = ScrollableDefaults.flingBehavior(),
            userScrollEnabled = true
        ) {
            // Add 5 items
            if (chaptersState.isSuccess) {
                items(chapters.size) {
                    ChapterCard(
                        chapterNumber = chapters[it].chapterNumber,
                        chapterName = chapters[it].name,
                        {
                            navController.navigate(Dist.ReadSloka(chapters[it].chapterNumber))
                        })
                }
            } else {
                items(5) {
                    ShimmeringPlaceholder()
                }
            }
        }

    }

}

@Composable
fun ChapterCard(chapterNumber: Int = 1, chapterName: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(101.dp)
            .clickable {
                onClick()
            }, colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF8E5)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                modifier = Modifier
                    .width(65.dp)
                    .height(65.dp)
                    .clip(RoundedCornerShape(16.dp)),
                painter = painterResource(R.drawable.placeholder_img_chapter_card),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp) // Space between items
            ) {
                Text(
                    text = "Chapter $chapterNumber", style = TextStyle(
                        color = Color.Black,
                        fontFamily = NotoSeriffontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Text(
                    text = chapterName, color = Color.Black, style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = NotoSeriffontFamily
                    )
                )
                LinearProgressIndicator(
                    progress = { 0.5f },
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFDFAD47),
                    trackColor = Color(0xFFFFEDC5) // Optional: background track color
                )
            }

        }
    }
}

@Composable
fun ShimmeringPlaceholder() {
    Row(
        modifier = Modifier
            .shimmer() // <- Affects all subsequent UI elements
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(80.dp, 80.dp)
                .background(Color.LightGray)
                .clip(RoundedCornerShape(16.dp)),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(Color.LightGray),
            )
            Box(
                modifier = Modifier
                    .size(120.dp, 20.dp)
                    .background(Color.LightGray),
            )
        }
    }
}

@Composable
fun ListenGitaCard() {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .height(100.dp)
            .clickable {
                Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
            }, colors = CardDefaults.cardColors(
            containerColor = Color(0xFF113C57)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.headphones), contentDescription = ""
            )
            Column(
                modifier = Modifier.padding(start = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Listen to Bhagavad Gita", color = Color.White, style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = NotoSeriffontFamily
                    )
                )
                Text(
                    text = "Recitation in Sanskrit / Hindi / English",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = NotoSeriffontFamily
                    )
                )
            }
        }
    }
}

@Composable
fun MeditationSection(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Meditation Music", color = Color.Black, style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = NotoSeriffontFamily
                )
            )
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {},
            ) {
                Image(
                    painter = painterResource(R.drawable.arrow_forward_ic), contentDescription = ""
                )
            }
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = rememberLazyListState(),
            reverseLayout = false,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            flingBehavior = ScrollableDefaults.flingBehavior(),
            userScrollEnabled = true
        ) {
            items(10) { index ->
                MeditationMusicCard {
                    navController.navigate(Dist.MusicPlayer)
                }
            }
        }
    }
}

@Composable
fun MeditationMusicCard(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .width(156.dp)
            .height(156.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                onClick()
            }, contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.placeholder_img_chapter_card),
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )

        Image(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(R.drawable.play_ic),
            contentDescription = ""
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xAA000000) // semi-black or use your theme color
                        )
                    )
                )
                .padding(start = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(30.dp)),
                painter = painterResource(R.drawable.demo_avatar),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Text(
                    text = "Enchanting Flute", color = Color.White, style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = NotoSeriffontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Text(
                    text = " Rakesh Chaurasia", color = Color.White, style = TextStyle(
                        fontSize = 8.sp,
                        fontFamily = NotoSeriffontFamily,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

        }

    }
}

@Composable
fun HomeFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .width(100.dp)
                .height(100.dp),
            painter = painterResource(R.drawable.gita_app_ic),
            contentDescription = ""
        )
        Text(
            text = "App Version\n" + "V1.0.0", color = Color.Black, style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = NotoSeriffontFamily,
                textAlign = TextAlign.Center
            )
        )
    }
}

//@Preview
//@Composable
//private fun HomeScreenPreview(modifier: Modifier = Modifier) {
//    HomeScreen(viewModel = Unit, navController = NavController)
//}