package kr.respectme.group.common.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class NullOrLengthValidator: ConstraintValidator<NullOrLength, String> {

    private var min = 1
    private var max = 255

    override fun initialize(constraintAnnotation: NullOrLength?) {
        min = 1
        max = constraintAnnotation?.max ?: 255
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if(value == null) return true

        if(value.isEmpty()) {
            return false;
        }

        return value.length in min..max
    }
}