package uz.gka.tapyoutest.presentation.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import uz.gka.tapyoutest.presentation.main.MainFragment
import uz.gka.tapyoutest.presentation.result.ResultFragment

object Screens {
    fun main() = FragmentScreen { MainFragment() }
    fun result() = FragmentScreen { ResultFragment() }
}
