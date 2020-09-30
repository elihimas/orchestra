package com.elihimas.orchestra

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import java.lang.reflect.Field

enum class ParamTypes { Boolean, Enum, Float, Int, Long }

fun Field.paramType(): ParamTypes = when (type.toString()) {
    "long" -> ParamTypes.Long
    "boolean" -> ParamTypes.Boolean
    "float" -> ParamTypes.Float
    "int" -> ParamTypes.Int
    else -> ParamTypes.Enum
}

//TODO traverse through all animations tree
fun <T> Class<T>.listFields() =
        declaredFields.reversed().toMutableList().also { list ->
            superclass?.let { parentClass ->
                val parentFields = parentClass.declaredFields.reversed()
                list.addAll(parentFields)

                superclass?.superclass?.let { parentClass ->
                    val parentFields = parentClass.declaredFields.reversed()
                    list.addAll(parentFields)
                }
            }
        }.reversed().apply {
            forEach { it.isAccessible = true }
        }

fun Field.valueAsString(animation: Animation) =
        when (paramType()) {
            ParamTypes.Long -> getLong(animation).toString()
            ParamTypes.Float -> getFloat(animation).toString()
            ParamTypes.Int -> getInt(animation).toString()
            ParamTypes.Boolean -> getBoolean(animation).toString()
            else -> get(animation)?.toString() ?: "null"
        }

fun Spinner.onItemSelectedListener(callback: (Int) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
            callback.invoke(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            //nothing to do
        }

    }
}