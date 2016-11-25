package cabanas.garcia.ismael.ens.controller.beans

import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 25/11/2016.
 */
@Builder
@ToString
class EventRequestBody {
    String name
    String description
    Date date
}
