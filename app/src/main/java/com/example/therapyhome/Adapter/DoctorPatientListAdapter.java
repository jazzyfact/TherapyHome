package com.example.therapyhome.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.therapyhome.DoctorMonitorActivity;
import com.example.therapyhome.item.DoctorMsg;
import com.example.therapyhome.R;

import java.util.List;

public class DoctorPatientListAdapter extends RecyclerView.Adapter<DoctorPatientListAdapter.ViewHolder>{

    private List<DoctorMsg> docterMsgList;
    Context context;

    // 어댑터 생성자
    public DoctorPatientListAdapter(List<DoctorMsg> doctorMsgList, Context context) {
        this.docterMsgList = doctorMsgList;
        this.context = context;
    }

    // 아이템 갯수 구하기
    @Override
    public int getItemCount() {
        return docterMsgList.size();
    }


    // 뷰홀더 만들기
    @NonNull
    @Override
    public DoctorPatientListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_doctor_msg, parent, false);
        return new DoctorPatientListAdapter.ViewHolder(v);
    }

    // 뷰홀더-레이아웃 연결하기
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemDocterMsgTvName;
        TextView itemDocterMsgtvPhone;
        Button itemGuardianmsgButtonDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemDocterMsgTvName = itemView.findViewById(R.id.item_doctermsg_textview_name);
            itemDocterMsgtvPhone = itemView.findViewById(R.id.item_doctermsg_textview_phone);
            itemGuardianmsgButtonDelete = itemView.findViewById(R.id.item_guardianmsg_button_delete);
        }
    }

    // 뷰홀더-내용물 연결하기
    @Override
    public void onBindViewHolder(@NonNull DoctorPatientListAdapter.ViewHolder holder, int position) {
        holder.itemDocterMsgTvName.setText(String.valueOf(docterMsgList.get(position).getName()));
        holder.itemDocterMsgtvPhone.setText(String.valueOf(docterMsgList.get(position).getNum()));
        holder.itemGuardianmsgButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), DoctorMonitorActivity.class);
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), DoctorMonitorActivity.class);
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });



    }

}
