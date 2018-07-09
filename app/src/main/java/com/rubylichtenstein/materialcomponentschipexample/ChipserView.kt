package com.rubylichtenstein.materialcomponentschipexample

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.chipser_layout.view.*


class ChipserView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    val chipsAdapter = ChipserAdapter()

    var items: List<String>? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.chipser_layout, this, true)

        recyclerView.adapter = chipsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        chipGroup.setOnClickListener {
            input.requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT)
        }

        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                items?.let {
                    chipsAdapter.replaceAll(filter(it, p0.toString()))
                }
            }
        })

        input.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.action != KeyEvent.ACTION_DOWN) {
                if (input.text.toString() == "") {
                    if (chipGroup.childCount > 1) {
                        chipGroup.removeViewAt(chipGroup.childCount - 2)
                    }
                }
            }

            return@setOnKeyListener false
        }

        chipsAdapter.onItemClickListener = {
            chipGroup.addView(createNewChip(it), chipGroup.childCount - 1)
            input.setText("")
            chipsAdapter.clear()
        }
    }

    private fun createNewChip(text: String): Chip {
        return Chip(this.context).apply {
            id = text.hashCode()
            this.text = text
            setCloseIconResource(R.drawable.ic_close_black_24dp)
            setOnCloseIconClickListener {
                chipGroup.removeView(this)
            }
        }
    }

    fun filter(models: List<String>, query: String): List<String> {
        val lowerCaseQuery = query.toLowerCase()

        val filteredModelList = mutableListOf<String>()
        for (model in models) {
            val text = model.toLowerCase()
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model)
            }
        }
        return filteredModelList
    }
}