package uz.gka.tapyoutest.presentation.result

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import uz.gka.tapyoutest.App
import uz.gka.tapyoutest.R
import uz.gka.tapyoutest.utils.isDarkTheme
import uz.gka.tapyoutest.utils.toPngBytes

@Composable
fun ResultScreen() {
    val context = LocalContext.current
    val viewModel: ResultViewModel = viewModel(factory = App.component.getViewModelFactory())

    val state by viewModel.state.collectAsState(ResultState.Initial)
    val chartHolder = remember { mutableStateOf<LineChart?>(null) }
    val pendingChartBytes = remember { mutableStateOf<ByteArray?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            pendingChartBytes.value?.let { viewModel.handleUiAction(ResultUiAction.SaveChart(it)) }
        } else {
            Toast.makeText(context, context.getString(R.string.result_permission_denied), Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ResultEffect.MemoryAccessError -> Toast.makeText(context, context.getString(R.string.result_memory_access_error), Toast.LENGTH_SHORT).show()
                ResultEffect.SaveError -> Toast.makeText(context, context.getString(R.string.result_save_error), Toast.LENGTH_SHORT).show()
                ResultEffect.Saved -> Toast.makeText(context, context.getString(R.string.result_chart_saved), Toast.LENGTH_SHORT).show()
                is ResultEffect.SavedIn -> Toast.makeText(context, context.getString(R.string.result_chart_saved_in, effect.savedPath), Toast.LENGTH_SHORT).show()
            }
        }
    }

    val points = when (state) {
        is ResultState.PointsData -> (state as ResultState.PointsData).points
        ResultState.Initial -> emptyList()
    }

    fun checkPermissionAndSave(bytes: ByteArray) {
        pendingChartBytes.value = bytes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            viewModel.handleUiAction(ResultUiAction.SaveChart(bytes))
        } else {
            val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                viewModel.handleUiAction(ResultUiAction.SaveChart(bytes))
            } else {
                launcher.launch(permission)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(points.size) { index ->
                val point = points[index]
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.Start) {
                    Text(text = point.x.toString(), modifier = Modifier.weight(1f))
                    Text(text = point.y.toString(), modifier = Modifier.weight(1f))
                }
            }
        }
        AndroidView(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            factory = { ctx ->
                LineChart(ctx).also { chartHolder.value = it }
            },
            update = { chart ->
                val entries = points.map { Entry(it.x.toFloat(), it.y.toFloat()) }
                val isDark = chart.context.resources.isDarkTheme()
                val textColor = if (isDark) Color.WHITE else Color.BLACK
                val dataSet = LineDataSet(entries, chart.context.getString(R.string.result_chart_label)).apply {
                    lineWidth = 2f
                    setDrawCircles(true)
                    mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                }
                chart.data = LineData(dataSet).apply { setValueTextColor(textColor) }
                chart.description.isEnabled = false
                chart.legend.textColor = textColor
                chart.xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    this.textColor = textColor
                }
                chart.axisLeft.textColor = textColor
                chart.axisRight.textColor = textColor
                chart.setTouchEnabled(true)
                chart.isDragEnabled = true
                chart.setScaleEnabled(true)
                chart.setPinchZoom(true)
                chart.animateX(300)
                chart.invalidate()
            }
        )
        Button(
            onClick = {
                chartHolder.value?.let { checkPermissionAndSave(it.chartBitmap.toPngBytes()) }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = stringResource(R.string.result_save_chart))
        }
    }
}
