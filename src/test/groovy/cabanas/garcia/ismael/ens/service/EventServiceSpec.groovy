package cabanas.garcia.ismael.ens.service

import cabanas.garcia.ismael.ens.utils.UnitTestUtils

import java.time.Instant

import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.*

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
        def anEvent = getDummyEvent()

        when:
        eventService.create(anEvent)

        then:
        1 * eventRepository.save(anEvent)
    }

    def "should return the persisted event"(){
        given:
        EventRepository eventRepository = Mock(EventRepository)
        eventRepository.save(_) >> UnitTestUtils.createTestEvent(expected)
        EventService eventService = new DefaultEventService(eventRepository)
        def anEvent = UnitTestUtils.createTestEvent(event)

        when:
        def actualEvent = eventService.create(anEvent)

        then:
        that actualEvent, is(notNullValue())
        that actualEvent.name, is(equalTo(expected.name))
        that actualEvent.description, is(equalTo(expected.description))
        that actualEvent.date, is(notNullValue())
        that actualEvent.id, is(notNullValue())

        where:
           event                                                                  |   expected
           [name: "EventA", description: "EventA description", date: dummyDate()] |   [name: "EventA", description: "EventA description", date: dummyDate(), id: 1]
           [name: "EventB", description: "EventB description", date: dummyDate()] |   [name: "EventB", description: "EventB description", date: dummyDate(), id: 2]
    }

    private Date dummyDate() {
        return Date.from(Instant.now())
    }

    private Event getDummyEvent() {
        Event
            .builder()
            .name("Test Event")
            .description("Event for testing")
            .date(dummyDate())
            .build()
    }
}
