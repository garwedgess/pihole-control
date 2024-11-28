package eu.wedgess.piholecontrol.presentation.base

interface EventDrivenViewModel<Event> {
    fun onEvent(event: Event)
}