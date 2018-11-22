import com.moowork.gradle.node.npm.NpmTask

plugins {
    base
    id("com.moowork.node") version "1.2.0"
}

val isCi = System.getenv("CI") != null

node {
    version = "10.13.0"
    npmVersion = "6.4.1"
    if (isCi) {
        // we specify a custom installation directory because of permission issues on Docker
        workDir = file("/tmp/node")
    }
    download = true
}


tasks {

    // Lint
    val lint by creating {
        dependsOn("npm_run_lint")
    }

    // Unit tests
    val test by creating {
        if (isCi) {
            dependsOn("npm_run_test-ci")
        } else {
            dependsOn("npm_run_test")
        }
        dependsOn(lint)
    }

    // E2E tests
    val clientIntegrationTest by creating {
        dependsOn("npm_run_e2e")
        dependsOn(lint)
    }

    val check by getting {
        dependsOn(test)
    }

    // Build assemble
    val assemble by getting {
        dependsOn("npm_run_build")
        dependsOn(lint)
    }

}

