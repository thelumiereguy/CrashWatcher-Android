package com.thelumierguy.crashwatcher.data

private const val MAX_STACK_TRACE_SIZE = 131071

@JvmInline
value class StackTrace(
    private val stackTrace: String
) {
    internal fun trimStackTrace(): String {
        return if (stackTrace.length > MAX_STACK_TRACE_SIZE) {
            val disclaimer = " [stack trace too large]"
            stackTrace.substring(
                0,
                MAX_STACK_TRACE_SIZE - disclaimer.length
            ) + disclaimer
        } else stackTrace
    }
}