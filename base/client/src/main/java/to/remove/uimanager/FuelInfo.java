package to.remove.uimanager;

import dev.rico.internal.projector.ForRemoval;
import to.remove.WeightUnit;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ForRemoval
public class FuelInfo {
    private final SpecialValues specialValue;

    public Integer getValue() {
        return value;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    private final Integer value;
    private final WeightUnit unit;

    public SpecialValues getSpecialValue() {
        return specialValue;
    }

    private FuelInfo(Integer value, WeightUnit unit) {
        specialValue = null;
        this.value = value;
        this.unit = unit;
    }

    private FuelInfo(SpecialValues specialValue) {
        this.specialValue = specialValue;
        this.value = null;
        this.unit = null;
    }

    /**
     * Erzeugt aus einem String nach dem Muster 'Wert Einheit'
     * ein FuelInfo-Objekt. Dabei gilt: Wert = Integer, Einheit = lbs oder liter (liter).
     * Der Wert darf 0 sein, in diesem Fall ist keine Einheit notwendig. Ein leerer
     * String wird als 0 interpretiert. Als gültig werden auch Varianten ohne Leerzeichen
     * zwischen Wert und Einheit interpretiert.
     * <p>
     * Der String kann als Spezialwert MINIMUM, MAXIMUM, REMAINING enthalten.
     * Die Klasse ist immutable.
     *
     * @throws IllegalArgumentException falls der String kein FuelInfo-Objekt beschreibt
     */
    public static FuelInfo from(String fuelInfoString, WeightUnit expectedUnit) {
        Objects.requireNonNull(fuelInfoString);
        fuelInfoString = fuelInfoString.trim();
        fuelInfoString = fuelInfoString.toUpperCase();
        fuelInfoString = fuelInfoString.replaceAll("\\s+", "");
        try {
            return new FuelInfo(SpecialValues.valueOf(fuelInfoString));
        } catch (IllegalArgumentException e) {
            if (fuelInfoString.isEmpty()) {
                return new FuelInfo(0, expectedUnit);
            }
            if (expectedUnit != null) {
                return fromInternal(fuelInfoString, expectedUnit);
            } else {
                throw new IllegalArgumentException("expectedUnit must not be null");
            }
        }
    }

    private static FuelInfo fromInternal(String fuelInfoString, WeightUnit expectedUnit) {
        String expectedUnitString;
        if (expectedUnit == WeightUnit.IMPERIAL) {
            expectedUnitString = "LBS";
        } else if (expectedUnit == WeightUnit.METRIC) {
            expectedUnitString = "LITER";
        } else {
            throw new IllegalArgumentException("Unknown unit: " + expectedUnit);
        }

        String valuePart = null;
        String unitPart = null;

        Matcher matcher = Pattern.compile("^(\\d+)$").matcher(fuelInfoString);
        if (matcher.matches()) {
            valuePart = matcher.group(1);
            unitPart = expectedUnitString;
        } else {
            matcher = Pattern.compile("^(\\d+)(" + expectedUnitString + ")$").matcher(fuelInfoString);
            if (matcher.matches()) {
                valuePart = matcher.group(1);
                unitPart = matcher.group(2);
            }
        }
        if (valuePart == null && unitPart == null) {
            throw new IllegalArgumentException("Expected a value with unit: " + expectedUnitString);
        }
        if (valuePart == null) {
            throw new IllegalArgumentException("Missing value part");
        }
        int value;
        WeightUnit unit = expectedUnit;
        try {
            value = Integer.parseInt(valuePart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Value part is not a number: " + valuePart, e);
        }
        if (value != 0) {
            if (!expectedUnitString.equalsIgnoreCase(unitPart)) {
                throw new IllegalArgumentException("Missing unit (value is not 0)");
            } else {
                switch (unitPart) {
                    case "LITER":
                        unit = WeightUnit.METRIC;
                        break;
                    case "LBS":
                        unit = WeightUnit.IMPERIAL;
                        break;
                    default:
                        throw new IllegalArgumentException("Found unknown unit: " + unitPart);
                }
            }
        }
        return new FuelInfo(value, unit);
    }

    public String toString() {
        String result = "";
        if (isMaximum() || isMinimum() || isRemaining()) {
            result = getSpecialValue().toString();
        } else if (value != null && value != 0) {
            result += value;
            if (unit != null) {
                switch (unit) {
                    case IMPERIAL:
                        result += " LBS";
                        break;
                    case METRIC:
                        result += " LITER";
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown unit: " + unit);
                }
            } else {
                throw new IllegalArgumentException("Unit is null");
            }

        }
        return result;
    }

    public boolean isValue() {
        return specialValue == null && value != null && unit != null;
    }

    public boolean isMinimum() {
        return specialValue == SpecialValues.MINIMUM && value == null && unit == null;
    }

    public boolean isMaximum() {
        return specialValue == SpecialValues.MAXIMUM && value == null && unit == null;
    }

    public boolean isRemaining() {
        return specialValue == SpecialValues.REMAINING && value == null && unit == null;
    }

    public enum SpecialValues {MINIMUM, MAXIMUM, REMAINING}
}
