package cabanas.garcia.ismael.ens.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * This class keep event information
 */
@Builder
@ToString
@EqualsAndHashCode
class Event {
    String name
    String description
    Date date
    long id
}
