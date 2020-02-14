package com.example.therapyhome.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.therapyhome.item.DoctorMsg;
import com.example.therapyhome.R;

import java.util.List;

public class DoctorPatientListAdapter extends RecyclerView.Adapter<DoctorPatientListAdapter.ViewHolder>{

    private List<DoctorMsg> docterMsgList;

    // 어댑터 생성자
    public DoctorPatientListAdapter(List<DoctorMsg> doctorMsgList) {
        this.docterMsgList = doctorMsgList;
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
                .inflate(R.layout.item_doctermsg, parent, false);
        return new DoctorPatientListAdapter.ViewHolder(v);
    }

    // 뷰홀더-레이아웃 연결하기
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemDocterMsgTvName;
        TextView itemDocterMsgtvPhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemDocterMsgTvName = itemView.findViewById(R.id.item_doctermsg_textview_name);
            itemDocterMsgtvPhone = itemView.findViewById(R.id.item_doctermsg_textview_phone);
        }
    }

    // 뷰홀더-내용물 연결하기
    @Override
    public void onBindViewHolder(@NonNull DoctorPatientListAdapter.ViewHolder holder, int position) {
        holder.itemDocterMsgTvName.setText(docterMsgList.get(position).getName());
        holder.itemDocterMsgtvPhone.setText(Integer.toString(docterMsgList.get(position).getPhone()));
    }

}
