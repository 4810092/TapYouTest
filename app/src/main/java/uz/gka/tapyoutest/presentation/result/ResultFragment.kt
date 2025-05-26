package uz.gka.tapyoutest.presentation.result

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gka.tapyoutest.App
import uz.gka.tapyoutest.R
import uz.gka.tapyoutest.databinding.FragmentResultBinding
import uz.gka.tapyoutest.domain.model.Point
import uz.gka.tapyoutest.utils.isDarkTheme
import uz.gka.tapyoutest.utils.viewBinding

class ResultFragment : Fragment(R.layout.fragment_result) {

    private val binding by viewBinding(FragmentResultBinding::bind)

    private val viewModel by viewModels<ResultViewModel> {
        App.component.getViewModelFactory()
    }

    private val args: ResultFragmentArgs by navArgs()
    private val points: List<Point> get() = args.points.toList()

    private lateinit var requestStoragePermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestStoragePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                submitUiAction(ResultUiAction.SaveChart(binding.lcPoints.chartBitmap))
            } else {
                showToast(R.string.result_permission_denied)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupTable(points)
        setupChart(points)
        initListeners()
        initObservers()
    }

    private fun submitUiAction(action: ResultUiAction) {
        viewModel.handleUiAction(action)
    }

    private fun checkPermissionAndSave() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            submitUiAction(ResultUiAction.SaveChart(binding.lcPoints.chartBitmap))
        } else {
            val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(
                    requireContext(), permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                submitUiAction(ResultUiAction.SaveChart(binding.lcPoints.chartBitmap))
            } else {
                requestStoragePermissionLauncher.launch(permission)
            }
        }
    }

    private fun initListeners() {
        binding.btnSave.setOnClickListener { checkPermissionAndSave() }
    }

    private fun initObservers() {
        viewModel.effect.onEach { onEffect(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupTable(points: List<Point>) = with(binding) {
        rvTable.adapter = PointsAdapter(points)
    }

    private fun setupChart(points: List<Point>) {
        val entries = points.map { Entry(it.x.toFloat(), it.y.toFloat()) }

        val isDark = resources.isDarkTheme()
        val textColorByTheme = if (isDark) Color.WHITE else Color.BLACK

        val dataSet = LineDataSet(entries, getString(R.string.result_chart_label)).apply {
            lineWidth = 2f
            setDrawCircles(true)
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        }

        binding.lcPoints.apply {
            data = LineData(dataSet).apply { setValueTextColor(textColorByTheme) }

            description.isEnabled = false
            legend.textColor = textColorByTheme

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = textColorByTheme
            }

            axisLeft.textColor = textColorByTheme
            axisRight.textColor = textColorByTheme

            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)

            animateX(300)
            invalidate()
        }
    }


    private fun onEffect(effect: ResultEffect) {
        when (effect) {
            ResultEffect.MemoryAccessError -> showToast(R.string.result_memory_access_error)

            ResultEffect.SaveError -> showToast(R.string.result_save_error)

            ResultEffect.Saved -> showToast(R.string.result_chart_saved)

            is ResultEffect.SavedIn -> showToast(
                getString(
                    R.string.result_chart_saved_in, effect.savedPath
                )
            )
        }
    }

    private fun showToast(stringResId: Int) {
        showToast(getString(stringResId))
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireContext(), message, Toast.LENGTH_SHORT
        ).show()
    }
}
