package com.github.teocci.taskrevolut.Views;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.teocci.taskrevolut.R;
import com.github.teocci.taskrevolut.interfaces.OnItemEventListener;
import com.github.teocci.taskrevolut.models.CurrencyRate;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public class CurrencyRateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView tvCurrencyId;
    public TextView tvCurrencyName;
    public EditText etCurrencyValue;

    public CurrencyRateViewHolder(final View itemView)
    {
        super(itemView);
        this.tvCurrencyId = (TextView) itemView.findViewById(R.id.currency_id);
        this.tvCurrencyName = (TextView) itemView.findViewById(R.id.currency_name);
        this.etCurrencyValue = (EditText) itemView.findViewById(R.id.currency_value);
        // Setup the click listener
//        itemView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                // Triggers click upwards to the adapter on click
//                if (listener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        listener.onItemClick(itemView, position);
//                    }
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v)
    {

    }

    public void bindView(CurrencyRate currencyRate, OnItemEventListener onItemEventListener, EditTextWatcher textWatcher)
    {
        tvCurrencyId.setText(currencyRate.currencyId);
        tvCurrencyName.setText(currencyRate.currencyName);
        etCurrencyValue.setText(Double.toString(currencyRate.value));

//        initCurrencyValue(currencyRate, onItemClickListener, textWatcher)
//        mLayout !!.setOnClickListener {
//        v ->
//                v.isFocusableInTouchMode = true
//        v.requestFocus()
//        v.isFocusableInTouchMode = false
//        onItemClickListener.onItemClick(currencyRate.currencyId);
//       }
    }
}
