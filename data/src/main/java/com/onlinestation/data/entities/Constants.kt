package com.onlinestation.data.entities

class Constants {

    companion object {
        //const val BASE_API_URL: String = "http://37.252.66.32/xradio_ap/"
        const val BASE_API_URL: String = "http://apk.am/xradio_ap/"
        const val API_KEY: String = "kIopUnVBQyRidSVVeUhdsKmqyf"
        const val errorDefaultCode = -1
        const val errorNotBalanceCode = 101
        const val errorDataNull = 102
        const val errorDataEmpty = 105
        const val notFountIndexExaction = 103
        const val notFountItemIntoListExaction = 104
        const val defaultUserID = 100
        const val defaultUserBalanceCount = 5

        const val METHOD_GET_GENRES = "getGenres"
        const val METHOD_GET_RADIOS = "getRadios"
        const val RADIOS_OFFSET = 0
        const val RADIOS_LIMIT = 10


        const val FOLDER_GENRES = "/uploads/genres/"
        const val FOLDER_RADIOS = "/uploads/radios/"
        const val FOLDER_THEMES = "/uploads/themes/"
    }
}