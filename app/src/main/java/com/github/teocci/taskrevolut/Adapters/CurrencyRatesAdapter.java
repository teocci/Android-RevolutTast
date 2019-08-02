package com.github.teocci.taskrevolut.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.github.teocci.taskrevolut.R;
import com.github.teocci.taskrevolut.Views.CurrencyRateViewHolder;
import com.github.teocci.taskrevolut.models.CurrencyRate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public class CurrencyRatesAdapter extends RecyclerView.Adapter<CurrencyRateViewHolder>
{
    private Map<String, Drawable> currencyIcons;
    private Map<String, CurrencyRate> currencyNames;

    private List<CurrencyRate> currencyRateList = new ArrayList<>();

    public CurrencyRatesAdapter(List<CurrencyRate> currencyRateList)
    {
        this.currencyRateList = currencyRateList;
    }

    @NonNull
    @Override
    public CurrencyRateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_currency, parent, false);

        // Return a new holder instance
        return new CurrencyRateViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyRateViewHolder viewHolder, int position)
    {
        // Get the data model based on position
        CurrencyRate rate = currencyRateList.get(position);
        String currencyId = rate.currencyId;

//        if (currencyNames.containsKey(currencyId)) {
//            viewHolder.bindView(
//                    rate,
//                    onItemClickListener,
//                    items.get(isoKey)!!,
//                    editTextWatcher
//            )
//        } else {
//            holder.bindView(
//                    rate,
//                    onItemClickListener,
//                    items.get(UNDEFINED_NAME)!!,
//                    editTextWatcher
//            )
//        }

        // Set item views based on your views and data model
        TextView tvCurrencyId = viewHolder.tvCurrencyId;
        TextView tvCurrencyName = viewHolder.tvCurrencyName;
        EditText etCurrencyValue = viewHolder.etCurrencyValue;
    }

    @Override
    public int getItemCount()
    {
        return currencyRateList.size();
    }
}
