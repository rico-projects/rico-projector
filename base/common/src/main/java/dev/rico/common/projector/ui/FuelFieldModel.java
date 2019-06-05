package dev.rico.common.projector.ui;

import dev.rico.common.projector.mixed.WeightUnit;
import dev.rico.remoting.Property;

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
