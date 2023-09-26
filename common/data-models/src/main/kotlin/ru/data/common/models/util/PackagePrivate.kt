package ru.data.common.models.util

/**
 * @RequiresOptIn(message = "Only to be used in MyPackage")
 * Simple
 * @OptIn(MyPackagePrivate::class)
 * class AClassInThePackage {
 *     fun userOfPackagePrivateMethod() {
 *         aPackagePrivateMethod()
 *     }
 * }
 *
 * @MyPackagePrivate
 * fun aPackagePrivateMethod() {
 *    // do something private within a package
 * }
 * @RequiresOptIn(level = RequiresOptIn.Level.ERROR, message = "Only to be used in MyPackage")
 * Если требуется сгенерировать ошибку, а не предупреждение, можно указать level параметр @RequiresOptIn
 * */

@RequiresOptIn(level = RequiresOptIn.Level.ERROR, message = "Only to be used in Package")
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.CLASS
)
@MustBeDocumented
annotation class PackagePrivate




