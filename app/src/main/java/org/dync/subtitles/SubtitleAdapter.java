package org.dync.subtitles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dync.subtitleconverter.subtitleFile.Caption;
import org.dync.subtitleconverter.subtitleFile.TimedTextObject;

import java.util.ArrayList;
import java.util.Collection;

public class SubtitleAdapter extends RecyclerView.Adapter<SubtitleAdapter.ViewHolder> {
    private Context context;
    private TimedTextObject model = null;
    private ArrayList<Caption> mData = new ArrayList<>();

    public SubtitleAdapter(Context context) {
        this.context = context;
    }

    public void setNewData(TimedTextObject model) {
        this.model = model;
        Collection<Caption> captions = model.captions.values();
        mData.clear();
        mData.addAll(captions);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subtitle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Caption caption = mData.get(position);
        holder.content.setText(caption.toString());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    final class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }
}
