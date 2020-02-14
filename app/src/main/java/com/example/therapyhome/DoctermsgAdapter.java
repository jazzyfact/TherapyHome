package com.example.therapyhome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoctermsgAdapter extends RecyclerView.Adapter<DoctermsgAdapter.ViewHolder>{

    private List<Doctermsg> docterMsgList;

    // 어댑터 생성자
    public DoctermsgAdapter(List<Doctermsg> doctermsgList) {
        this.docterMsgList = doctermsgList;
    }

    // 아이템 갯수 구하기
    @Override
    public int getItemCount() {
        return docterMsgList.size();
    }


    // 뷰홀더 만들기
    @NonNull
    @Override
    public DoctermsgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_doctermsg, parent, false);
        return new DoctermsgAdapter.ViewHolder(v);
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
    public void onBindViewHolder(@NonNull DoctermsgAdapter.ViewHolder holder, int position) {
        holder.itemDocterMsgTvName.setText(docterMsgList.get(position).getName());
        holder.itemDocterMsgtvPhone.setText(Integer.toString(docterMsgList.get(position).getPhone()));
    }

}
