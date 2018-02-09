package io.data2viz.geojson

import kotlinx.coroutines.experimental.*
import org.w3c.fetch.Request
import runTest
import kotlin.browser.window
import kotlin.js.Date
import kotlin.js.Promise
import kotlin.test.*



class GeoJsonSerializationTests {
 
    @Test
    fun loadBigJson() = promise {
        val request = window.fetch(Request("base/build/classes/kotlin/test/ny.json"))
        val response = request.await()
        val text = response.text().await()
        val start = Date.now()
        val featureCollection = text.toGeoJson().asFeatureCollection()
        val multi = featureCollection.multipolygons
        val polygons = multi.flatMap { it.coordinates.toList() }
        println("Polygon size:: ${polygons.size}")
        println("Parsing in ${Date.now() - start} ms")
        assertEquals(104, polygons.size)
    }

    @Test
    fun loadWithMap() = promise {
        val request = window.fetch(Request("base/build/classes/kotlin/test/ny.json"))
        val response = request.await()
        val text = response.text().await()
        val start = Date.now()
        val asMap = JSON.parse<Map<String,Any>>(text)
        
        val type = asMap.get("type")
        
        val featureCollection = text.toGeoJson().asFeatureCollection()
        val multi = featureCollection.multipolygons
        val polygons = multi.flatMap { it.coordinates.toList() }
        println("Polygon size:: ${polygons.size}")
        println("Parsing in ${Date.now() - start} ms")
        assertEquals(104, polygons.size)
    }

    @Test
    fun pointJson() {
        val json = """{"type":"Point", "coordinates":[1.0, 2.0]}"""
        val p = json.toGeoJson().asPoint()
        assertEquals(p.coordinates.lon, 1.0)
        assertEquals(p.coordinates.lat, 2.0)
    }

    @Test
    fun lineString() {

        //language=JSON
        val json = """{"type":"LineString", "coordinates":[[1.0, 2.0],[3.0,4.0]]}"""
        val lineString = json.toGeoJson().asLineString()
        assertEquals(1.0, lineString.coordinates[0].lon)
    }

    @Test
    fun multiPoint() {
        val json = """{"type":"MultiPoint", "coordinates":[[1.0, 2.0],[3.0,4.0]]}"""
        val lineString = json.toGeoJson().asMultiPoint()
        assertEquals(1.0, lineString.coordinates[0].lon)
    }

    @Test
    fun multiLineString() {
        val json = """{
         "type": "MultiLineString",
         "coordinates": [
             [
                 [100.0, 0.0],
                 [101.0, 1.0]
             ],
             [
                 [102.0, 2.0],
                 [103.0, 3.0]
             ]
         ]
     }"""

        val multiLineString = json.toGeoJson().asMultiLineString()
        assertEquals(101.0, multiLineString.coordinates[0][1].lon)
    }

    @Test
    fun polygonWithoutHole() {
        val json = """ {
         "type": "Polygon",
         "coordinates": [
             [
                 [100.0, 0.0],
                 [101.0, 0.0],
                 [101.0, 1.0],
                 [100.0, 1.0],
                 [100.0, 0.0]
             ]
         ]
     }"""

        val polygon = json.toGeoJson().asPolygon()
        assertFalse(polygon.hasHoles)
    }

    @Test
    fun polygonWithHoles() {
        val json = """ {
         "type": "Polygon",
         "coordinates": [
             [
                 [100.0, 0.0],
                 [101.0, 0.0],
                 [101.0, 1.0],
                 [100.0, 1.0],
                 [100.0, 0.0]
             ],
             [
                 [100.8, 0.8],
                 [100.8, 0.2],
                 [100.2, 0.2],
                 [100.2, 0.8],
                 [100.8, 0.8]
             ]
         ]
        }"""

        val polygon = json.toGeoJson().asPolygon()
        assertTrue(polygon.hasHoles)
    }


    @Test
    fun featureCollection() {
        val json = """
   {
       "type": "FeatureCollection",
       "features": [{
           "type": "Feature",
           "geometry": {
               "type": "Point",
               "coordinates": [102.0, 0.5]
           },
           "properties": {
               "prop0": "value0"
           }
       }, {
           "type": "Feature",
           "geometry": {
               "type": "LineString",
               "coordinates": [
                   [102.0, 0.0],
                   [103.0, 1.0],
                   [104.0, 0.0],
                   [105.0, 1.0]
               ]
           },
           "properties": {
               "prop0": "value0",
               "prop1": 0.0
           }
       }, {
           "type": "Feature",
           "geometry": {
               "type": "Polygon",
               "coordinates": [
                   [
                       [100.0, 0.0],
                       [101.0, 0.0],
                       [101.0, 1.0],
                       [100.0, 1.0],
                       [100.0, 0.0]
                   ]
               ]
           },
           "properties": {
               "prop0": "value0",
               "prop1": {
                   "this": "that"
               }
           }
       }]
   }
        """.trimIndent()


        val featureCollection = json.toGeoJson().asFeatureCollection()
        assertEquals(3, featureCollection.features.size)
        val feature = featureCollection.features[2]

        val props = feature.properties as PolygonProps
        assertEquals("value0", props.prop0)

        val polygon: Polygon = feature.geometry.asPolygon()

        assertEquals(100.0, polygon.coordinates[0][0].lon)
    }
}

external interface PolygonProps {
    val prop0: String
    val prop1: ThisThat
}

external interface ThisThat {
    val `this`: String
}
