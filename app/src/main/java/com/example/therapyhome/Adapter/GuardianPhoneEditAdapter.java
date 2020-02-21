package com.example.therapyhome.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.therapyhome.CustomDialogActivity;
import com.example.therapyhome.CustomDialogDeleteActivity;
import com.example.therapyhome.R;
import com.example.therapyhome.item.GuardianMsg;
import com.example.therapyhome.item.PhoneContactEdit;

import java.util.List;

public class GuardianPhoneEditAdapter extends RecyclerView.Adapter<GuardianPhoneEditAdapter.ViewHolder> {

    /**
     * 휴대폰 번호를 수정하는 보호자 화면 리사이클러뷰 어댑터
     */

    private List<PhoneContactEdit> guardianPhoneEditList ;

    // 어댑터 생성자
    public GuardianPhoneEditAdapter(List<PhoneContactEdit> guardianPhoneEditList) {
        this.guardianPhoneEditList = guardianPhoneEditList;

    }

    // 아이템 갯수 구하기
    @Override
    public int getItemCount() {
        return guardianPhoneEditList.size();
    }


    // 뷰홀더 만들기
    @NonNull
    @Override
    public GuardianPhoneEditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_guardian_contact, parent, false);
        return new ViewHolder(v);
    }

    // 뷰홀더-레이아웃 연결하기
    // 아이템의 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemContactEditNum;
        TextView itemContactEditName;
        FrameLayout itemContactEditEmergency;
        ImageView ivGuardianEdit;
        ImageView ivGuardianre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemContactEditNum = itemView.findViewById(R.id.item_contactEdit_textview_num);
            itemContactEditName = itemView.findViewById(R.id.item_contactEdit_textview_name);
            itemContactEditEmergency = itemView.findViewById(R.id.item_contactEdit_emergencySelect);
            ivGuardianEdit = itemView.findViewById(R.id.iv_guardian_edit);
            ivGuardianre = itemView.findViewById(R.id.iv_guardian_re);
        }
    }

    // 뷰홀더-내용물 연결하기
    @Override
    public void onBindViewHolder(@NonNull final GuardianPhoneEditAdapter.ViewHolder holder, final int position) {
        holder.itemContactEditName.setText(guardianPhoneEditList.get(position).getName());
        Log.d("연락처 리사이클러뷰 ", "onBindViewHolder: "+guardianPhoneEditList.get(position).getName());
        holder.itemContactEditNum.setText(guardianPhoneEditList.get(position).getNum());
        holder.ivGuardianEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 커스텀 다이얼로그로 인텐트 보내기
                String intentCk = "연락처편집";
                Intent patientEdit = new Intent(v.getContext(), CustomDialogActivity.class);
                patientEdit.putExtra("intentCk",intentCk);
                patientEdit.putExtra("name",guardianPhoneEditList.get(position).getName());
                patientEdit.putExtra("num",guardianPhoneEditList.get(position).getNum());
                patientEdit.putExtra("ckbox",guardianPhoneEditList.get(position).getEmergency());
                ((Activity) v.getContext()).startActivity(patientEdit);

            }
        });
        holder.ivGuardianre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 커스텀 다이얼로그로 인텐트 보내기
                String intentCk = "연락처삭제";
                Intent patientEdit = new Intent(v.getContext(), CustomDialogDeleteActivity.class);
                patientEdit.putExtra("intentCk",intentCk);
                patientEdit.putExtra("name",guardianPhoneEditList.get(position).getName());
                patientEdit.putExtra("num",guardianPhoneEditList.get(position).getNum());
                patientEdit.putExtra("ckbox",guardianPhoneEditList.get(position).getEmergency());
                ((Activity) v.getContext()).startActivity(patientEdit);

            }
        });

        Log.d("긴급 연락처  ", "onBindViewHolder: "+guardianPhoneEditList.get(position).getEmergency());
        if(guardianPhoneEditList.get(position).getEmergency().equals("Y")){
            holder.itemContactEditEmergency.setVisibility(View.VISIBLE);
        }else if(guardianPhoneEditList.get(position).getEmergency().equals("N")) {
            holder.itemContactEditEmergency.setVisibility(View.INVISIBLE);
        }


    }



}
