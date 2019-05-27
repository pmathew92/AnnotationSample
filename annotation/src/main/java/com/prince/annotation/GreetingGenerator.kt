package com.prince.annotation


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class GreetingGenerator(val greeting: String = "Merry Christmas!!")