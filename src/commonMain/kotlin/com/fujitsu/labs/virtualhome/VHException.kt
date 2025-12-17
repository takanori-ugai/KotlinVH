package com.fujitsu.labs.virtualhome

/**
 * Custom exception class.
 *
 * @property str The message for the exception.
 */
class VHException(
    str: String,
) : Exception(str)
