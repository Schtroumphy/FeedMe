package com.jeanloth.project.android.kotlin.feedme.core.database

import androidx.room.TypeConverter
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.CommandStatus
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.ProductCategory
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Status
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperType
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
        return Status.values().first { it.value.equals(value) } ?: Status.TO_DO
    }

    @TypeConverter
    fun statusToString(status: Status): String {
        return status.value
    }
}

class WrapperTypeConverter {
    @TypeConverter
    fun fromString(value: String): WrapperType {
        return WrapperType.values().first { it.name.equals(value) } ?: WrapperType.NONE
    }

    @TypeConverter
    fun statusToString(status: WrapperType): String {
        return status.name
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