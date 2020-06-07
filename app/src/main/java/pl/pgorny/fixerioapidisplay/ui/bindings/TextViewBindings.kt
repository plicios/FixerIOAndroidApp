package pl.pgorny.fixerioapidisplay.ui.bindings

import android.widget.TextView
import androidx.databinding.BindingAdapter
import pl.pgorny.fixerioapidisplay.data.CurrencyMapper

@BindingAdapter("currencyName")
fun TextView.setCurrencyName(value: String){
    CurrencyMapper.names[value]?.let {
        this.text = it
    }
}