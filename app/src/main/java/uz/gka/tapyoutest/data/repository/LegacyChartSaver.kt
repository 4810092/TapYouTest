package uz.gka.tapyoutest.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import uz.gka.tapyoutest.domain.model.ChartSaveResult
import uz.gka.tapyoutest.domain.repository.ChartSaver
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class LegacyChartSaver @Inject constructor(
    private val context: Context
) : ChartSaver {

    override fun save(bitmap: Bitmap): ChartSaveResult {
        require(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            "LegacyChartSaver should only be used on Android 9 and below"
        }

        val dir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/TapYouCharts")
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, "chart_${System.currentTimeMillis()}.png")

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        MediaScannerConnection.scanFile(
            context, arrayOf(file.absolutePath), arrayOf("image/png"), null
        )

        return ChartSaveResult.Legacy(file.absolutePath)
    }
}
