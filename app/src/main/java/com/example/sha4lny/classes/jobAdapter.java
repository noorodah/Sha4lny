package com.example.sha4lny.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sha4lny.R;

import java.util.ArrayList;

public class jobAdapter extends RecyclerView.Adapter<jobAdapter.Viewholder> {
private Context context;
private ArrayList<JobsModel> jobsModelArrayList;
private OnJobListener monJobListener;
    public jobAdapter(Context context, ArrayList<JobsModel> jobsModelArrayList,OnJobListener onJobListener) {
        this.context = context;
        this.jobsModelArrayList = jobsModelArrayList;
        this.monJobListener = onJobListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);

        return new Viewholder(view,monJobListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        JobsModel jobsModel= jobsModelArrayList.get(position);
        holder.imgIcon.setImageResource(jobsModel.getJobImg());
        holder.txtOfferedJob.setText("الوظيفة الشاغرة : " +jobsModel.getOfferedJob() );
        holder.txtJobType.setText("نوع العمل : " + jobsModel.getJobType() );
        holder.txtJobTitle.setText(jobsModel.getJobTitle());

    }

    @Override
    public int getItemCount() {
        return jobsModelArrayList.size();
    }

    public interface OnJobListener{
        void onJobClick(int pos);
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgIcon;
        private TextView txtJobTitle,txtJobType,txtOfferedJob;
        OnJobListener onJobListener;

        public Viewholder(@NonNull View itemView,OnJobListener onJobListener) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            txtJobTitle = itemView.findViewById(R.id.txtJobTitle);
            txtJobType = itemView.findViewById(R.id.txtJobType);
            txtOfferedJob = itemView.findViewById(R.id.txtOfferedPos);
            this.onJobListener = onJobListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onJobListener.onJobClick(getAdapterPosition());
        }
    }
}
