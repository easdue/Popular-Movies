package nl.erikduisters.popularmovies.util;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Erik Duisters on 05-03-2018.
 */

public interface ViewHolderClickListener<T extends RecyclerView.ViewHolder> {
    void onClick(T viewHolder);
}
