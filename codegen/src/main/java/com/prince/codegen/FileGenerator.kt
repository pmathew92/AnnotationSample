package com.prince.codegen

import com.google.auto.service.AutoService
import com.prince.annotation.GreetingGenerator
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedOptions(FileGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class FileGenerator : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(GreetingGenerator::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        p1?.getElementsAnnotatedWith(GreetingGenerator::class.java)?.forEach { classElement ->

            if (classElement.kind != ElementKind.CLASS) {
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Can only be applied to class,  element: $classElement "
                )
                return false
            }
            val className = classElement.simpleName.toString()
            val pack = processingEnv.elementUtils.getPackageOf(classElement).toString()
            val constructorValue = classElement.getAnnotation(GreetingGenerator::class.java).greeting
            generateClass(className, pack, constructorValue)
        }

        return true
    }

    private fun generateClass(className: String, pack: String, greeting: String) {
        val fileName = "${className}Greeting"

        val file = FileSpec.builder(pack, fileName)
            .addType(
                TypeSpec.classBuilder(fileName)
                    .addFunction(
                        FunSpec.builder("greeting")
                            .addStatement("return \"$greeting\"").build()
                    )
                    .build()
            )
            .build()

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]

        file.writeTo(File(kaptKotlinGeneratedDir, "$fileName.kt"))
    }


    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}