package dev.rico.internal.client.projector.uimanager;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;

import java.util.List;

public class PropertySheetBehavior extends BehaviorBase<PropertySheet> {
    /**
     * Create a new BehaviorBase for the given control. The Control must not
     * be null.
     *
     * @param control     The control. Must not be null.
     * @param keyBindings The key bindings that should be used with this behavior.
     */
    public PropertySheetBehavior(PropertySheet control, List<KeyBinding> keyBindings) {
        super(control, keyBindings);
    }
}
