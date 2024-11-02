package eu.wedgess.mihole.ui.base

interface EventDrivenViewModel<Event> {
    fun onEvent(event: Event)
}