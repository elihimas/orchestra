package com.elihimas.orchestra.ui.animateimage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.elihimas.orchestra.*
import kotlinx.android.synthetic.main.field_row.view.*
import java.lang.IllegalStateException
import java.lang.reflect.Field

class FieldsAdapter(private val animation: Animation) : RecyclerView.Adapter<FieldsAdapter.FieldHolder>() {

    class FieldHolder(view: View,
                      val nameText: TextView,
                      val valueEditText: EditText,
                      val valueSpinner: Spinner,
                      val valueCheck: CheckBox) : RecyclerView.ViewHolder(view)

    private val fields = mutableListOf<Field>()
    private val animationClone = animation.clone()

    init {
        val declaredFields = animation.javaClass.listFields()

        fields.addAll(declaredFields)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.field_row, parent, false).let { view ->
                FieldHolder(view, view.nameText, view.valueEditText, view.valueSpinner, view.valueCheck).also { holder ->
                    addEditTextWatcher(holder.valueEditText, view)
                    addSpinnerWatcher(holder.valueSpinner, view)
                    addCheckWatcher(holder.valueCheck, view)
                }

            }

    private fun addEditTextWatcher(editText: EditText, view: View) {
        editText.doAfterTextChanged { newValueText ->
            val field = view.tag as Field
            val paramType = field.paramType()

            when (paramType) {
                ParamTypes.Float -> field.setFloat(animationClone, newValueText.toString().toFloat())
                ParamTypes.Int -> field.setInt(animationClone, newValueText.toString().toInt())
                ParamTypes.Long -> field.setLong(animationClone, newValueText.toString().toLong())
                else -> throw IllegalStateException("not implemented for $paramType")
            }
        }
    }

    private fun addSpinnerWatcher(valueSpinner: Spinner, view: View) {
        valueSpinner.onItemSelectedListener { position ->
            val field = view.tag as Field
            val values = field.type.enumConstants?.toList()
            values?.let { values ->
                val selectedValue = values[position]

                field.set(animationClone, selectedValue)
            }
        }
    }

    private fun addCheckWatcher(valueCheck: CheckBox, view: View) {
        valueCheck.setOnCheckedChangeListener { _, checked ->
            val field = view.tag as Field
            field.setBoolean(animationClone, checked)
        }
    }

    override fun onBindViewHolder(holder: FieldHolder, position: Int) {
        val field = fields[position]

        holder.itemView.tag = field
        holder.nameText.text = field.name

        holder.valueEditText.visibility = View.GONE
        holder.valueSpinner.visibility = View.GONE
        holder.valueCheck.visibility = View.GONE

        when (field.paramType()) {
            ParamTypes.Boolean -> {
                holder.valueCheck.visibility = View.VISIBLE
                holder.valueCheck.isChecked = field.getBoolean(animation)
            }
            ParamTypes.Float -> {
                holder.valueEditText.visibility = View.VISIBLE
                holder.valueEditText.setText(field.getFloat(animation).toString())
            }
            ParamTypes.Int -> {
                holder.valueEditText.visibility = View.VISIBLE
                holder.valueEditText.setText(field.getInt(animation).toString())
            }
            ParamTypes.Long -> {
                holder.valueEditText.visibility = View.VISIBLE
                holder.valueEditText.setText(field.getLong(animation).toString())
            }
            ParamTypes.Enum -> {
                val values = field.type.enumConstants?.toList()
                values?.let { values ->
                    val selectedEnum = field.get(animation)
                    val selectedEnumIndex = values.indexOf(selectedEnum)

                    values.let {
                        holder.valueSpinner.adapter =
                                ArrayAdapter(holder.itemView.context, android.R.layout.simple_list_item_1, it)
                        holder.valueSpinner.setSelection(selectedEnumIndex)
                    }
                    holder.valueSpinner.visibility = View.VISIBLE
                }
            }
            else -> throw IllegalStateException("not implemented for ${field.paramType()}")
        }
    }

    override fun getItemCount(): Int = fields.size

    fun update() {
        fields.forEach { field ->
            when (field.paramType()) {
                ParamTypes.Boolean -> {
                    field.getBoolean(animationClone).let {
                        field.setBoolean(animation, it)
                    }
                }
                ParamTypes.Enum -> {
                    field.get(animationClone).let {
                        field.set(animation, it)
                    }
                }
                ParamTypes.Float -> {
                    field.getFloat(animationClone).let {
                        field.setFloat(animation, it)
                    }
                }
                ParamTypes.Int -> {
                    field.getInt(animationClone).let {
                        field.setInt(animation, it)
                    }
                }
                ParamTypes.Long -> {
                    field.getLong(animationClone).let {
                        field.setLong(animation, it)
                    }
                }
            }
        }
    }
}