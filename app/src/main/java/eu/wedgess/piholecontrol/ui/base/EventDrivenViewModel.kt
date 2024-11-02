package eu.wedgess.piholecontrol.ui.base

interface EventDrivenViewModel<Event> {
    fun onEvent(event: Event)
}