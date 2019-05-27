package com.prince.annotationsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.prince.annotation.GreetingGenerator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    @GreetingGenerator
    class Santa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = generatedText
        textView.text = SantaGreeting().greeting()

    }
}
