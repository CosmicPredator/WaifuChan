package com.cosmic.waifuchan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cosmic.waifuchan.models.ThemeMode
import com.cosmic.waifuchan.ui.theme.WaifuChanTheme
import com.cosmic.waifuchan.views.EnlargedImageView
import com.cosmic.waifuchan.views.MainPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val defaultTheme = isSystemInDarkTheme()
            var isDarkMode by rememberSaveable {
                mutableStateOf(defaultTheme)
            }
            WaifuChanTheme (
                darkTheme = isDarkMode
            ) {
                Surface {
                    RootNavGraph(
                        navController = rememberNavController(),
                        onThemeUpdated = { themeMode ->
                            isDarkMode = if (themeMode == ThemeMode.DEFAULT) {
                                defaultTheme
                            } else {
                                themeMode == ThemeMode.DARK
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RootNavGraph(
    navController: NavHostController,
    onThemeUpdated: (ThemeMode) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Routes.MAIN_PAGE
    ) {
        composable(Routes.MAIN_PAGE) { MainPage(rootNavController = navController, onThemeUpdated = onThemeUpdated) }
        composable(
            Routes.IMAGE_VIEW,
            arguments = listOf(navArgument("imageUrl") { type = NavType.StringType })
        ) {backStackEntry ->
            EnlargedImageView(backStackEntry.arguments?.getString("imageUrl"), navController = navController)
        }
    }
}

object Routes {
    const val MAIN_PAGE = "/mainpage"
    var IMAGE_VIEW = "/imageview?imageUrl={imageUrl}"
    const val SFW_PAGE = "/sfw"
    const val NSFW_PAGE = "/nsfw"
    const val SETTINGS = "/settings"
}