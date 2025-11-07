package com.yhyzgn.tv.rubbish.activity

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.yhyzgn.tv.rubbish.activity.theme.TvAppTheme
import com.yhyzgn.tv.rubbish.activity.ui.HomeScreen
import com.yhyzgn.tv.rubbish.activity.view.HomeViewModel

class MainActivity : ComponentActivity() {
    private val vm: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContent {
            TvAppTheme {
                HomeScreen(
                    viewModel = vm,
                    onMediaClick = { media ->
                        Toast.makeText(applicationContext, media.title, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}