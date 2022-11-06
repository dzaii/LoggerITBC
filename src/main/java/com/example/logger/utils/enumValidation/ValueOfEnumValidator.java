package com.example.logger.utils.enumValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum,Integer> {
    private Integer acceptedValue;

    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValue = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList()).size();
    }
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value>=0 && value< acceptedValue;
    }
}
