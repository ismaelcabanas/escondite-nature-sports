package cabanas.garcia.ismael.ens.controller.beans

import groovy.transform.ToString
import groovy.transform.builder.Builder
import org.hibernate.validator.constraints.NotEmpty

/**
 * Created by XI317311 on 25/11/2016.
 */
@Builder
@ToString
class EventRequestBody {

    @NotEmpty
    String name

    String description
    
    Date date
}
