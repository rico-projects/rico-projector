package to.remove.ui;

import dev.rico.internal.projector.ForRemoval;
import dev.rico.internal.projector.ui.TextFieldModel;
import to.remove.WeightUnit;
import dev.rico.remoting.Property;

@ForRemoval
public class FuelFieldModel extends TextFieldModel {

    private Property<WeightUnit> weightUnit;

    public WeightUnit getWeightUnit() {
        return weightUnit.get();
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit.set(weightUnit);
    }

    public Property<WeightUnit> weightUnitProperty() {
        return weightUnit;
    }
}
