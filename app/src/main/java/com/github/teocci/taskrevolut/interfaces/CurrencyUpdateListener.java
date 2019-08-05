package com.github.teocci.taskrevolut.interfaces;

import com.github.teocci.taskrevolut.models.CurrencyRate;

import java.util.List;

/**
 * This interface provides callbacks to handle the update emitted or any error notification issued by the updater.
 * <p>
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public interface CurrencyUpdateListener
{
    void onUpdate(List<CurrencyRate> rateList, boolean ignoreFocus);

    void onError(String message);
}
