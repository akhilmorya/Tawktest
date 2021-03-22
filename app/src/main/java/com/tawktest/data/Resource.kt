package com.tawktest.data

/**
 * @author Avinash Kumar
 * @mail avisingh736@gmail.com
 */

class Resource<out T> private constructor(
        val status: Status,
        val message: String? = null,
        val data: T? = null
) {

    /**
     * Enum for resource ype
     * */
    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        /**
         * A generic method to create a successful resource of type T
         *
         * @param data
         * @return Resource<T>
         * */
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data = data)
        }

        /**
         * A generic method to create a successful resource of type T and message
         *
         * @param data
         * @param message
         * @return Resource<T>
         * */
        fun <T> success(data: T?, message: String?): Resource<T> {
            return Resource(Status.SUCCESS, message = message, data = data)
        }

        /**
         * A generic method to create an error resource of type T
         *
         * @param data
         * @return Resource<T>
         * */
        fun <T> error(data: T?): Resource<T> {
            return Resource(Status.ERROR, data = data)
        }

        /**
         * A generic method to create an error resource of type T
         *
         * @param message
         * @return Resource<T>
         * */
        fun <T> error(message: String?): Resource<T> {
            return Resource(Status.ERROR, message = message)
        }

        /**
         * A generic method to create an error resource of type T and message
         *
         * @param data
         * @param message
         * @return Resource<T>
         * */
        fun <T> error(data: T?, message: String?): Resource<T> {
            return Resource(Status.ERROR, message = message, data = data)
        }

        /**
         * Generic method to create a loading resource of type T
         * */
        fun <T> loading(data: T?): Resource<T> = Resource(Status.LOADING, data = data)
    }
}