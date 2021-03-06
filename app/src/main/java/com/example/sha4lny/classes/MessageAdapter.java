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
import com.example.sha4lny.classes.MessageModel;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Viewholder> {

    private Context context;
    private ArrayList<MessageModel> courseModelArrayList;
    private  onChatClickListener onClickListener;
    private View myView;

    // Constructor
    public MessageAdapter(Context context, ArrayList<MessageModel> courseModelArrayList , onChatClickListener onlickListener) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
        this.onClickListener = onlickListener;
    }

    @NonNull
    @Override
    public MessageAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_card_layout, parent, false);
        myView = view;
        return new Viewholder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        if((position+1) % 2 !=0)
        { myView.setBackgroundResource(R.drawable.contact_list_background);
            holder.txtName.setTextColor(myView.getResources().getColor(R.color.black));
            holder.txtLastMessage.setTextColor(myView.getResources().getColor(R.color.black));
            holder.imgCont.setImageResource(R.drawable.person_black);
        }
        else {
            myView.setBackgroundResource(R.drawable.contact_list_2);
            holder.txtName.setTextColor(myView.getResources().getColor(R.color.white));
            holder.txtLastMessage.setTextColor(myView.getResources().getColor(R.color.white));
            holder.imgCont.setImageResource(R.drawable.person_white);
        }
        MessageModel model = courseModelArrayList.get(position);
        holder.txtName.setText(model.getFullName());
        holder.txtLastMessage.setText(model.getLastMessage());

    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return courseModelArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{



        onChatClickListener onChatClickListener;
        private ImageView imgCont;
        private TextView txtName, txtLastMessage;

        public Viewholder(@NonNull View itemView , onChatClickListener onChatClickListener) {
            super(itemView);
        txtLastMessage = itemView.findViewById(R.id.txtLastMessage);
        txtName =itemView.findViewById(R.id.txtSenderName);
        imgCont= itemView.findViewById(R.id.imgIcon);
        this.onChatClickListener = onChatClickListener;
        itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onChatClickListener.onChatClick(getAdapterPosition());
        }
    }
    public interface  onChatClickListener{
        void onChatClick(int position);
    }
}
