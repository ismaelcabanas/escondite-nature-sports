package cabanas.garcia.ismael.ens.util

import java.text.MessageFormat

/**
 * Created by XI317311 on 30/11/2016.
 */
class ErrorPropertiesTestUtils {

    private static Properties properties = new Properties()
    private static String MESSAGE = ".message"
    private static String DESCRIPTION = ".description"

    static{
        InputStream is = getClass().getResourceAsStream(Constants.ERROR_MESSAGES_PROPERTIES)
        properties.load(is)
    }

    static def getProperty(property){
        properties.getProperty(property)
    }

    static String getMessage(String errorCode) {
        properties.getProperty("${errorCode}${MESSAGE}")
    }

    static String getDescription(String errorCode, String... params) {
        String description = properties.getProperty("${errorCode}${DESCRIPTION}")

        if(params != null)
            description = MessageFormat.format(description, params)

        return description
    }
}
