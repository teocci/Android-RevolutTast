package com.github.teocci.taskrevolut.interfaces;

import android.view.View;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public interface OnItemEventListener
{
    void onItemClick(String currencyId);

    void onFocusChange(boolean hasFocus, View view);
}
