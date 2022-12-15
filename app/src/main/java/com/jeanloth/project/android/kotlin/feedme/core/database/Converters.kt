package com.jeanloth.project.android.kotlin.feedme.core.database

import androidx.room.TypeConverter
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.*
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

class ProductCategoryConverter {
    @TypeConverter
    fun fromCode(code: Int): ProductCategory {
        return ProductCategory.values().first { it.code.equals(code) } ?: ProductCategory.FRUIT
    }

    @TypeConverter
    fun categoryToInt(category: ProductCategory): Int {
        return category.code
    }
}

class StatusConverter {
    @TypeConverter
    fun fromString(value: String): Status {
        return Status.values().firstOrNull { it.value == value } ?: Status.TO_DO
    }

    @TypeConverter
    fun statusToString(status: Status): String {
        return status.value
    }
}

class CoordinatesConverter {
    @TypeConverter
    fun fromValue(value: String): Coordinates {
        return value.split(",").getOrElse(0){ "0"} to value.split(",").getOrElse(1){"0"}
    }

    @TypeConverter
    fun coordinatesToValue(coordinates: Coordinates?): String {
        return "${coordinates?.first},${coordinates?.second}"
    }
}

class WrapperTypeConverter {
    @TypeConverter
    fun fromString(value: String): WrapperType {
        return WrapperType.values().firstOrNull { it.name == value } ?: WrapperType.NONE
    }

    @TypeConverter
    fun statusToString(status: WrapperType): String {
        return status.name
    }
}

class CommandStatusConverter {
    @TypeConverter
    fun fromString(value: String): CommandStatus {
        return CommandStatus.values().firstOrNull { it.label == value } ?: CommandStatus.TO_DO
    }

    @TypeConverter
    fun statusToString(status: CommandStatus): String {
        return status.label
    }
}