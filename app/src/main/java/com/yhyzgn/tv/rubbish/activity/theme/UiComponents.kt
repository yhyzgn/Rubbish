//package com.yhyzgn.tv.verde.activity.theme
//
//import androidx.compose.animation.core.animateDpAsState
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.focusable
//import androidx.compose.foundation.gestures.detectTapGestures
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Button
//import androidx.compose.material.ButtonDefaults
//import androidx.compose.material.Card
//import androidx.compose.material.Icon
//import androidx.compose.material.Text
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.FocusRequester
//import androidx.compose.ui.focus.focusRequester
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import coil3.compose.AsyncImage
//import com.yhyzgn.tv.verde.activity.source.ContentItem
//import com.yhyzgn.tv.verde.activity.source.SourceRegistry
//import com.yhyzgn.tv.verde.activity.view.HomeViewModel
//
//@Composable
//fun HomeScreen(viewModel: HomeViewModel, onRequestPlay: (ContentItem) -> Unit) {
//    val currentSource by viewModel.currentSourceId.collectAsState()
//    val categories by viewModel.categories.collectAsState()
//    val recommend by viewModel.recommend.collectAsState()
//    val contents by viewModel.contents.collectAsState()
//    val history by viewModel.history.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(AppColors.background)
//            .padding(12.dp)
//    ) {
//
//        TopBar(
//            currentSource = currentSource,
//            onSwitchSource = { viewModel.switchSource(it) },
//            sources = SourceRegistry.all().map { it.id to it.displayName })
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        // HERO: show first recommended item
//        val hero = recommend.firstOrNull()
//        if (hero != null) {
//            HeroSection(item = hero, onPlay = {
//                onRequestPlay(it)
//            }, onFavorite = {
//                // TODO: add favorite
//            })
//            Spacer(modifier = Modifier.height(18.dp))
//        }
//
//        // Recent row
//        if (history.isNotEmpty()) {
//            SectionTitle("最近观看")
//            CategoryRow(
//                items = history,
//                onItemFocus = { /* optional */ },
//                onItemClick = { onRequestPlay(it) }
//            )
//            Spacer(modifier = Modifier.height(12.dp))
//        }
//
//        // Type rows
//        categories.forEach { cat ->
//            SectionTitle(cat.name)
//            val items = contents[cat.id] ?: emptyList()
//            CategoryRow(
//                items = items,
//                onItemFocus = { /* preview could update hero */ },
//                onItemClick = { onRequestPlay(it) }
//            )
//            Spacer(modifier = Modifier.height(12.dp))
//        }
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        Footer(currentSource)
//    }
//}
//
//@Composable
//fun TopBar(currentSource: String, sources: List<Pair<String, String>>, onSwitchSource: (String) -> Unit) {
//    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
//        Text("Verde", color = Color(0xFF1DB954), fontSize = 28.sp, fontWeight = FontWeight.Bold)
//        Spacer(modifier = Modifier.width(20.dp))
//        // Source switcher
//        Row {
//            sources.forEach { (id, display) ->
//                val isSelected = id == currentSource
//                Button(
//                    onClick = { onSwitchSource(id) },
//                    colors = ButtonDefaults.buttonColors(backgroundColor = if (isSelected) AppColors.accent else Color.DarkGray),
//                    modifier = Modifier.padding(end = 8.dp)
//                ) {
//                    Text(display, color = Color.White)
//                }
//            }
//        }
//        Spacer(modifier = Modifier.weight(1f))
//        // right icons (placeholders)
//        Text("收藏", color = Color.LightGray, modifier = Modifier.padding(end = 12.dp))
//        Text("历史", color = Color.LightGray)
//    }
//}
//
//@Composable
//fun HeroSection(item: ContentItem, onPlay: (ContentItem) -> Unit, onFavorite: (ContentItem) -> Unit) {
//    Card(
//        shape = RoundedCornerShape(12.dp), modifier = Modifier
//            .fillMaxWidth()
//            .height(220.dp),
//        backgroundColor = AppColors.cardNormal,
//        elevation = 8.dp
//    ) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            // P    oster placeholder
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.DarkGray)
//            )
//            AsyncImage(
//                model = "https://img-blog.csdnimg.cn/6343e1698dc34686b87dbf50f4eaf0f2.png",
//                contentDescription = null,
//            )
//            // left overlay with title and buttons
////            Column(
////                modifier = Modifier
////                    .align(Alignment.BottomStart)
////                    .padding(16.dp)
////            ) {
////                Text("继续观看", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
////                Spacer(modifier = Modifier.height(8.dp))
////                Row {
////                    Button(onClick = { onPlay(item) }) { Text("播放") }
////                    Spacer(Modifier.width(8.dp))
////                    OutlinedButton(onClick = { onFavorite(item) }) { Text("收藏") }
////                }
////            }
//        }
//    }
//}
//
//@Composable
//fun SectionTitle(title: String) {
//    val focusRequester = remember { FocusRequester() }
//    var isFocused by remember { mutableStateOf(false) }
//
//    val scale by animateFloatAsState(if (isFocused) 1.08f else 1f)
//    val elevation by animateDpAsState(if (isFocused) 16.dp else 6.dp)
//
//    Row(
//        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(
//            title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium,
//            modifier = Modifier.padding(vertical = 8.dp)
//        )
//        Spacer(modifier = Modifier.weight(1f))
//
//        Row(
//            modifier = Modifier
//                .padding(end = 12.dp)
//                .focusRequester(focusRequester)
//                .onFocusChanged { state ->
//                    if (state.isFocused) {
//                        isFocused = true
//                    } else isFocused = false
//                }
//                .focusable()
//                .graphicsLayer {
//                    scaleX = scale
//                    scaleY = scale
//                },
//        ) {
//            Text("更多", color = Color.LightGray, modifier = Modifier.padding(end = 2.dp))
//            Icon(
//                Icons.AutoMirrored.Filled.KeyboardArrowRight,
//                "contentDescription",
//                tint = Color.LightGray,
//            )
//        }
//    }
//}
//
//@Composable
//fun CategoryRow(items: List<ContentItem>, onItemFocus: (ContentItem) -> Unit, onItemClick: (ContentItem) -> Unit) {
//    val focusManager = LocalFocusManager.current
//    LazyRow(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(160.dp)
//    ) {
//        itemsIndexed(items) { index, item ->
//            ContentCard(item = item, onClick = { onItemClick(item) }, onFocus = { onItemFocus(item) })
//            Spacer(modifier = Modifier.width(12.dp))
//        }
//    }
//}
//
//@Composable
//fun ContentCard(item: ContentItem, onClick: () -> Unit, onFocus: () -> Unit) {
//    val focusRequester = remember { FocusRequester() }
//    var isFocused by remember { mutableStateOf(false) }
//
//    val scale by animateFloatAsState(if (isFocused) 1.08f else 1f)
//    val elevation by animateDpAsState(if (isFocused) 16.dp else 6.dp)
//
//    Card(
//        modifier = Modifier
//            .width(240.dp)
//            .height(140.dp)
//            .focusRequester(focusRequester)
//            .onFocusChanged { state ->
//                if (state.isFocused) {
//                    isFocused = true
//                    onFocus()
//                } else isFocused = false
//            }
//            .focusable()
//            .graphicsLayer {
//                scaleX = scale
//                scaleY = scale
//            },
//        shape = RoundedCornerShape(8.dp),
//        backgroundColor = AppColors.cardNormal,
//        border = if (isFocused) BorderStroke(2.dp, AppColors.accent) else null,
//        elevation = elevation
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .pointerInput(Unit) {
//                    detectTapGestures(onTap = { onClick() })
//                }) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(92.dp)
//                    .background(if (isFocused) Color.DarkGray else Color.Gray)
//            )
//            Spacer(modifier = Modifier.height(6.dp))
//            Text(item.title, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp), maxLines = 1)
//        }
//    }
//}
//
//@Composable
//fun Footer(currentSource: String) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(36.dp)
//            .background(AppColors.cardNormal)
//            .padding(horizontal = 12.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text("当前片源：$currentSource", color = Color.White, fontSize = 12.sp)
//        Spacer(modifier = Modifier.weight(1f))
//        Text("网络：正常", color = Color.LightGray, fontSize = 12.sp)
//    }
//}