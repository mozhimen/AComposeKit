package com.mozhimen.composek.ui.text

import androidx.compose.ui.util.fastFold
import androidx.compose.ui.util.fastMap
import java.util.SortedSet

/**
 * @ClassName JvmAnnotatedString
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/30
 * @Version 1.0
 */
/**
 * The core function of [AnnotatedString] transformation.
 *
 * @param transform the transformation method
 * @return newly allocated transformed AnnotatedString
 */
internal /*actual*/ fun AnnotatedString.transform(
    transform: (String, Int, Int) -> String
): AnnotatedString {
    val transitions = sortedSetOf(0, text.length)
    collectRangeTransitions(spanStylesOrNull, transitions)
    collectRangeTransitions(paragraphStylesOrNull, transitions)
    collectRangeTransitions(annotations, transitions)

    var resultStr = ""
    val offsetMap = mutableMapOf(0 to 0)
    transitions.windowed(size = 2) { (start, end) ->
        resultStr += transform(text, start, end)
        offsetMap.put(end, resultStr.length)
    }

    val newSpanStyles = spanStylesOrNull?.fastMap {
        // The offset map must have mapping entry from all style start, end position.
        AnnotatedString.Range(it.item, offsetMap[it.start]!!, offsetMap[it.end]!!)
    }
    val newParaStyles = paragraphStylesOrNull?.fastMap {
        AnnotatedString.Range(it.item, offsetMap[it.start]!!, offsetMap[it.end]!!)
    }
    val newAnnotations = annotations?.fastMap {
        AnnotatedString.Range(it.item, offsetMap[it.start]!!, offsetMap[it.end]!!)
    }

    return AnnotatedString(
        text = resultStr,
        spanStylesOrNull = newSpanStyles,
        paragraphStylesOrNull = newParaStyles,
        annotations = newAnnotations
    )
}

/**
 * Adds all [AnnotatedString.Range] transition points
 *
 * @param ranges The list of AnnotatedString.Range
 * @param target The output list
 */
private fun collectRangeTransitions(
    ranges: List<AnnotatedString.Range<*>>?,
    target: SortedSet<Int>
) {
    ranges?.fastFold(target) { acc, range ->
        acc.apply {
            add(range.start)
            add(range.end)
        }
    }
}
