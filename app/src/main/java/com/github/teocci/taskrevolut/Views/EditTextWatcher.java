package com.github.teocci.taskrevolut.Views;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public class EditTextWatcher implements TextWatcher
{
    private CurrencyUpdater mPresenter;
    private double mEndValue = 1d;

    public EditTextWatcher(CurrencyUpdater presenter)
    {
        mPresenter = presenter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        try {
            mEndValue = Double.valueOf(s.toString());
        } catch (NumberFormatException exc) {
            exc.printStackTrace();
        }
        mPresenter.setSelectedCurrencyValue(mEndValue);
    }
}
