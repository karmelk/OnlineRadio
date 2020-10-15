package com.kmworks.appbase

class Constants {
    companion object{
        const val BASE_URL: String = "http://api.shoutcast.com"
        const val API_KEY: String = "HLLYSYyBlCi5mXEU"
        const val DATA_FORMAT: String = "json"
        const val STATION_FORMAT: String = "audio/mpeg"
        const val defaultUserID=100
        const val defaultUserBalanceCount=5
        const val LIMIT: Int = 100
        const val errorDefaultCode=-1
        const val errorAddStationCode=100
        const val errorNotBalanceCode=101
        const val errorDataNull=102
    }
}