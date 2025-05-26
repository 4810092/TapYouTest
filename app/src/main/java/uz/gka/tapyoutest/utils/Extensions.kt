package uz.gka.tapyoutest.utils

import android.content.res.Configuration
import android.content.res.Resources
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty


fun <T : ViewBinding> Fragment.viewBinding(bind: (View) -> T): ReadOnlyProperty<Fragment, T> {
    return FragmentViewBindingDelegate(this, bind)
}

fun Resources.isDarkTheme(): Boolean {
    return (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
}


fun WindowInsetsCompat.safeInsets(): Insets {
    val cutout = displayCutout
    return if (cutout != null) {
        Insets.of(
            cutout.safeInsetLeft,
            cutout.safeInsetTop,
            cutout.safeInsetRight,
            cutout.safeInsetBottom
        )
    } else {
        Insets.NONE
    }
}