package cabanas.garcia.ismael.ens.controller

import cabanas.garcia.ismael.ens.controller.beans.EventRequestBody
import cabanas.garcia.ismael.ens.model.Event
import cabanas.garcia.ismael.ens.service.EventService
import org.springframework.beans.BeanUtils
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

/**
 * Created by XI317311 on 25/11/2016.
 */
@RestController
@RequestMapping(value = "/events", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
class EventController {
    private EventService eventService

    EventController(EventService eventService) {
        this.eventService = eventService
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody @Valid EventRequestBody eventRequestBody){
        Event theEvent = new Event()

        copyProperties(eventRequestBody, theEvent)

        eventService.create(theEvent)

        return new ResponseEntity<>(HttpStatus.CREATED)
    }

    def copyProperties(def source, def target){
        target.metaClass.properties.each{
            if (source.metaClass.hasProperty(source, it.name) && it.name != 'metaClass' && it.name != 'class')
                it.setProperty(target, source.metaClass.getProperty(source, it.name))
        }
    }
}
