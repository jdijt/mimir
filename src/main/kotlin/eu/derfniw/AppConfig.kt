package eu.derfniw

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "kb")
open interface Config {
    fun name(): String
}