package dev.zmeuion.vitalya.data.mappers

import dev.zmeuion.vitalya.database.ScheduleDBO
import dev.zmeuion.vitalya.network.models.ScheduleDTO

fun ScheduleDTO.toScheduleDBO(scheduleDTO: ScheduleDTO): ScheduleDBO {

    return ScheduleDBO(
        id = null,
        group = scheduleDTO.group,
        timeStart = scheduleDTO.timeStart,
        timeEnd = scheduleDTO.timeEnd,
        name = scheduleDTO.name,
        type = scheduleDTO.type,
        place = scheduleDTO.place,
        teacher = scheduleDTO.teacher,
        date = scheduleDTO.date,
        pairNumber = scheduleDTO.pairNumber.toString(),

    )
}