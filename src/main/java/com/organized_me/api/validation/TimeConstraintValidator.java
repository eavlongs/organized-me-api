package com.organized_me.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Calendar;
import java.util.Date;

public class TimeConstraintValidator implements ConstraintValidator<ValidTime, Date> {
    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        Calendar nextYear = (Calendar) today.clone();
        nextYear.add(Calendar.YEAR, 1);

        Calendar minYear = Calendar.getInstance();
        minYear.set(1970, Calendar.JANUARY, 1, 0, 0, 0);

        return !value.before(minYear.getTime()) && !value.after(nextYear.getTime());
    }
}
