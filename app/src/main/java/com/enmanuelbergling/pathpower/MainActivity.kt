package com.enmanuelbergling.pathpower

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.enmanuelbergling.pathpower.ui.animation.liquid.BetterLiquidBottomBar
import com.enmanuelbergling.pathpower.ui.animation.liquid.LiquidCircles
import com.enmanuelbergling.pathpower.ui.animation.liquid.MoreIconsContainer
import com.enmanuelbergling.pathpower.ui.theme.PathPowerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PathPowerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        BetterLiquidBottomBar()
                    }
                }
            }
        }
    }
}