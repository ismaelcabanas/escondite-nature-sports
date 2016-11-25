package cabanas.garcia.ismael.ens.service

import cabanas.garcia.ismael.ens.utils.UnitTestUtils

import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.*

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
        def anEvent = UnitTestUtils.getDummyEvent()

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
        expect actualEvent, is(notNullValue())
        expect actualEvent.name, is(equalTo(expected.name))
        expect actualEvent.description, is(equalTo(expected.description))

        expect:
        that actualEvent.date, is(notNullValue())
        that actualEvent.id, is(notNullValue())

        where:
           event                                                                          |   expected
           [name: "EventA", description: "EventA description", date: UnitTestUtils.now()] |   [name: "EventA", description: "EventA description", date: UnitTestUtils.now(), id: 1]
           [name: "EventB", description: "EventB description", date: UnitTestUtils.now()] |   [name: "EventB", description: "EventB description", date: UnitTestUtils.now(), id: 2]
    }


}
