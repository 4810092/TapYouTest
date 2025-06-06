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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.gka.tapyoutest.App
import uz.gka.tapyoutest.R
import uz.gka.tapyoutest.databinding.FragmentResultBinding
import uz.gka.tapyoutest.domain.model.Point
import uz.gka.tapyoutest.utils.isDarkTheme
import uz.gka.tapyoutest.utils.viewBinding

class ResultFragment : MvpAppCompatFragment(R.layout.fragment_result), ResultView {

    private val binding by viewBinding(FragmentResultBinding::bind)

    private val presenter by moxyPresenter {
        ResultPresenter(
            App.component.saveChartUseCase(),
            App.component.pointsCache()
        )
    }

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
        initListeners()
        initObservers()
    }

    private fun submitUiAction(action: ResultUiAction) {
        when (action) {
            is ResultUiAction.SaveChart -> presenter.onSaveChartClicked(action.chartBitmap)
        }
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

    private fun initObservers() {}

    override fun showPoints(points: List<Point>) {
        setData(points)
    }

    private fun setData(points: List<Point>) {
        setupTable(points)
        setupChart(points)
    }

    override fun showMemoryAccessError() {
        showToast(R.string.result_memory_access_error)
    }

    override fun showSaveError() {
        showToast(R.string.result_save_error)
    }

    override fun showChartSaved() {
        showToast(R.string.result_chart_saved)
    }

    override fun showChartSavedIn(path: String) {
        showToast(getString(R.string.result_chart_saved_in, path))
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

    private fun showToast(stringResId: Int) {
        showToast(getString(stringResId))
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireContext(), message, Toast.LENGTH_SHORT
        ).show()
    }
}
