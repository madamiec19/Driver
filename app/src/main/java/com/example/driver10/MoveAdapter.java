package com.example.driver10;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driver10.Activities.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MoveAdapter extends RecyclerView.Adapter<MoveAdapter.MoveHolder> {
    private List<Move> moves = new ArrayList<>();
    private Move mRecentlyDeleted;
    private int mRecentlyDeletedPosition;

    @NonNull
    @Override
    public MoveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.move_item, parent, false);

        return new MoveHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MoveHolder holder, int position) {
        Move currentMove = moves.get(position);
        holder.tvCar.setText(currentMove.getCar());
        holder.tvKmStart.setText(String.valueOf(currentMove.getKmStart()));
        holder.tvKmStop.setText(String.valueOf(currentMove.getKmStop()));

        //
        BigDecimal tempBig = new BigDecimal(Double.toString(currentMove.getValue()));
        tempBig = tempBig.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        String strValue = tempBig
                .toPlainString();

        //
        holder.tvValue.setText(String.valueOf(strValue));


    }

    @Override
    public int getItemCount() {
        return moves.size();
    }

    public void setMoves(List<Move> moves){
        this.moves = moves;
        notifyDataSetChanged();
    }

    public Move getMoveAt(int position) {
        return moves.get(position);
    }


    public void deleteItem(int position) {
        mRecentlyDeleted = getMoveAt(position);
        mRecentlyDeletedPosition = position;
        moves.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = null;
        view.findViewById(R.id.relativeLayout);
        Snackbar snackbar = Snackbar.make(view, "ruch usunięty",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("cofnij", v -> undoDelete());
        snackbar.show();
    }

    private void undoDelete() {
        moves.add(mRecentlyDeletedPosition,
                mRecentlyDeleted);
        notifyItemInserted(mRecentlyDeletedPosition);
    }




    class MoveHolder extends RecyclerView.ViewHolder{
        private TextView tvCar;
        private TextView tvKmStart;
        private TextView tvKmStop;
        private TextView tvValue;


        public MoveHolder(View itemView){
            super(itemView);

            tvCar = itemView.findViewById(R.id.tvCar);
            tvKmStart = itemView.findViewById(R.id.tvKmStart);
            tvKmStop = itemView.findViewById(R.id.tvKmStop);
            tvValue = itemView.findViewById(R.id.tvValue);
        }
    }


}