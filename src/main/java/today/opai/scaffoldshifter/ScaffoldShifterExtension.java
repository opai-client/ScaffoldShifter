package today.opai.scaffoldshifter;

import today.opai.api.Extension;
import today.opai.api.OpenAPI;
import today.opai.api.annotations.ExtensionInfo;
import today.opai.scaffoldshifter.modules.ScaffoldShifter;

@ExtensionInfo(name = "ScaffoldShifter", author = "NikoCat233", version = "1.0.0")
public class ScaffoldShifterExtension extends Extension {
    public static OpenAPI openAPI;
    private ScaffoldShifter scaffoldShifter;

    @Override
    public void initialize(OpenAPI openAPI) {
        ScaffoldShifterExtension.openAPI = openAPI;
        scaffoldShifter = new ScaffoldShifter();
        openAPI.registerFeature(scaffoldShifter);
    }

    @Override
    public void onUnload() {
        if (scaffoldShifter != null) {
            scaffoldShifter.releaseSneak();
        }
    }
}
