package org.geojson.jackson

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.annotation.JsonCreator



enum class CrsType {
    NAME, LINK;

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: String): CrsType {
            return valueOf(value.toUpperCase())
        }

    }

    @JsonValue
    fun toValue(): String = name.toLowerCase()

}
