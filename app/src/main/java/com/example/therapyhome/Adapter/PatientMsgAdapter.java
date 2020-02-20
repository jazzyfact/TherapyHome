package com.example.therapyhome.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.therapyhome.CustomDialogActivity;
import com.example.therapyhome.CustomDialogSendMsgActivity;
import com.example.therapyhome.PatientMsgActivity;
import com.example.therapyhome.R;
import com.example.therapyhome.item.PatientEditKeyWord;

import java.util.List;

public class PatientMsgAdapter extends RecyclerView.Adapter<PatientMsgAdapter.ViewHolder> {

    /**
     *
     * 환자 텍스트 수정해주는 어댑터
     * (환자화면임)
     */


    private List<PatientEditKeyWord> patientEditKeyWordList;

    // 어댑터 생성자
    public PatientMsgAdapter(List<PatientEditKeyWord> patientEditKeyWordList) {
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
    public PatientMsgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_patient_editkeyword, parent, false);
        return new ViewHolder(v);
    }

    // 뷰홀더-레이아웃 연결하기
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemGuardianEditTv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemGuardianEditTv = itemView.findViewById(R.id.item_editKeyword_textview_patient);


        }
    }

    // 뷰홀더-내용물 연결하기
    @Override
    public void onBindViewHolder(@NonNull final PatientMsgAdapter.ViewHolder holder, final int position) {
//        patientEditKeyWordList.get(position).getText()

        holder.itemGuardianEditTv.setText(patientEditKeyWordList.get(position).getText());
        holder.itemGuardianEditTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 커스텀 다이얼로그로 인텐트 보내기
                String intentCk = "문자보내기";
                Intent patientEdit = new Intent(v.getContext(), PatientMsgActivity.class);
                patientEdit.putExtra("intentCk",intentCk);
                patientEdit.putExtra("sendText",patientEditKeyWordList.get(position).getText());
//                patientEdit.putExtra("sendNum",patientEditKeyWordList.get(position).getNum());
                ((PatientMsgActivity)v.getContext()).startActivityForResult(new Intent(patientEdit), 0);

            }
        });
    }

}

