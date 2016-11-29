package cabanas.garcia.ismael.ens

/**
 * Created by XI317311 on 28/11/2016.
 */
enum LevelError {
    INFO("info"),
    WARNING("warning"),
    ERROR("error")

    String levelName

    private LevelError(String levelName){
        this.levelName = levelName
    }

    String getLevelName() {
        levelName
    }
}