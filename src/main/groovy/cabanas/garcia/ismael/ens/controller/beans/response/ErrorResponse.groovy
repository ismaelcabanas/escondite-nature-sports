package cabanas.garcia.ismael.ens.controller.beans.response

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 * Created by XI317311 on 28/11/2016.
 */
@Builder
@ToString
@EqualsAndHashCode
class ErrorResponse {
    String code
    String message
    String description
    String level
    String moreInfo
}
