package kr.respectme.group.common.validator

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [NullOrLengthValidator::class])
annotation class NullOrLength(
    val max: Int = 255,
    val message: String = "Field must be null or less than {max} characters.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
) {

}
