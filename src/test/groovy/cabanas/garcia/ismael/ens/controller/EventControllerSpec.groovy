package cabanas.garcia.ismael.ens.controller

import cabanas.garcia.ismael.ens.ErrorCode
import cabanas.garcia.ismael.ens.LevelError
import cabanas.garcia.ismael.ens.controller.beans.EventRequestBody
import cabanas.garcia.ismael.ens.model.Event
import cabanas.garcia.ismael.ens.service.ErrorMessageService
import cabanas.garcia.ismael.ens.service.EventService
import cabanas.garcia.ismael.ens.util.ErrorPropertiesUtil
import cabanas.garcia.ismael.ens.util.UnitTestUtils
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
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

        given: "the event controller and event data"
        EventService eventService = Mock(EventService)
        EventController eventController = new EventController(eventService)
        GlobalExceptionHandler controllerAdvice = new GlobalExceptionHandler()
        EventRequestBody eventData = getAnEventRequestBody()

        when: "send a REST post request for creating an event"
        def response = sendPost(eventController, controllerAdvice, "/events", eventData)

        then: "the create method of event service is invoked"
        1 * eventService.create(_) >> {arguments -> expectedEvent = arguments[0]}

        and: "and the event of create method is the expected event"
        expectedEvent != null
        expectedEvent.name == eventData.name // short way to compare equals
        expect expectedEvent.description, is(equalTo(eventData.description)) // another way to compare equals
        expect expectedEvent.date, sameDayOfMonth(eventData.date)
        expect expectedEvent.date, sameYear(eventData.date)
    }

    def "should return 204 status code when create a new event"(){

        given: "the event controller and event data"
        EventController eventController = getEventController()
        GlobalExceptionHandler controllerAdvice = new GlobalExceptionHandler()
        EventRequestBody eventData = getAnEventRequestBody()

        when: "send a REST post request for creating an event"
        def response = sendPost(eventController, controllerAdvice, "/events", eventData)

        then: "the response status code should be " + HttpStatus.CREATED.value()
        response.status == HttpStatus.CREATED.value()
        //response.contentType == MediaType.APPLICATION_JSON_UTF8_VALUE
    }

    def "should return 400 status code when create a event with validation errors"(){
        given: "the event controller"
        EventController eventController = getEventController()

        and: "global exception handler prepared for managing validation errors"
        ErrorMessageService errorMessageService = Mock(ErrorMessageService)
        errorMessageService.getMessage(_) >> ErrorPropertiesUtil.getMessage(ErrorCode.MISSING_REQUIRED_REQUEST_BODY_PARAMETER)
        errorMessageService.getDescription(_, _) >> ErrorPropertiesUtil.getDescription(ErrorCode.MISSING_REQUIRED_REQUEST_BODY_PARAMETER)
        GlobalExceptionHandler controllerAdvice = new GlobalExceptionHandler(errorMessageService)

        when: "send a REST request for creating an event for events with validation errors"
        def response = sendPost(eventController, controllerAdvice, "/events", givenEventRequestBody)

        then: "status code is a BAD_REQUEST"
        response.status == HttpStatus.BAD_REQUEST.value()

        and: "the errors are the expected"
        matchesErrorsExpected(response, errorsExpected)

        where:
        givenEventRequestBody                        | errorsExpected
        getAnEventRequestBodyWithoutEventName()      | [[code: getErrorCodeForMissingRequiredParameter(), message: getMessageForMissingRequiredParameter(), description: getDescriptionForMissingRequiredParameter(), level: LevelError.INFO.getLevelName(), moreInfo: ""]]
        getAnEventRequestBodyWithoutEventDate()      | [[code: getErrorCodeForMissingRequiredParameter(), message: getMessageForMissingRequiredParameter(), description: getDescriptionForMissingRequiredParameter(), level: LevelError.INFO.getLevelName(), moreInfo: ""]]
        getAnEventRequestBodyWithoutRequiredParams() | [[code: getErrorCodeForMissingRequiredParameter(), message: getMessageForMissingRequiredParameter(), description: getDescriptionForMissingRequiredParameter(), level: LevelError.INFO.getLevelName(), moreInfo: ""],
                                                        [code: getErrorCodeForMissingRequiredParameter(), message: getMessageForMissingRequiredParameter(), description: getDescriptionForMissingRequiredParameter(), level: LevelError.INFO.getLevelName(), moreInfo: ""]]

    }

    def getEventController(){
        EventService eventService = Mock(EventService)
        EventController eventController = new EventController(eventService)
        return eventController
    }

    def sendPost(controller, controllerAdvice, path, content){
        MockMvc mockMvc = standaloneSetup(controller)
                .setControllerAdvice(controllerAdvice)
                .build()
        def response = mockMvc.perform(
                post(path)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJson(content))
                 )
                .andDo(log())
                .andReturn().response

        return response
    }

    def matchesErrorsExpected(response, errorsExpected) {
        def content = new JsonSlurper().parseText(response.contentAsString)
        content.errors.size == errorsExpected.size()
        content.errors.eachWithIndex { error, i ->
            assert error.code == errorsExpected[i]['code']
            assert error.message == errorsExpected[i]['message']
            assert error.description == errorsExpected[i]['description']
            assert error.level == errorsExpected[i]['level']
            assert error.moreInfo == errorsExpected[i]['moreInfo']
        }
    }

    def getAnEventRequestBodyWithoutRequiredParams() {
        EventRequestBody.builder()
                .description(TEST_EVENT_DESCRIPTION)
                .build()
    }

    def getAnEventRequestBodyWithoutEventDate() {
        EventRequestBody.builder()
                .name(TEST_EVENT_NAME)
                .description(TEST_EVENT_DESCRIPTION)
                .build()
    }

    def getDescriptionForMissingRequiredParameter() {
        ErrorPropertiesUtil.getDescription(ErrorCode.MISSING_REQUIRED_REQUEST_BODY_PARAMETER)
    }

    def getMessageForMissingRequiredParameter() {
        ErrorPropertiesUtil.getMessage(ErrorCode.MISSING_REQUIRED_REQUEST_BODY_PARAMETER)
    }

    def getErrorCodeForMissingRequiredParameter() {
        ErrorCode.MISSING_REQUIRED_REQUEST_BODY_PARAMETER
    }

    def getAnEventRequestBodyWithoutEventName() {
        EventRequestBody.builder()
                .description(TEST_EVENT_DESCRIPTION)
                .date(NOW)
                .build()
    }

    def getAnEventRequestBody() {
        EventRequestBody.builder()
                .name(TEST_EVENT_NAME)
                .description(TEST_EVENT_DESCRIPTION)
                .date(NOW)
                .build()
    }

    def toJson(Object object) {
        return JsonOutput.toJson(object)
    }
}
