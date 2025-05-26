package uz.gka.tapyoutest.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import uz.gka.tapyoutest.domain.model.ChartSaveResult
import uz.gka.tapyoutest.domain.repository.ChartSaver
import javax.inject.Inject

class ScopedChartSaver @Inject constructor(
    private val context: Context
) : ChartSaver {

    override fun save(bitmap: Bitmap): ChartSaveResult {
        require(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            "ScopedChartSaver should only be used on Android 10+"
        }


        val filename = "chart_${System.currentTimeMillis()}.png"
        val mimeType = "image/png"
        val relativeLocation = Environment.DIRECTORY_PICTURES + "/TapYouCharts"


        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }

        val contentResolver = context.contentResolver
        val uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        val existUri = uri
            ?: throw IllegalStateException("Failed to insert image into MediaStore: URI is null")

        contentResolver.openOutputStream(existUri)?.use { stream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }
        contentValues.clear()
        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
        contentResolver.update(existUri, contentValues, null, null)

        return ChartSaveResult.Scoped
    }
}
