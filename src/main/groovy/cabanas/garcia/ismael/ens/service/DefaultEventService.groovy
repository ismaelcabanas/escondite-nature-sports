package cabanas.garcia.ismael.ens.service

import cabanas.garcia.ismael.ens.model.Event
import cabanas.garcia.ismael.ens.repository.EventRepository

/**
 * Created by XI317311 on 24/11/2016.
 */
class DefaultEventService implements EventService{
    EventRepository eventRepository

    def DefaultEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository
    }

    @Override
    Event create(Event event) {
        Event newEvent = eventRepository.save(event)
        return newEvent
    }
}
