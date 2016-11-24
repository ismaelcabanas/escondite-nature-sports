package cabanas.garcia.ismael.ens.repository

import cabanas.garcia.ismael.ens.model.Event

/**
 * Persistent repository for events
 */
interface EventRepository {

    Event save(Event event)
}