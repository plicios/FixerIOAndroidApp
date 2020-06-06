package pl.pgorny.fixerioapidisplay.util

import pl.pgorny.fixerioapidisplay.data.model.RateRow

abstract class Event

class ShowRateDetailsEvent(val rate: RateRow) : Event()
class ShowApiErrorEvent(val errorText: String) : Event()
class LoadingNextPage : Event()
class FinishedLoadingNextPage : Event()