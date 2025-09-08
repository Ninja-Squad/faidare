plugins {
    id("org.sonarqube") version "6.3.1.5724"
}

sonarqube {
    properties {
        property ("sonar.projectKey", "urgi-is_faidare_AXlGu_BxPgTGgvpuDgeB")
        property ("sonar.qualitygate.wait", false)
        property("sonar.exclusions", "**/*.gradle.kts")
    }
}
