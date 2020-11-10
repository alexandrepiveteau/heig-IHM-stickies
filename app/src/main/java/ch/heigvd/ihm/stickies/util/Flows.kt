package ch.heigvd.ihm.stickies.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun timestampFlow(timestamp: Long?): Flow<Boolean> = flow {

    // Fast return.
    if (timestamp == null) {
        emit(false)
        return@flow
    }

    // Await timestamp.
    while (true) {
        val now = System.currentTimeMillis()
        if (now > timestamp) {
            emit(true)
            return@flow
        } else {
            emit(false)
            delay(timestamp - now)
        }
    }
}
