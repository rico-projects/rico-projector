package dev.rico.projector.extension;

import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

import java.util.Optional;

@RemotingBean
public class SliderModel extends ItemModel {

    private Property<Double> min;

    private Property<Double> max;

    private Property<Double> value;

    public Property<Double> minProperty() {
        return min;
    }

    public SliderModel withMin(final double min) {
        setMin(min);
        return this;
    }

    public double getMin() {
        return Optional.ofNullable(minProperty().get()).orElse(-1d);
    }

    public void setMin(final double min) {
        minProperty().set(min);
    }

    public Property<Double> maxProperty() {
        return max;
    }

    public SliderModel withMax(final double max) {
        setMax(max);
        return this;
    }

    public double getMax() {
        return Optional.ofNullable(maxProperty().get()).orElse(-1d);
    }

    public void setMax(final double max) {
        maxProperty().set(max);
    }

    public Property<Double> valueProperty() {
        return value;
    }

    public SliderModel withValue(final double value) {
        setValue(value);
        return this;
    }

    public double getValue() {
        return Optional.ofNullable(valueProperty().get()).orElse(-1d);
    }

    public void setValue(final double value) {
        valueProperty().set(value);
    }
}
