package com.kmworks.appbase.command

sealed class Command {
    data class PlayStation(val stationId: Int) : ViewCommand
    data class StopStation(val stationId: Int) : ViewCommand
    data class NextStation(val stationId: Int) : ViewCommand
    data class PreviousStation(val stationId: Int) : ViewCommand
}