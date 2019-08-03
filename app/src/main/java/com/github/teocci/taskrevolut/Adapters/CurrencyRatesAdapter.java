package com.github.teocci.taskrevolut.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.teocci.taskrevolut.R;
import com.github.teocci.taskrevolut.Views.CurrencyRateViewHolder;
import com.github.teocci.taskrevolut.Views.EditTextWatcher;
import com.github.teocci.taskrevolut.interfaces.OnItemEventListener;
import com.github.teocci.taskrevolut.models.CurrencyRate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.github.teocci.taskrevolut.utils.Config.UNDEFINED_NAME;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-02
 */
public class CurrencyRatesAdapter extends RecyclerView.Adapter<CurrencyRateViewHolder>
{
    private Map<String, Drawable> currencyFlags;
    private Map<String, String> currencyNames;

    private EditTextWatcher editTextWatcher;
    private OnItemEventListener onItemEventListener;

    private List<CurrencyRate> currencyRateList = new ArrayList<>();

    public CurrencyRatesAdapter(OnItemEventListener onItemEventListener, EditTextWatcher editTextWatcher)
    {
        this.onItemEventListener = onItemEventListener;
        this.editTextWatcher = editTextWatcher;
    }

    @NonNull
    @Override
    public CurrencyRateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View currencyRateView = inflater.inflate(R.layout.item_currency, parent, false);

        // Return a new holder instance
        return new CurrencyRateViewHolder(currencyRateView);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyRateViewHolder viewHolder, int position)
    {
        // Get the data model based on position
        CurrencyRate rate = currencyRateList.get(position);
        String currencyId = rate.currencyId;

        if (currencyNames.containsKey(currencyId)) {
            viewHolder.bindView(
                    rate,
                    onItemEventListener,
                    editTextWatcher
            );
        } else {
            rate.currencyName = currencyNames.get(UNDEFINED_NAME);
            viewHolder.bindView(
                    rate,
                    onItemEventListener,
                    editTextWatcher
            );
        }

        // Set item views based on your views and data model
//        TextView currencyId = viewHolder.currencyId;
//        TextView currencyName = viewHolder.currencyName;
//        EditText currencyValue = viewHolder.currencyValue;
    }

    @Override
    public int getItemCount()
    {
        return currencyRateList.size();
    }

    public void updateData(List<CurrencyRate> rateUpdateList)
    {
        if (rateUpdateList == null) return;
        int updateSize = rateUpdateList.size();

        if (currencyRateList.size() == 0) {
            currencyRateList.addAll(rateUpdateList);
            initCurrencyRateList();
            notifyItemRangeInserted(0, updateSize);
        } else if (currencyRateList.size() == updateSize) {
            updateList(rateUpdateList);
            notifyItemRangeChanged(0, updateSize);
        } else {
            updateList(rateUpdateList);
            notifyDataSetChanged();
        }
    }

    private void initCurrencyRateList()
    {
        if (isCurrencyRateListEmpty()) return;

        int size = currencyRateList.size();
        for (int i = 0; i < size; i++) {
            CurrencyRate rate = currencyRateList.get(i);
            if (rate == null) return;

            rate.currencyName = currencyNames.get(rate.currencyId);
            rate.currencyFlag = currencyFlags.get(rate.currencyId);
            currencyRateList.set(i, rate);
        }
    }

    private void updateList(List<CurrencyRate> rateUpdateList)
    {
        if (isCurrencyRateListEmpty()) return;

        for (CurrencyRate rate : rateUpdateList) {
            updateById(rate.currencyId, rate.value);
        }
    }


    private void updateById(String currencyId, double currencyValue)
    {
        if (isCurrencyRateListEmpty()) return;

        int size = currencyRateList.size();
        for (int i = 0; i < size; i++) {
            CurrencyRate rate = currencyRateList.get(i);
            if (rate == null) return;
            if (rate.currencyId.equals(currencyId)) {
                rate.value = currencyValue;
                currencyRateList.set(i, rate);
                return;
            }
        }
    }

    public void moveToTop(String currencyId)
    {
        if (isCurrencyRateListEmpty()) return;

        int position = getPositionById(currencyId);
        if (position == -1) return;

        CurrencyRate item = currencyRateList.get(position);
        currencyRateList.remove(position);
        currencyRateList.add(0, item);

        notifyItemMoved(position, 0);
    }

    public void setCurrencyFlags(Map<String, Drawable> currencyFlags)
    {
        this.currencyFlags = currencyFlags;
    }

    public void setCurrencyNames(Map<String, String> currencyNames)
    {
        this.currencyNames = currencyNames;
    }

    public int getPositionById(String currencyId)
    {
        if (isCurrencyRateListEmpty()) return -1;

        int size = currencyRateList.size();
        for (int i = 0; i < size; i++) {
            if (currencyRateList.get(i).currencyId.equals(currencyId)) {
                return i;
            }
        }

        return -1;
    }

    public double getValueById(String currencyId)
    {
        if (isCurrencyRateListEmpty()) return -1;

        int size = currencyRateList.size();
        for (int i = 0; i < size; i++) {
            if (currencyRateList.get(i).currencyId.equals(currencyId)) {
                return currencyRateList.get(i).value;
            }
        }

        return -1;
    }

    public boolean isCurrencyRateListEmpty() {
        return currencyRateList == null || currencyRateList.isEmpty();
    }
}
