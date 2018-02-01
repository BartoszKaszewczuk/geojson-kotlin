package org.geojson.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import org.geojson.LngLatAlt
import org.geojson.Polygon
import org.junit.Test

import org.junit.Assert.assertEquals

class PolygonTest {

    private val mapper = ObjectMapper()

    @Test
    @Throws(Exception::class)
    fun itShouldSerialize() {
        val polygon = Polygon(MockData.EXTERNAL)
        assertEquals(
            "{\"type\":\"Polygon\",\"coordinates\":" + "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]]]}",
            mapper.writeValueAsString(polygon)
        )
    }

    @Test
    @Throws(Exception::class)
    fun itShouldSerializeWithHole() {
        val polygon = Polygon(MockData.EXTERNAL)
        polygon.addInteriorRing(MockData.INTERNAL)
        assertEquals(
            "{\"type\":\"Polygon\",\"coordinates\":"
                    + "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],"
                    + "[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]]}",
            mapper.writeValueAsString(polygon)
        )
    }

    @Test(expected = RuntimeException::class)
    @Throws(Exception::class)
    fun itShouldFailOnAddInteriorRingWithoutExteriorRing() {
        val polygon = Polygon()
        polygon.addInteriorRing(MockData.EXTERNAL)
    }

    @Test
    @Throws(Exception::class)
    fun itShouldDeserialize() {
        val polygon = mapper.readValue(
            "{\"type\":\"Polygon\",\"coordinates\":"
                    + "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],"
                    + "[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]]}", Polygon::class.java
        )
        assertListEquals(MockData.EXTERNAL, polygon.exteriorRing)
        assertListEquals(MockData.INTERNAL, polygon.getInteriorRing(0))
        assertListEquals(MockData.INTERNAL, polygon.interiorRings[0])
    }

    private fun assertListEquals(expectedList: List<LngLatAlt>, actualList: List<LngLatAlt>) {
        for (x in actualList.indices) {
            val expected = expectedList[x]
            val actual = actualList[x]
            PointTest.assertLngLatAlt(expected.longitude, expected.latitude, expected.getAltitude(), actual)
        }
    }
}
