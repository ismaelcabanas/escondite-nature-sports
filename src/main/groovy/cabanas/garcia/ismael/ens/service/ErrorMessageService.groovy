package cabanas.garcia.ismael.ens.service

/**
 * Created by XI317311 on 28/11/2016.
 */
interface ErrorMessageService {

    String getDescription(String errorCode, String field)

    String getMessage(String errorCode)
}