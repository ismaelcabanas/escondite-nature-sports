package cabanas.garcia.ismael.ens.service

import static org.hamcrest.Matchers.*

import cabanas.garcia.ismael.ens.model.Event
import cabanas.garcia.ismael.ens.repository.EventRepository
import spock.lang.Specification

/**
 * Unit tests for Event service.
 *
 * Created by XI317311 on 24/11/2016.
 */
class EventServiceSpec extends Specification{

    def "should persist an event in repository"(){
        given:
        EventRepository eventRepository = Mock(EventRepository)
        EventService eventService = new DefaultEventService(eventRepository)
        def anEvent = Event
                .builder()
                .name("Test Event")
                .description("Event for testing")
                .date(Date.from(Calendar.getInstance().toInstant()))
                .build()

        when:
        eventService.create(anEvent)

        then:
        1 * eventRepository.save(anEvent)
    }
}
