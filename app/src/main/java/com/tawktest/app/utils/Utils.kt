package com.tawktest.app.utils

/**
 * @author Avinash Kumar
 * @mail avisingh736@gmail.com
 */

class Utils {
    companion object {

        /**
         * Color matrix that flips the components (<code>-1.0f * c + 255 = 255 - c</code>)
         * and keeps the alpha intact.
         */
        val NEGATIVE = floatArrayOf(
                -1.0f, 0.0f, 0.0f, 0.0f, 255.0f,
                0.0f, -1.0f, 0.0f, 0.0f, 255.0f,
                0.0f, 0.0f, -1.0f, 0.0f, 255.0f,
                0.0f, 0.0f, 0.0f, 1.0f, 0.0f
        )
    }
}