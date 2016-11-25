package cabanas.garcia.ismael.ens.utils

import cabanas.garcia.ismael.ens.model.Event

/**
 * Created by XI317311 on 24/11/2016.
 */
final class UnitTestUtils {
    static Event createTestEvent(Map overrides = [:]) {
        def event = Event
                .builder()
                .name("My Event")
                .description("My description")
                .date(Date.from(Calendar.getInstance().toInstant()))
                .build()

        overrides.each { String key, value ->
            if(event.hasProperty(key)){
                event.setProperty(key, value)
            } else {
                println "Error: Trying to add property that doesn't exist"
            }
        }

        return event
    }
}
