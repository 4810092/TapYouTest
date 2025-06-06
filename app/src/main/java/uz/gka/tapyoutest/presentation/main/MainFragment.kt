package uz.gka.tapyoutest.presentation.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.gka.tapyoutest.App
import uz.gka.tapyoutest.R
import uz.gka.tapyoutest.databinding.FragmentMainBinding
import uz.gka.tapyoutest.domain.validator.InputValidator
import uz.gka.tapyoutest.utils.viewBinding

class MainFragment : MvpAppCompatFragment(R.layout.fragment_main), MainView {

    private val binding by viewBinding(FragmentMainBinding::bind)

    private val presenter by moxyPresenter {
        MainPresenter(
            App.component.getPointsUseCase(),
            InputValidator(),
            App.component.pointsCache(),
            App.component.router()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListeners()
        initObservers()
    }

    private fun initListeners() = with(binding) {
        btnGo.setOnClickListener {
            presenter.onLoadPointsClicked(etCount.text.toString())
        }
        etCount.setOnEditorActionListener { _, _, _ ->
            btnGo.performClick()
            true
        }
        flProgress.setOnClickListener { }
    }

    private fun initObservers() {}

    override fun showLoading(show: Boolean) = with(binding) {
        flProgress.isVisible = show
        etCount.isEnabled = !show
        btnGo.isEnabled = !show
    }

    override fun showInvalidNumberError() {
        binding.etCount.error = getString(R.string.main_invalid_number)
    }

    override fun showPointsLoadingError(message: String?) {
        val existMessage = message ?: getString(R.string.main_points_load_error)
        Toast.makeText(requireContext(), existMessage, Toast.LENGTH_SHORT).show()
    }
}