package pl.pgorny.fixerioapidisplay.util

import pl.pgorny.fixerioapidisplay.data.model.Rate

abstract class Event

class ShowRateDetails(val rate: Rate) : Event()
class ShowError(val errorText: String) : Event()
class Loading : Event()
class FinishedLoading : Event()