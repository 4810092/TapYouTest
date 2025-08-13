package uz.gka.tapyoutest.ui

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import uz.gka.tapyoutest.presentation.main.MainFragment
import uz.gka.tapyoutest.presentation.result.ResultFragment

@Composable
fun FragmentHost(fragment: Fragment, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val containerId = remember { View.generateViewId() }

    AndroidView(
        factory = { ctx ->
            FrameLayout(ctx).apply {
                id = containerId
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        update = { _ ->
            val fm = (context as? AppCompatActivity)?.supportFragmentManager ?: return@AndroidView
            if (fm.findFragmentById(containerId) == null) {
                fm.commit {
                    replace(containerId, fragment, fragment.javaClass.simpleName)
                }
            }
        },
        modifier = modifier
    )
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Main : Screen("main", "Main", Icons.Filled.Home)
    object Result : Screen("result", "Result", Icons.Filled.Done)
}

@Composable
fun MainScreen(navController: NavHostController) {
    val items = listOf(Screen.Main, Screen.Result)


    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Main.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Main.route) {
                val fragment = remember { MainFragment() }
                fragment.onNavigateToResult = {
                    navController.navigate(Screen.Result.route)
                }
                FragmentHost(fragment = fragment)
            }
            composable(Screen.Result.route) {
                FragmentHost(fragment = remember { ResultFragment() })
            }
        }
    }
}
