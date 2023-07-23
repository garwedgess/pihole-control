package eu.wedgess.mihole

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import eu.wedgess.mihole.ui.app.view.MiHoleApp

@AndroidEntryPoint
class MiHoleEntryPointActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiHoleApp()
        }
    }
}