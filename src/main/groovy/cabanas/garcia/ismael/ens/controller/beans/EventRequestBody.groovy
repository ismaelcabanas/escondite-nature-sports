package cabanas.garcia.ismael.ens.controller.beans

import cabanas.garcia.ismael.ens.ErrorCode
import groovy.transform.ToString
import groovy.transform.builder.Builder
import org.hibernate.validator.constraints.NotEmpty

import javax.validation.constraints.NotNull

/**
 * Created by XI317311 on 25/11/2016.
 */
@Builder
@ToString
class EventRequestBody {

    @NotEmpty(message = ErrorCode.MISSING_REQUIRED_REQUEST_BODY_PARAMETER)
    String name

    String description

    @NotNull(message = ErrorCode.MISSING_REQUIRED_REQUEST_BODY_PARAMETER)
    Date date
}
