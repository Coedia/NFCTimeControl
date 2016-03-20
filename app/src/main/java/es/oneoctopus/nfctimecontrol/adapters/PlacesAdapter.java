/*
 * Copyright (c) 2016. OneOctopus www.oneoctopus.es
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.oneoctopus.nfctimecontrol.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.io.Serializable;
import java.util.List;

import es.oneoctopus.nfctimecontrol.R;
import es.oneoctopus.nfctimecontrol.activities.PlaceActivity;
import es.oneoctopus.nfctimecontrol.data.PlacesDAO;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    private Activity context;
    private List<String> items;
    private PlacesDAO db;

    public PlacesAdapter(Activity context, List<String> items) {
        this.context = context;
        this.items = items;
        this.db = new PlacesDAO(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = context.getLayoutInflater().inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(items.get(position));
        holder.subtitle.setText(String.format(context.getResources().getQuantityString(R.plurals.been_here_times, ((Long) db.getVisitsCount(items.get(position))).intValue()), db.getVisitsCount(items.get(position))));

        TextDrawable initials = new TextDrawable.Builder()
                .setHeight(dpToPx(48))
                .setWidth(dpToPx(48))
                .setShape(TextDrawable.DRAWABLE_SHAPE_OVAL)
                .setText(items.get(position).substring(0,1))
                .setColor(getAppropiateColor(position))
                .build();

        holder.initials.setCompoundDrawablesWithIntrinsicBounds(initials, null, null, null);
    }

    private int dpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private int getAppropiateColor(int position){
        if(position%2 == 0)
            return ContextCompat.getColor(context, R.color.accent);
        else
            return ContextCompat.getColor(context, R.color.primary_dark);
    }

    public void replaceData(List<String> newItems){
        this.items = newItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView title;
        private TextView subtitle;
        private TextView initials;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.place_title);
            this.subtitle = (TextView) itemView.findViewById(R.id.place_subtitle);
            this.initials = (TextView) itemView.findViewById(R.id.initials);

            itemView.setLongClickable(true);
            itemView.setClickable(true);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent placeActivity = new Intent(context, PlaceActivity.class);
            placeActivity.putExtra("place", items.get(getAdapterPosition()));
            placeActivity.putExtra("position", getAdapterPosition());
            context.startActivity(placeActivity);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

}
