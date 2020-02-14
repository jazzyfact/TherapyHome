package com.example.therapyhome.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.therapyhome.item.GuardianMsg;
import com.example.therapyhome.R;

import java.util.List;

public class GuardianMsgAdapter extends RecyclerView.Adapter<GuardianMsgAdapter.ViewHolder> {

    private List<GuardianMsg> guardianMsgList;

    // 어댑터 생성자
    public GuardianMsgAdapter(List<GuardianMsg> guardianMsgList) {
        this.guardianMsgList = guardianMsgList;
    }

    // 아이템 갯수 구하기
    @Override
    public int getItemCount() {
        return guardianMsgList.size();
    }


    // 뷰홀더 만들기
    @NonNull
    @Override
    public GuardianMsgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_guardian_msg, parent, false);
        return new ViewHolder(v);
    }

    // 뷰홀더-레이아웃 연결하기
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemGuardianMsgTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemGuardianMsgTv = itemView.findViewById(R.id.item_guardianmsg_textview);
        }
    }

    // 뷰홀더-내용물 연결하기
    @Override
    public void onBindViewHolder(@NonNull GuardianMsgAdapter.ViewHolder holder, int position) {
        holder.itemGuardianMsgTv.setText(guardianMsgList.get(position).getMessage());
    }

}
