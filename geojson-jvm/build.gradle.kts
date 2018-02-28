dependencies {
    
    expectedBy(project(":geojson-common"))
    
    compile ("com.fasterxml.jackson.core:jackson-core:2.7.3")           
    compile ("com.fasterxml.jackson.core:jackson-databind:2.7.3")       
    compile ("com.fasterxml.jackson.core:jackson-annotations:2.7.0")    
    compile ("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.0")

    
    testCompile ("org.mockito:mockito-core:1.10.19")
    testCompile ("junit:junit:4.12")
}


tasks {
    val copyJsonTestFiles by creating(Copy::class) {
        from("../geojson-common/src/test/resources")
        into("$buildDir/classes/kotlin/test")
    }
    
    "test" {
        dependsOn(copyJsonTestFiles)
    }
}