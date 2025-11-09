@file:OptIn(ExperimentalTvMaterial3Api::class)

package com.yhyzgn.tv.rubbish.activity.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Glow
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.Text
import coil3.compose.AsyncImage
import com.yhyzgn.tv.rubbish.activity.model.Media
import com.yhyzgn.tv.rubbish.activity.source.SourceProvider
import com.yhyzgn.tv.rubbish.activity.source.SourceRegistry
import com.yhyzgn.tv.rubbish.activity.theme.AppColors
import com.yhyzgn.tv.rubbish.activity.theme.FocusDefaults
import com.yhyzgn.tv.rubbish.activity.view.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMediaClick: (Media) -> Unit,
    onBackToTop: (() -> Unit)? = null
) {
    val currentSource by viewModel.currentSourceId.collectAsState()
    val categories by viewModel.categories.collectAsState()
//    val recommend by viewModel.recommend.collectAsState()
    val contents by viewModel.contents.collectAsState()
    val history by viewModel.history.collectAsState()

    val accent = AppColors.textAccent
    val bg = AppColors.background

//    val topFocusRequester = remember { FocusRequester() }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyUp && event.key == Key.Back) {
                    // 返回键逻辑：焦点回到顶部源按钮
//                    topFocusRequester.requestFocus()
                    onBackToTop?.invoke()
                    true
                } else false
            },
        colors = SurfaceDefaults.colors(
            containerColor = bg
        )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            item {
                SourceRow(
                    current = currentSource,
                    sources = SourceRegistry.all(),
                    onSelect = { viewModel.switchSource(it.id) },
                    accent = accent,
//                    topFocusRequester = topFocusRequester
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
                        onItemClick = onMediaClick,
                        onMoreClick = {},
//                        focusUp = topFocusRequester
                    )
                }
            }

            // 分类
            categories.forEachIndexed { index, cat ->
                val items = contents[cat.id] ?: emptyList()
                item {
                    CategoryRow(
                        title = cat.name,
                        medias = items,
                        accent = accent,
                        showMore = true,
                        onItemClick = onMediaClick,
                        onMoreClick = {},
//                        focusUp = if (index == 0) topFocusRequester else null
                    )
                }
            }
        }
    }
}

@Composable
fun SourceRow(
    current: String,
    sources: List<SourceProvider>,
    onSelect: (SourceProvider) -> Unit,
    accent: Color,
//    topFocusRequester: FocusRequester
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .focusGroup()
//            .focusRequester(topFocusRequester)
            .padding(top = 8.dp), // add larger top spacing to avoid top overflow when focused
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp) // match other rows and add top padding
    ) {
        items(sources.size) { index ->
            val source = sources[index];
            val selected = current == source.id

            Button(
                modifier = Modifier
                    .width(72.dp)
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
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
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

//@Composable
//fun BannerSection(banners: List<Media>, onItemClick: (Media) -> Unit) {
//    var currentIndex by remember { mutableIntStateOf(0) }
//
//    // 自动轮播
//    LaunchedEffect(banners) {
//        while (true) {
//            delay(4000)
//            currentIndex = (currentIndex + 1) % banners.size
//        }
//    }
//
//    val banner = if (banners.isEmpty()) null else banners[currentIndex]
//    Box(
//        Modifier
//            .fillMaxWidth()
//            .height(220.dp)
//            .padding(horizontal = 16.dp)
//            .pointerInput(Unit) {
//                detectTapGestures(onTap = { if (null != banner) onItemClick(banner) })
//            }
//    ) {
//        AsyncImage(
//            model = banner?.posterUrl,
//            contentDescription = banner?.title,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .fillMaxSize()
//                .shadow(8.dp, RoundedCornerShape(16.dp))
//                .clip(RoundedCornerShape(16.dp))
//        )
//        Box(
//            Modifier
//                .fillMaxSize()
//                .background(
//                    Brush.verticalGradient(
//                        listOf(Color.Transparent, AppColors.overlayDark)
//                    )
//                )
//        )
//        Text(
//            text = banner?.title ?: "x",
//            color = AppColors.textPrimary,
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .align(Alignment.BottomStart)
//                .padding(16.dp)
//        )
//    }
//}

@Composable
fun CategoryRow(
    title: String,
    medias: List<Media>,
    accent: Color,
    showMore: Boolean,
    onItemClick: (Media) -> Unit,
    onMoreClick: () -> Unit,
    focusUp: FocusRequester? = null
) {
//    val focusRequesterMore = remember { FocusRequester() }
//    val focusRequesterRow = remember { FocusRequester() }
    val rowState = rememberLazyListState()

    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                color = AppColors.textPrimary,
                style = MaterialTheme.typography.titleLarge
            )
            if (showMore) {
                Button(
                    onClick = onMoreClick,
                    modifier = Modifier
                        .defaultMinSize(),
//                        .focusRequester(focusRequesterMore)
//                        .focusable()
//                        .focusProperties {
//                            up = focusUp ?: FocusRequester.Default
//                            left = focusRequesterRow
//                            down = FocusRequester.Default
//                            right = FocusRequester.Default
//                        },
                    colors = ButtonDefaults.colors(
                        containerColor = Color.Transparent,
                        contentColor = accent,
                        focusedContentColor = AppColors.textAccent,
                        focusedContainerColor = AppColors.cardFocused,
                        pressedContentColor = AppColors.pressedText,
                        pressedContainerColor = AppColors.pressedBackground
                    ),
                    shape = ButtonDefaults.shape(
                        shape = RoundedCornerShape(32)
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("更多 ")
                            withStyle(SpanStyle(color = accent)) { append(" ->") }
                        },
                        color = AppColors.textSecondary,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
//                .focusRequester(focusRequesterRow)
//                .focusGroup(),
            state = rowState,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) {
            val takenMedias = medias.take(10)
            items(takenMedias.size) { index ->
                val media = takenMedias[index]
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
            .width(180.dp)
            .zIndex(if (focused) 1f else 0f)
            .onFocusChanged { focused = it.isFocused },
        scale = CardDefaults.scale(focusedScale = FocusDefaults.FOCUSED_SCALE),
        glow = CardDefaults.glow(
            focusedGlow = Glow(
                elevationColor = AppColors.focusGlow, // 发光颜色
                elevation = FocusDefaults.focusedGlowElevation,                  // 光晕强度
            )
        ),
        colors = CardDefaults.colors(
            containerColor = if (focused) AppColors.cardFocused else AppColors.cardNormal,
            contentColor = AppColors.textPrimary
        ),
        shape = CardDefaults.shape(
            RoundedCornerShape(16.dp)
        ),
        border = CardDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(1.dp, Color.Transparent)
            )
        ),
        onClick = onClick
    ) {
        // animate image scale so poster visually scales when card is focused (match card scale)
        val scale by animateFloatAsState(
            targetValue = if (focused) FocusDefaults.FOCUSED_SCALE else 1f,
            animationSpec = tween(durationMillis = 180)
        )

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = media.posterUrl,
                contentDescription = media.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .graphicsLayer { scaleX = scale; scaleY = scale }
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .graphicsLayer { scaleX = scale; scaleY = scale })
            Text(
                text = media.title,
                modifier = Modifier.graphicsLayer { scaleX = scale; scaleY = scale },
                color = if (focused) AppColors.textPrimary else AppColors.textSecondary,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}