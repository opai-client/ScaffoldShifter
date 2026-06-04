package today.opai.scaffoldshifter.modules;

import today.opai.api.enums.EnumKeybind;
import today.opai.api.enums.EnumModuleCategory;
import today.opai.api.features.ExtensionModule;
import today.opai.api.interfaces.EventHandler;
import today.opai.api.interfaces.dataset.Vector3d;
import today.opai.api.interfaces.modules.PresetModule;
import today.opai.api.interfaces.modules.values.BooleanValue;
import today.opai.api.interfaces.modules.values.NumberValue;

import static today.opai.scaffoldshifter.ScaffoldShifterExtension.openAPI;

public class ScaffoldShifter extends ExtensionModule implements EventHandler {
    private final NumberValue blocks;
    private final NumberValue sneakTicks;
    private final BooleanValue airOnly;

    private int lastBlockX;
    private int lastBlockZ;
    private int movedBlocks;
    private int sneakingTicksLeft;
    private boolean hasLastBlock;
    private boolean pressingSneak;
    private boolean previousSneakPressed;

    public ScaffoldShifter() {
        super("ScaffoldShifter", "Sneak for configured ticks every few scaffold blocks.", EnumModuleCategory.MISC);
        setEventHandler(this);

        blocks = openAPI.getValueManager().createDouble("Blocks", 4.0, 1.0, 16.0, 1.0);
        sneakTicks = openAPI.getValueManager().createDouble("Sneak Ticks", 3.0, 1.0, 20.0, 1.0);
        airOnly = openAPI.getValueManager().createBoolean("AirOnly", false);
        addValues(blocks, sneakTicks, airOnly);
    }

    @Override
    public void onEnabled() {
        resetState();
    }

    @Override
    public void onDisabled() {
        releaseSneak();
        resetState();
    }

    @Override
    public void onTick() {
        if (openAPI.isNull() || !isScaffoldEnabled()) {
            releaseSneak();
            resetState();
            return;
        }

        int interval = blocks.getValue().intValue();
        updateMovedBlocks(interval);

        if (sneakingTicksLeft > 0 && shouldSneak()) {
            setSneak(true);
            sneakingTicksLeft--;
        } else {
            setSneak(false);
        }
    }

    @Override
    public void onModuleToggle(PresetModule module, boolean state) {
        if ("Scaffold".equalsIgnoreCase(module.getName()) && !state) {
            releaseSneak();
            resetState();
        }
    }

    public void releaseSneak() {
        setSneak(false);
    }

    private void updateMovedBlocks(int interval) {
        Vector3d position = openAPI.getLocalPlayer().getPosition();
        int blockX = floor(position.getX());
        int blockZ = floor(position.getZ());

        if (!hasLastBlock) {
            lastBlockX = blockX;
            lastBlockZ = blockZ;
            hasLastBlock = true;
            return;
        }

        int delta = Math.abs(blockX - lastBlockX) + Math.abs(blockZ - lastBlockZ);
        if (delta <= 0) {
            return;
        }

        movedBlocks += delta;
        lastBlockX = blockX;
        lastBlockZ = blockZ;

        if (movedBlocks >= interval) {
            movedBlocks %= interval;
            sneakingTicksLeft = sneakTicks.getValue().intValue();
        }
    }

    private boolean isScaffoldEnabled() {
        PresetModule module = openAPI.getModuleManager().getModule("Scaffold");
        return module != null && module.isEnabled();
    }

    private boolean shouldSneak() {
        return !airOnly.getValue() || !openAPI.getLocalPlayer().isOnGround();
    }

    private void setSneak(boolean pressed) {
        if (pressingSneak == pressed) {
            return;
        }

        if (pressed) {
            previousSneakPressed = openAPI.getOptions().isPressed(EnumKeybind.SNEAK);
            openAPI.getOptions().setPressed(EnumKeybind.SNEAK, true);
        } else {
            openAPI.getOptions().setPressed(EnumKeybind.SNEAK, previousSneakPressed);
            previousSneakPressed = false;
        }

        pressingSneak = pressed;
    }

    private void resetState() {
        hasLastBlock = false;
        movedBlocks = 0;
        sneakingTicksLeft = 0;
    }

    private int floor(double value) {
        int integer = (int) value;
        return value < integer ? integer - 1 : integer;
    }
}
