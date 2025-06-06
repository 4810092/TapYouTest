package uz.gka.tapyoutest.ui

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.github.terrakok.cicerone.androidx.AppNavigator
import uz.gka.tapyoutest.App
import uz.gka.tapyoutest.R
import uz.gka.tapyoutest.presentation.navigation.Screens
import uz.gka.tapyoutest.utils.isDarkTheme
import uz.gka.tapyoutest.utils.safeInsets

class MainActivity : AppCompatActivity() {

    private val router get() = App.component.router()
    private val navigatorHolder get() = App.component.navigatorHolder()
    private val navigator by lazy { AppNavigator(this, R.id.flMainRoot) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        setupStatusBar()
        setupContentView()
        if (savedInstanceState == null) {
            router.newRootScreen(Screens.main())
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun setupContentView() {
        val rootView = findViewById<FrameLayout>(R.id.flMainRoot)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val insetTypes = WindowInsetsCompat.Type.systemBars() or
                    WindowInsetsCompat.Type.ime() or
                    WindowInsetsCompat.Type.systemGestures()

            val systemInsets = insets.getInsets(insetTypes)
            val cutoutInsets = insets.safeInsets()

            view.setPadding(
                maxOf(systemInsets.left, cutoutInsets.left),
                maxOf(systemInsets.top, cutoutInsets.top),
                maxOf(systemInsets.right, cutoutInsets.right),
                maxOf(systemInsets.bottom, cutoutInsets.bottom)
            )

            insets
        }
    }


    private fun setupStatusBar() {
        val isDarkTheme = resources.isDarkTheme()

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = !isDarkTheme
            isAppearanceLightNavigationBars = !isDarkTheme
        }
    }
}