plugins {
    id("java")
    id("maven-publish")
}

group = "gg.flyte.pluginportal.mclicense"
version = "1.3.9"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    implementation("org.json:json:20240303")
}

publishing {
    repositories {
        maven {
            name = "flyte-repository"
            url = uri(
                "https://repo.flyte.gg/${
                    if (version.toString().endsWith("-SNAPSHOT")) "snapshots" else "releases"
                }"
            )
            credentials {
                username = property("MAVEN_USER").toString()
                password = property("MAVEN_PASSWORD").toString()
            }
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = group.toString()
                artifactId = project.name
                version = version.toString()

                from(components["java"])
            }
        }
    }
}