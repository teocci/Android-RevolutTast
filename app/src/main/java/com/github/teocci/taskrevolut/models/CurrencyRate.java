package com.github.teocci.taskrevolut.models;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public class CurrencyRate
{
    public String currencyId;
    public String currencyName;
    public double value;
    public double rate;


    public CurrencyRate(String currencyId, double value)
    {
        this.currencyId = currencyId;
        this.value = value;
    }

    public CurrencyRate(String currencyId, double rate, double value)
    {
        this.currencyId = currencyId;
        this.rate = rate;
        this.value = value;
    }

    public CurrencyRate(String currencyId, String currencyName, double rate, double value)
    {
        this.currencyId = currencyId;
        this.currencyName = currencyName;
        this.rate = rate;
        this.value = value;
    }
}
