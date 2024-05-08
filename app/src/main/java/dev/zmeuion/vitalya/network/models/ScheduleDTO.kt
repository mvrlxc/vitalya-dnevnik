package dev.zmeuion.vitalya.network.models

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleDTO(
    val id: Int,
    val group: String,
    val timeStart: String,
    val timeEnd: String,
    val name: String,
    val type: String,
    val place: String,
    val teacher: String,
    val date: String,
    val pairNumber: Int,
    val homework: String,
)
