package org.geojson.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.geojson.LngLatAlt

import java.io.IOException
import java.util.ArrayList

class LngLatAltDeserializer : JsonDeserializer<LngLatAlt>() {

    @Throws(IOException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): LngLatAlt {
        if (jp.isExpectedStartArrayToken) {
            return deserializeArray(jp, ctxt)
        }
        throw ctxt.mappingException(LngLatAlt::class.java)
    }

    @Throws(IOException::class)
    protected fun deserializeArray(jp: JsonParser, ctxt: DeserializationContext): LngLatAlt {
        val node = LngLatAlt()
        node.longitude = extractDouble(jp, ctxt, false)
        node.latitude = extractDouble(jp, ctxt, false)
        node.setAltitude(extractDouble(jp, ctxt, true))
        val additionalElementsList = ArrayList<Double>()
        while (jp.hasCurrentToken() && jp.currentToken != JsonToken.END_ARRAY) {
            val element = extractDouble(jp, ctxt, true)
            if (!java.lang.Double.isNaN(element)) {
                additionalElementsList.add(element)
            }
        }
        val additionalElements = DoubleArray(additionalElementsList.size)
        for (i in additionalElements.indices) {
            additionalElements[i] = additionalElementsList[i]
        }
        node.setAdditionalElements(*additionalElements)
        return node
    }

    @Throws(IOException::class)
    private fun extractDouble(jp: JsonParser, ctxt: DeserializationContext, optional: Boolean): Double {
        val token = jp.nextToken()
        return if (token == null) {
            if (optional)
                java.lang.Double.NaN
            else
                throw ctxt.mappingException("Unexpected end-of-input when binding data into LngLatAlt")
        } else {
            when (token) {
                JsonToken.END_ARRAY -> if (optional)
                    java.lang.Double.NaN
                else
                    throw ctxt.mappingException("Unexpected end-of-input when binding data into LngLatAlt")
                JsonToken.VALUE_NUMBER_FLOAT -> jp.doubleValue
                JsonToken.VALUE_NUMBER_INT -> jp.longValue.toDouble()
                JsonToken.VALUE_STRING -> jp.valueAsDouble
                else -> throw ctxt.mappingException(
                    "Unexpected token (" + token.name + ") when binding data into LngLatAlt"
                )
            }
        }
    }
}
