package uz.gka.tapyoutest.presentation.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gka.tapyoutest.App
import uz.gka.tapyoutest.R
import uz.gka.tapyoutest.databinding.FragmentMainBinding
import uz.gka.tapyoutest.domain.model.Point
import uz.gka.tapyoutest.utils.viewBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)

    private val viewModel by viewModels<MainViewModel> {
        App.component.getViewModelFactory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListeners()
        initObservers()
    }

    private fun submitUiAction(action: MainUiAction) {
        viewModel.handleUiAction(action)
    }

    private fun initListeners() = with(binding) {
        btnGo.setOnClickListener {
            submitUiAction(MainUiAction.LoadPoints(etCount.text.toString()))
        }
        etCount.setOnEditorActionListener { _, _, _ ->
            btnGo.performClick()
            true
        }
        flProgress.setOnClickListener { }
    }

    private fun initObservers() {
        viewModel.effect.onEach { onEffect(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onEffect(effect: MainEffect) {
        when (effect) {
            is MainEffect.InvalidNumber -> showInvalidNumberError()
            is MainEffect.Loading -> onLoading(effect.show)
            is MainEffect.PointsLoaded -> onPointsLoaded(effect.points)
            is MainEffect.PointsLoadingError -> onPointsLoadingError(effect.message)
        }
    }

    private fun onLoading(show: Boolean) = with(binding) {
        flProgress.isVisible = show
        etCount.isEnabled = !show
        btnGo.isEnabled = !show
    }

    private fun onPointsLoadingError(message: String?) {
        val existMessage = message ?: getString(R.string.main_points_load_error)
        Toast.makeText(requireContext(), existMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showInvalidNumberError() {
        binding.etCount.error = getString(R.string.main_invalid_number)
    }

    private fun onPointsLoaded(points: List<Point>) {
        val action = MainFragmentDirections.actionMainToResult(points.toTypedArray())
        findNavController().navigate(action)
    }
}