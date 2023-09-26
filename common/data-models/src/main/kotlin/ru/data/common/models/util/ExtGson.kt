package ru.data.common.models.util

import com.google.gson.FieldNamingStrategy
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.json.JsonNamingStrategy
import java.lang.reflect.Field

fun Any.objectToJson(): String = Gson().toJson(this)
inline fun <reified T> String.jsonToObject(): T? = try {
    Gson().fromJson(this, T::class.java) ?: null
} catch (e: Exception) {
    e.printStackTrace()
    null
}

fun <T> stringToArray(s: String?, clazz: Class<Array<T>>?): List<T>? = try {
    val arr = Gson().fromJson(s, clazz)
    arr.toList()
} catch (e: Exception) {
    e.printStackTrace()
    null
}


class SnakeCaseToCamelCaseStrategy : FieldNamingStrategy {
    override fun translateName(field: Field): String {
        val serializedName = field.getAnnotation(SerializedName::class.java)
        return serializedName?.value?.toCamelCase() ?: field.name.toCamelCase()
    }
}

class SnakeCaseStrategy : JsonNamingStrategy {

    override fun serialNameForJson(
        descriptor: SerialDescriptor,
        elementIndex: Int,
        serialName: String
    ): String {
        TODO("Not yet implemented")
    }


}

fun String.toCamelCase(): String {
    val parts = this.split("_").mapIndexed { index, part ->
        if (index == 0) part else part.capitalize()
    }
    return parts.joinToString("")
}

public fun String.capitalize(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

class CamelCaseToSnakeCaseStrategy : FieldNamingStrategy {
    override fun translateName(field: Field): String {
        val serializedName = field.getAnnotation(SerializedName::class.java)
        return serializedName?.value?.toSnakeCase() ?: field.name.toSnakeCase()
    }
}

fun String.toSnakeCase(): String {
    val result = StringBuilder()
    for (i in indices) {
        val c = this[i]
        if (c.isUpperCase()) {
            if (i > 0) {
                result.append("_")
            }
            result.append(c.lowercaseChar())
        } else {
            result.append(c)
        }
    }
    return result.toString()
}

