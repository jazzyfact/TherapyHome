package com.example.therapyhome.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.therapyhome.R;
import com.example.therapyhome.item.GuardianMsg;
import com.example.therapyhome.item.PatientEditKeyWord;

import java.util.List;

public class GuardianEditKeyWordAdapter extends RecyclerView.Adapter<GuardianEditKeyWordAdapter.ViewHolder> {

    /**
     * 환자 텍스트 수정해주는 어댑터
     */


    private List<PatientEditKeyWord> patientEditKeyWordList;

    // 어댑터 생성자
    public GuardianEditKeyWordAdapter(List<PatientEditKeyWord> patientEditKeyWordList) {
        this.patientEditKeyWordList = patientEditKeyWordList;
    }

    // 아이템 갯수 구하기
    @Override
    public int getItemCount() {
        return patientEditKeyWordList.size();
    }


    // 뷰홀더 만들기
    @NonNull
    @Override
    public GuardianEditKeyWordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_guardian_editkeyword, parent, false);
        return new ViewHolder(v);
    }

    // 뷰홀더-레이아웃 연결하기
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemGuardianEditTv;
        ImageView ivGuardianEditKeyEdit;
        ImageView ivGuardianEditKeyRe;
        FrameLayout itemContactEditEmergencySelect;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemGuardianEditTv = itemView.findViewById(R.id.item_editKeyword_textview);
            ivGuardianEditKeyEdit =  itemView.findViewById(R.id.iv_guardianEditKey_edit);
            ivGuardianEditKeyRe = itemView.findViewById(R.id.iv_guardianEditKey_re);


        }
    }

    // 뷰홀더-내용물 연결하기
    @Override
    public void onBindViewHolder(@NonNull GuardianEditKeyWordAdapter.ViewHolder holder, int position) {
        holder.itemGuardianEditTv.setText(patientEditKeyWordList.get(position).getText());
        holder.ivGuardianEditKeyEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "수정버튼 눌렀음", Toast.LENGTH_SHORT).show();
            }
        });
        holder.ivGuardianEditKeyRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "삭제버튼 눌렀음", Toast.LENGTH_SHORT).show();
            }
        });




    }

}

