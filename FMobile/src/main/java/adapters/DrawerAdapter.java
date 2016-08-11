package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;
import com.mikepenz.iconics.view.IconicsImageView;

import java.util.Collections;
import java.util.List;

import models.DrawerItem;

/**
 * Created by Focus on 8/8/2016.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    List<DrawerItem> data = Collections.emptyList();
    private SparseBooleanArray selectedItems;

    public DrawerAdapter(Context context, List<DrawerItem> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.drawer_card, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(data.get(position).title);
        holder.icon.setIcon(data.get(position).icon);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        IconicsImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.drawer_title);
            icon = (IconicsImageView) itemView.findViewById(R.id.drawer_icon);
        }

    }
}
