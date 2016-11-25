package cabanas.garcia.ismael.ens.model

import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * This class keep event information
 */
@Builder
@ToString
class Event {
    String name
    String description
    Date date
    long id
}
