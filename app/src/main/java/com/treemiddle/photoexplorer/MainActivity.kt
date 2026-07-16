package com.treemiddle.photoexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.treemiddle.photoexplorer.common.theme.PhotoExplorerTheme
import com.treemiddle.photoexplorer.navigation.NavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotoExplorerTheme {
                NavHost()
            }
        }
    }
}
