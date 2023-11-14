package com.solvd.laba.homework02.exercise01.UI;

public class IntegerInRangeValidator implements IValidator<Integer> {
    private final Integer minimum;
    private final Integer maximum;

    public IntegerInRangeValidator(Integer minimum, Integer maximum) {
        if (minimum == null && maximum == null) {
            throw new IllegalArgumentException("both minimum and maximum cannot be null");
        }
        if ((minimum != null && maximum != null) && minimum > maximum) {
            throw new IllegalArgumentException("minimum cannot be greater than maximum");
        }
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public boolean isValid(Integer obj) {
        if (this.minimum != null && this.maximum != null) {
            return this.minimum <= obj && obj <= this.maximum;
        }
        if (this.minimum != null && this.maximum == null) {
            return this.minimum <= obj;
        }
        if (this.minimum == null && this.maximum != null) {
            return obj <= this.maximum;
        }
        return false;
    }
}
