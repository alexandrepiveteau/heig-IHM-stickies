package ch.heigvd.ihm.stickies.util

/**
 * Iterates through a [List] using the index and calls [action] for each item.
 * This does not allocate an iterator like [Iterable.forEachIndexed].
 */
inline fun <T> List<T>.fastForEachIndexedReversed(action: (Int, T) -> Unit) {
    for (index in indices.reversed()) {
        val item = get(index)
        action(index, item)
    }
}