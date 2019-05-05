package com.enesgemci.mymovies.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.enesgemci.mymovies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * taken from : https://github.com/mancj/MaterialSearchBar
 * modified by enes gemci
 */
public class DefaultSuggestionsAdapter extends SuggestionsAdapter<String, DefaultSuggestionsAdapter.SuggestionHolder> {

    private List<ItemInteractionListener> listeners = new ArrayList<>();

    public DefaultSuggestionsAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    public void addListener(ItemInteractionListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public int getSingleViewHeight() {
        return 50;
    }

    @Override
    public SuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_last_request, parent, false);
        return new SuggestionHolder(view);
    }

    @Override
    public void onBindSuggestionHolder(String suggestion, SuggestionHolder holder, int position) {
        holder.text.setText(getSuggestions().get(position));
    }

    class SuggestionHolder extends RecyclerView.ViewHolder {

        private final TextView text;
        private final ImageView iv_delete;

        public SuggestionHolder(final View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            iv_delete = itemView.findViewById(R.id.iv_delete);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();

                v.setTag(getSuggestions().get(position));

                for (int i = listeners.size() - 1; i >= 0; i--) {
                    listeners.get(i).onItemClicked(position, v);
                }
            });

            iv_delete.setOnClickListener(v -> {
                int position = getAdapterPosition();

                if (position >= 0 && position < getSuggestions().size()) {
                    v.setTag(getSuggestions().get(position));

                    for (int i = listeners.size() - 1; i >= 0; i--) {
                        listeners.get(i).onItemDeleted(position, v);
                    }
                }
            });
        }
    }
}
