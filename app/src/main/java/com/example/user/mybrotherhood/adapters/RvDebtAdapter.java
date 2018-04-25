package com.example.user.mybrotherhood.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.mybrotherhood.R;

/**
 * Created by tommy on 26/04/2018.
 */

public class RvDebtAdapter extends RecyclerView.Adapter<RvDebtAdapter.CvDebtViewHolder> {

    private String[]Dataset;



    public static class CvDebtViewHolder extends RecyclerView.ViewHolder{
        public CardView CvDebt;
        public TextView MemberName;
        public TextView DebtAmount;
        public Button DebtPay;
        public Button DebtDetails;

        public CvDebtViewHolder(View v){
            super(v);
            CvDebt=v.findViewById(R.id.cv_debt);
            MemberName=v.findViewById(R.id.debt_member_name);
            DebtAmount=v.findViewById(R.id.debt_amount);
            DebtPay=v.findViewById(R.id.debt_pay);
            DebtDetails=v.findViewById(R.id.debt_details);

        }

    }
    public RvDebtAdapter(String[]myDataset){
        Dataset=myDataset;

    }
    @Override
    public RvDebtAdapter.CvDebtViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_debt_layout,parent,false);
        CvDebtViewHolder dvh=new CvDebtViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(CvDebtViewHolder holder, int position) {
    holder.MemberName.setText(Dataset[position]);
    }

    @Override
    public int getItemCount() {
        return Dataset.length;
    }
}
