package com.github.teocci.taskrevolut.interfaces;

import android.view.View;

/**
 * This interface listens fro item's onClick and onFocus events.
 * <p>
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public interface OnItemEventListener
{
    /**
     * @param currencyId this is the tag of the view that represents the Currency Id
     */
    void onItemClick(String currencyId);


    /**
     * @param hasFocus true if the view is focused.
     * @param view the view that resolve the event.
     */
    void onFocusChange(boolean hasFocus, View view);
}
