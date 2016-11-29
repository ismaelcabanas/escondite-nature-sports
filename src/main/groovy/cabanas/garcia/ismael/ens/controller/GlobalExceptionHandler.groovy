package cabanas.garcia.ismael.ens.controller

import cabanas.garcia.ismael.ens.LevelError
import cabanas.garcia.ismael.ens.controller.beans.response.ErrorResponse
import cabanas.garcia.ismael.ens.controller.beans.response.ErrorsResponse
import cabanas.garcia.ismael.ens.service.ErrorMessageService
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Created by XI317311 on 28/11/2016.
 */
@ControllerAdvice
class GlobalExceptionHandler {

    private final ErrorMessageService errorMessageService

    GlobalExceptionHandler(ErrorMessageService errorMessageService) {
        this.errorMessageService = errorMessageService
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsResponse handleFields(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult()
        List<ObjectError> errors = result.getAllErrors()
        return processFieldErrors(errors)
    }

    private ErrorsResponse processFieldErrors(List<ObjectError> errors) {
        ErrorsResponse errorsResponse = new ErrorsResponse()
        for (ObjectError error: errors) {
            String localizedErrorMessage = resolveLocalizedErrorMessage(error.getDefaultMessage());
            String localizedErrorDescription;
            if(error instanceof FieldError){
                FieldError fieldError = (FieldError) error;
                localizedErrorDescription = resolveLocalizedErrorDescription(fieldError.getDefaultMessage(), fieldError.getField());
            }
            else{
                localizedErrorDescription = resolveLocalizedErrorDescription(error.getDefaultMessage());
            }
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .code(error.getDefaultMessage())
                    .message(localizedErrorMessage)
                    .description(localizedErrorDescription)
                    .level(LevelError.INFO.getLevelName())
                    .moreInfo("")
                    .build()
            errorsResponse.addError(errorResponse)
        }
        return errorsResponse
    }

    String resolveLocalizedErrorDescription(String errorCode, String field) {
        return errorMessageService.getDescription(errorCode, field);
    }

    String resolveLocalizedErrorMessage(String errorCode) {
        return errorMessageService.getMessage(errorCode);
    }
}
