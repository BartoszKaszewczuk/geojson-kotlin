package org.geojson

class Point : GeoJsonObject {

    var coordinates: LngLatAlt? = null

    constructor() {}

    constructor(coordinates: LngLatAlt) {
        this.coordinates = coordinates
    }

    constructor(longitude: Double, latitude: Double) {
        coordinates = LngLatAlt(longitude, latitude)
    }

    constructor(longitude: Double, latitude: Double, altitude: Double) {
        coordinates = LngLatAlt(longitude, latitude, altitude)
    }

    constructor(longitude: Double, latitude: Double, altitude: Double, vararg additionalElements: Double) {
        coordinates = LngLatAlt(longitude, latitude, altitude, *additionalElements)
    }

    override fun <T> accept(geoJsonObjectVisitor: GeoJsonObjectVisitor<T>): T {
        return geoJsonObjectVisitor.visit(this)!!
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o !is Point) {
            return false
        }
        if (!super.equals(o)) {
            return false
        }
        val point = o as Point?
        return !if (coordinates != null) coordinates != point!!.coordinates else point!!.coordinates != null
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + if (coordinates != null) coordinates!!.hashCode() else 0
        return result
    }

    override fun toString(): String {
        return "Point{" + "coordinates=" + coordinates + "} " + super.toString()
    }
}
