@file:OptIn(ExperimentalTvMaterial3Api::class)

package com.yhyzgn.tv.rubbish.activity.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Glow
import androidx.tv.material3.NonInteractiveSurfaceDefaults
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import coil3.compose.AsyncImage
import com.yhyzgn.tv.rubbish.activity.model.Media
import com.yhyzgn.tv.rubbish.activity.source.SourceProvider
import com.yhyzgn.tv.rubbish.activity.source.SourceRegistry
import com.yhyzgn.tv.rubbish.activity.theme.AppColors
import com.yhyzgn.tv.rubbish.activity.view.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(viewModel: HomeViewModel, onMediaClick: (Media) -> Unit) {
    val currentSource by viewModel.currentSourceId.collectAsState()
    val categories by viewModel.categories.collectAsState()
//    val recommend by viewModel.recommend.collectAsState()
    val contents by viewModel.contents.collectAsState()
    val history by viewModel.history.collectAsState()

    val accent = AppColors.textAccent
    val bg = AppColors.background

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        colors = NonInteractiveSurfaceDefaults.colors(
            containerColor = bg
        )
    ) {
        TvLazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            item {
                SourceRow(
                    current = currentSource,
                    sources = SourceRegistry.all(),
                    onSelect = { viewModel.switchSource(it.id) },
                    accent = accent
                )
            }

            // 最近播放
            if (!history.isEmpty()) {
                item {
                    CategoryRow(
                        title = "最近播放",
                        medias = history,
                        accent = accent,
                        showMore = false,
                        onMediaClick
                    )
                }
            }

            // 分类
            categories.forEach { cat ->
                val items = contents[cat.id] ?: emptyList()
                item {
                    CategoryRow(
                        title = cat.name,
                        medias = items,
                        accent = accent,
                        showMore = true,
                        onMediaClick
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SourceRow(
    current: String,
    sources: List<SourceProvider>,
    onSelect: (SourceProvider) -> Unit,
    accent: Color
) {
    TvLazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp), // add larger top spacing to avoid top overflow when focused
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(start = 24.dp, top = 12.dp, end = 24.dp, bottom = 12.dp) // match other rows and add top padding
    ) {
        items(sources.size) { index ->
            val source = sources[index]
            val selected = current == source.id

            Button(
                modifier = Modifier
                    .width(80.dp)
                    .height(40.dp),
                onClick = { onSelect(source) },
                colors = ButtonDefaults.colors(
                    containerColor = Color.Transparent,
                    contentColor = if (selected) accent else AppColors.textPrimary,
                    focusedContentColor = AppColors.textAccent,
                    focusedContainerColor = AppColors.cardFocused,
                    pressedContentColor = AppColors.pressedText,
                    pressedContainerColor = AppColors.pressedBackground
                ),
                shape = ButtonDefaults.shape(
                    shape = RoundedCornerShape(32)
                ),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp,
                    bottom = 8.dp
                )
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterVertically),
                    text = source.displayName,
                    textAlign = TextAlign.Center,
                    fontSize = if (selected) 17.sp else 15.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun BannerSection(banners: List<Media>, onItemClick: (Media) -> Unit) {
    var currentIndex by remember { mutableIntStateOf(0) }

    // 自动轮播
    LaunchedEffect(banners) {
        while (true) {
            delay(4000)
            currentIndex = (currentIndex + 1) % banners.size
        }
    }

    val banner = if (banners.isEmpty()) null else banners[currentIndex]
    Box(
        Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(horizontal = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { if (null != banner) onItemClick(banner) })
            }
    ) {
        AsyncImage(
            model = banner?.posterUrl,
            contentDescription = banner?.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .shadow(8.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                    )
                )
        )
        Text(
            text = banner?.title ?: "x",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }
}

@Composable
fun CategoryRow(title: String, medias: List<Media>, accent: Color, showMore: Boolean, onItemClick: (Media) -> Unit) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            if (showMore) {
                Button(
                    onClick = { },
                    modifier = Modifier.height(48.dp),
                    colors = ButtonDefaults.colors(
                        containerColor = Color.Transparent,
                        contentColor = accent
                    )
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("更多 ")
                            withStyle(SpanStyle(color = accent)) { append(">>") }
                        },
                        color = Color.LightGray,
                        fontSize = 16.sp,
                        modifier = Modifier.focusable()
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        TvLazyRow(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) {
            items(medias.take(10).size) { index ->
                val media = medias[index]
                MediaCard(media) { onItemClick(media) }
            }
        }
    }
}

@Composable
fun MediaCard(media: Media, onClick: () -> Unit) {
    var focused by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(150.dp)
            .height(230.dp)
            .onFocusChanged { focused = it.isFocused },
        scale = CardDefaults.scale(focusedScale = 1.1f),
        glow = CardDefaults.glow(
            focusedGlow = Glow(
                elevationColor = Color(0xFFFF3D00), // 发光颜色
                elevation = 30.dp,                  // 光晕强度
            )
        ),
        colors = CardDefaults.colors(
            containerColor = if (focused) Color(0xFF202020) else Color(0xFF101010),
            contentColor = Color.White
        ),
        shape = CardDefaults.shape(
            RoundedCornerShape(20.dp)
        ),
        border = CardDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(1.dp, Color.Transparent)
            )
        ),
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = media.posterUrl,
                contentDescription = media.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .shadow(if (focused) 12.dp else 4.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = media.title,
                color = if (focused) AppColors.textPrimary else AppColors.textSecondary,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}