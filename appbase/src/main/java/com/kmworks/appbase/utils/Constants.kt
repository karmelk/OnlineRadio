package com.kmworks.appbase.utils

abstract class Constants {
    companion object{
        const val BASE_API_URL: String = "http://api.shoutcast.com"
        const val BASE_OWNER_SERVER_API_URL: String = "https://apk.am/xradio_ap/" //  /api/api.php?method=getGenres&api_key=kIopUnVBQyRidSVVeUhdsKmqyf
        const val BASE_STATION_URL: String = "http://yp.shoutcast.com"
       // const val API_KEY: String = "HLLYSYyBlCi5mXEU"
        const val API_KEY: String = "kIopUnVBQyRidSVVeUhdsKmqyf"
        const val DATA_FORMAT: String = "json"

        //const val STATION_FORMAT: String = "audio/mpeg"
        const val LIMIT: Int = 100
        const val OFFSET: Int = 0
        const val errorDefaultCode=-1
        const val errorAddStationCode=100
        const val errorNotBalanceCode=101
        const val errorDataNull=102
        const val errorDataEmpty=105
        const val notFountIndexExaction=103
        const val notFountItemIntoListExaction=104
        const val defaultUserID=100
        const val defaultUserBalanceCount=5

        const val FORMAT_API_END_POINT = "/api/api.php?method=%1\$s"
        const val METHOD_GET_GENRES = "getGenres"
        const val METHOD_GET_RADIOS = "getRadios"
        const val RADIOS_OFFSET = 0
        const val RADIOS_LIMIT = 10
        const val METHOD_GET_THEMES = "getThemes"
        const val METHOD_GET_REMOTE_CONFIGS = "getRemoteConfigs"

        const val METHOD_PRIVACY_POLICY = "/privacy_policy.php"
        const val METHOD_TERM_OF_USE = "/term_of_use.php"

        const val KEY_API = "&api_key="
        const val KEY_QUERY = "&q="
        const val KEY_GENRE_ID = "&genre_id="
        const val KEY_APP_TYPE = "&app_type="
        const val KEY_OFFSET = "&offset="
        const val KEY_LIMIT = "&limit="
        const val KEY_IS_FEATURE = "&is_feature=1"

        const val FOLDER_GENRES = "/uploads/genres/"
        const val FOLDER_RADIOS = "/uploads/radios/"
        const val FOLDER_THEMES = "/uploads/themes/"
    }
}