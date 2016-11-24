package cabanas.garcia.ismael.ens.model

import groovy.transform.builder.Builder

/**
 * This class keep event information
 */
@Builder
class Event {
    String name
    String description
    Date date
    long id
}
