package uz.gka.tapyoutest.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentContainerView
import uz.gka.tapyoutest.R
import uz.gka.tapyoutest.utils.isDarkTheme
import uz.gka.tapyoutest.utils.safeInsets

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        setupStatusBar()
        setupContentView()
    }

    private fun setupContentView() {
        val rootView = findViewById<FragmentContainerView>(R.id.nav_host_fragment)

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