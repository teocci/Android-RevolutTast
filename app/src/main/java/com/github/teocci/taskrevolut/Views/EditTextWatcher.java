package com.github.teocci.taskrevolut.Views;

import android.text.Editable;
import android.text.TextWatcher;
import com.github.teocci.taskrevolut.net.CurrencyUpdater;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public class EditTextWatcher implements TextWatcher
{
    private CurrencyUpdater currencyUpdater;
    private double value = 1d;

    public EditTextWatcher(CurrencyUpdater currencyUpdater)
    {
        this.currencyUpdater = currencyUpdater;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        try {
            value = Double.valueOf(s.toString());
        } catch (NumberFormatException exc) {
            exc.printStackTrace();
        }
        currencyUpdater.setSelectedCurrencyValue(value);
        currencyUpdater.onUpdate(true);
    }

    @Override
    public void afterTextChanged(Editable s) {}
}
