# ServerStatistics
 Adds a Server Statistics system that combines all player stats together.

# Useful events for developers:

StatChangedEvent - Sent when a server statistic changes.
AdvancedFirstTimeEvent - Sent when an advancement is earned for the first time on the entire server.
AdvancementCompletelyRevokedEvent - Sent when all players who had an advancement had it revoked. This is usually only happens when a command is ran to do so.
ServerStatsResetEvent - This is sent when the server stats data cache is reset. Accounting for this is not necessary unless your mod used ServerStats before version 1.1.8

# Using as a Gradle Dependency
1. Put this in your build script
```gradle
repositories {
    maven {
        name = "ServerStatistics"
        url = uri("https://maven.pkg.github.com/Vicious-MCModding/ServerStatistics")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GPR_USER")
            password = project.findProperty("gpr.key") ?: System.getenv("GPR_API_KEY")
        }
    }
}
dependencies {
    compile "com.vicious:serverstatistics:MCVERSION-VERSION"
}
```
2. If you don't have a GPR Key you need to make one. To do so, go here: [https://github.com/settings/tokens](https://github.com/settings/tokens)
* Click generate new token
* Click the read:packages checkbox
* Scroll down and generate that boyo. Copy the key, and keep it safe somewhere.

You can do this in two ways:

1. By putting it in your System Environment Variables using the names in the build.gradle code above.
2. or by putting them in your gradle.properties file (INSECURE! NOT RECOMMENDED!)

* You should be good now BUT be warned. This key grants anyone with it special privileges (the ones you gave it in that checkbox section). Make sure that this key is kept private.

Doing this will both provide you ALL dependencies for the core and any of the dependencies for the dependencies (Wow its like magic).
