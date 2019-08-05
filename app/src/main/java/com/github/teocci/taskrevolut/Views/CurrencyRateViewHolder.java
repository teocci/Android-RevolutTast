package com.github.teocci.taskrevolut.Views;

import android.text.Editable;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.github.teocci.taskrevolut.R;
import com.github.teocci.taskrevolut.interfaces.OnItemEventListener;
import com.github.teocci.taskrevolut.models.CurrencyRate;
import com.github.teocci.taskrevolut.utils.LogHelper;

import androidx.recyclerview.widget.RecyclerView;

/**
 * This Class provides a reference to the views for each data item.
 * <p>
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public class CurrencyRateViewHolder extends RecyclerView.ViewHolder
{
    private final static String TAG = LogHelper.makeLogTag(CurrencyRateViewHolder.class);

    private View itemView;


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
    }

    /**
     * Replace the contents of a view.
     *
     * @param rateUpdate New data to update the view.
     * @param onItemEventListener onClick and onFocus event listener
     * @param editTextWatcher editable element listener
     */
    public void bindView(CurrencyRate rateUpdate, OnItemEventListener onItemEventListener, EditTextWatcher editTextWatcher)
    {
        if (currencyValue == null) return;
        if (rateUpdate.currencyId == null) return;
        if (itemView == null) return;

        this.currencyFlag.setImageDrawable(rateUpdate.currencyFlag);

        currencyId.setText(rateUpdate.currencyId);
        currencyName.setText(rateUpdate.currencyName);

        initCurrencyValue(rateUpdate, onItemEventListener, editTextWatcher);

        itemView.setOnClickListener(v -> {
            v.setFocusableInTouchMode(true);
            v.requestFocus();
            v.setFocusableInTouchMode(false);
            onItemEventListener.onItemClick(rateUpdate.currencyId);
        });
    }

    /**
     * Update the editable element of the view.
     *
     * @param currencyRate New data to update the view.
     * @param onItemEventListener onClick and onFocus event listener
     * @param editTextWatcher editable element listener
     */
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
    }
}
