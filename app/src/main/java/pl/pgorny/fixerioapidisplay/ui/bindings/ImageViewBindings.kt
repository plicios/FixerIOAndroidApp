package pl.pgorny.fixerioapidisplay.ui.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import pl.pgorny.fixerioapidisplay.R
import pl.pgorny.fixerioapidisplay.data.CurrencyMapper

@BindingAdapter("flag")
fun ImageView.setFlagByCode(currencyCode: String){
    this.setImageResource(CurrencyMapper.flags[currencyCode] ?: R.drawable.ic_no_country)
}