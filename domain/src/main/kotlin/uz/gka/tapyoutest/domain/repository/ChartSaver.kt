package uz.gka.tapyoutest.domain.repository

import uz.gka.tapyoutest.domain.model.ChartSaveResult

interface ChartSaver {
    fun save(data: ByteArray): ChartSaveResult
}