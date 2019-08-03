package com.github.teocci.taskrevolut.Views;

import android.text.Editable;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.github.teocci.taskrevolut.R;
import com.github.teocci.taskrevolut.interfaces.OnItemEventListener;
import com.github.teocci.taskrevolut.models.CurrencyRate;
import com.github.teocci.taskrevolut.utils.LogHelper;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public class CurrencyRateViewHolder extends RecyclerView.ViewHolder
{
    private final static String TAG = LogHelper.makeLogTag(CurrencyRateViewHolder.class);

    private View itemView;

    private EditTextWatcher editTextWatcher;
    private OnItemEventListener onItemEventListener;

    private CircularImageView currencyFlag;
    private TextView currencyId;
    private TextView currencyName;
    private EditText currencyValue;

    public CurrencyRateViewHolder(final View itemView)
    {
        super(itemView);

        this.itemView = itemView;

        this.currencyFlag = (CircularImageView) itemView.findViewById(R.id.currency_flag);
        this.currencyId = (TextView) itemView.findViewById(R.id.currency_id);
        this.currencyName = (TextView) itemView.findViewById(R.id.currency_name);
        this.currencyValue = (EditText) itemView.findViewById(R.id.currency_value);


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

    public void bindView(CurrencyRate currencyRate, OnItemEventListener onItemEventListener, EditTextWatcher editTextWatcher)
    {
        if (currencyValue == null) return;
        if (currencyRate.currencyId == null) return;
        if (itemView == null) return;

        this.currencyFlag.setImageDrawable(currencyRate.currencyFlag);
        this.onItemEventListener = onItemEventListener;
        this.editTextWatcher = editTextWatcher;

        currencyId.setText(currencyRate.currencyId);
        currencyName.setText(currencyRate.currencyName);
//        currencyValue.setText(Double.toString(currencyRate.value));

        initCurrencyValue(currencyRate, onItemEventListener, editTextWatcher);

        itemView.setOnClickListener(v -> {
            v.setFocusableInTouchMode(true);
            v.requestFocus();
            v.setFocusableInTouchMode(false);
            onItemEventListener.onItemClick(currencyRate.currencyId);
        });
    }

    private void initCurrencyValue(CurrencyRate currencyRate, OnItemEventListener onItemEventListener, EditTextWatcher editTextWatcher)
    {
        if (currencyValue == null) return;

        currencyValue.removeTextChangedListener(editTextWatcher);

        String value = String.format("%." + 2 + "f", currencyRate.value);

        if (!currencyValue.getText().toString().equals(value)) {
            currencyValue.setText(Editable.Factory.getInstance().newEditable(value));
        }
        currencyValue.addTextChangedListener(editTextWatcher);
        currencyValue.setTag(currencyRate.currencyId);
        currencyValue.setOnFocusChangeListener((v, hasFocus) -> onItemEventListener.onFocusChange(hasFocus, v));
        currencyValue.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                currencyValue.clearFocus();
                return true;
            }

            return false;
        });

//        try {
//            currencyValue.setSelection(currencyValue.getText().length());
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
    }
}
