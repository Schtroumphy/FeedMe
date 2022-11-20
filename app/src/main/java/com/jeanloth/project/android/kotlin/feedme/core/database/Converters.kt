package com.jeanloth.project.android.kotlin.feedme.core.database

import androidx.room.TypeConverter
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.CommandStatus
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Status
import java.time.LocalDate

class DateTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDate? {
        return value?.let {  LocalDate.parse(value) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): String? {
        return date.toString()
    }
}

class StatusConverter {
    @TypeConverter
    fun fromString(value: String): Status {
        return Status.values().first { it.value.equals(value) } ?: Status.TO_DO
    }

    @TypeConverter
    fun statusToString(status: Status): String {
        return status.value
    }
}

class CommandStatusConverter {
    @TypeConverter
    fun fromString(value: String): CommandStatus {
        return CommandStatus.values().first { it.label.equals(value) } ?: CommandStatus.TO_DO
    }

    @TypeConverter
    fun statusToString(status: CommandStatus): String {
        return status.label
    }
}