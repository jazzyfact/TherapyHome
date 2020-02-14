package com.example.therapyhome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GuardianmsgAdapter extends RecyclerView.Adapter<GuardianmsgAdapter.ViewHolder> {

    private List<Guardianmsg> guardianmsgList;

    // 어댑터 생성자
    public GuardianmsgAdapter(List<Guardianmsg> guardianmsgList) {
        this.guardianmsgList = guardianmsgList;
    }

    // 아이템 갯수 구하기
    @Override
    public int getItemCount() {
        return guardianmsgList.size();
    }


    // 뷰홀더 만들기
    @NonNull
    @Override
    public GuardianmsgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_guardianmsg, parent, false);
        return new ViewHolder(v);
    }

    // 뷰홀더-레이아웃 연결하기
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item_guardianmsg_textview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_guardianmsg_textview = itemView.findViewById(R.id.item_guardianmsg_textview);
        }
    }

    // 뷰홀더-내용물 연결하기
    @Override
    public void onBindViewHolder(@NonNull GuardianmsgAdapter.ViewHolder holder, int position) {
        holder.item_guardianmsg_textview.setText(guardianmsgList.get(position).getMessage());
    }

}
