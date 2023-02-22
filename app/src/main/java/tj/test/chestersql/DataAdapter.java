package tj.test.chestersql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private List<Tt> listItems;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public DataAdapter(Context context, List<Tt> listItems, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.listItems = listItems;
        this.onItemClickListener = onItemClickListener;
    }

    public void addItems(List<Tt> list) {
        listItems.addAll(list);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Tt item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tt listItem = listItems.get(position);
        holder.id.setText("id: " + listItem.getId());
        holder.name.setText("name: " + listItem.getName());
        holder.childCount.setText("count: " + listItem.getCountIds());
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(listItems.get(position));
            }
        });
    }

    public void clear() {
        int size = listItems.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                listItems.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id, name, childCount;

        public ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.tvDBId);
            name = itemView.findViewById(R.id.tvDBName);
            childCount = itemView.findViewById(R.id.tvDBCount);


        }
    }
}