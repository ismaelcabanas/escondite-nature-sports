package cabanas.garcia.ismael.ens.controller

import cabanas.garcia.ismael.ens.controller.beans.EventRequestBody
import cabanas.garcia.ismael.ens.model.Event
import cabanas.garcia.ismael.ens.service.EventService
import cabanas.garcia.ismael.ens.utils.UnitTestUtils
import groovy.json.JsonOutput
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static spock.util.matcher.HamcrestSupport.*
import static org.hamcrest.Matchers.*
import static org.exparity.hamcrest.date.DateMatchers.*

import spock.lang.Specification

/**
 * Created by XI317311 on 25/11/2016.
 */
class EventControllerSpec extends Specification{

    private static final String TEST_EVENT_NAME = "Event Test"
    private static final String TEST_EVENT_DESCRIPTION = "Event Test description"
    private static final Date NOW = UnitTestUtils.now()

    def "should use event service for creating a new event"(){
        Event expectedEvent

        given:
        EventService eventService = Mock(EventService)
        EventController eventController = new EventController(eventService)
        MockMvc mockMvc = standaloneSetup(eventController).build()
        EventRequestBody eventData = getAnEventRequestBody()

        when:
        def response = mockMvc.perform(
                post("/events")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .content(toJson(eventData))
                ).andDo(log())

        then:
        1 * eventService.create(_) >> {arguments -> expectedEvent = arguments[0]}
        expectedEvent != null
        expectedEvent.name == eventData.name // short way to compare equals
        expect expectedEvent.description, is(equalTo(eventData.description)) // another way to compare equals
        expect expectedEvent.date, sameDayOfMonth(eventData.date)
        expect expectedEvent.date, sameYear(eventData.date)
    }

    def "should return 204 status code when create a new event"(){
        given:
        EventService eventService = Mock(EventService)
        EventController eventController = new EventController(eventService)
        MockMvc mockMvc = standaloneSetup(eventController).build()
        EventRequestBody eventData = getAnEventRequestBody()

        when:
        def response = mockMvc.perform(
                post("/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJson(eventData))
                )
                .andDo(log())
                .andReturn().response

        then:
        response.status == HttpStatus.CREATED.value()
        //response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
    }

    def "should return 400 status code when create a event but name is not sended"(){
        given:
        EventService eventService = Mock(EventService)
        EventController eventController = new EventController(eventService)
        MockMvc mockMvc = standaloneSetup(eventController).build()
        EventRequestBody eventData = getAnEventRequestBodyWithoutName()

        when:
        def response = mockMvc.perform(
                post("/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJson(eventData))
        )
                .andDo(log())
                .andReturn().response

        then:
        response.status == HttpStatus.BAD_REQUEST.value()
    }

    EventRequestBody getAnEventRequestBodyWithoutName() {
        EventRequestBody.builder()
                .description(TEST_EVENT_DESCRIPTION)
                .date(NOW)
                .build()
    }

    private EventRequestBody getAnEventRequestBody() {
        EventRequestBody.builder()
                .name(TEST_EVENT_NAME)
                .description(TEST_EVENT_DESCRIPTION)
                .date(NOW)
                .build()
    }

    private String toJson(Object object) {
        return JsonOutput.toJson(object)
    }
}
