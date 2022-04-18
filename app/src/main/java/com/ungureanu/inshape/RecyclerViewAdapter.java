package com.ungureanu.inshape;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements
ItemTouchHelperAdapter{
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<String> Names;
    private ArrayList<String> Kgs;
    private ArrayList<String> Sets;
    private ArrayList<String> Reps;
    private Context context;
    private ItemTouchHelper mTouchHelper;

    public RecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<String> kgs, ArrayList<String> sets, ArrayList<String> reps) {
        Names = names;
        Kgs = kgs;
        Sets = sets;
        Reps = reps;
        this.context = context;
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder( @androidx.annotation.NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        //Log.d(TAG, "onBindViewHolder: called");

        viewHolder.textViewName.setText(Names.get(i));
        viewHolder.textViewKgs.setText(Kgs.get(i));
        viewHolder.textViewSets.setText(Sets.get(i));
        viewHolder.textViewReps.setText(Reps.get(i));
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "onClick: called");
                Toast.makeText(context, Names.get(i), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Names.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        String fromName = Names.get(fromPosition);
        Names.remove(fromName);
        Names.add(toPosition, fromName);

        String fromKgs = Kgs.get(fromPosition);
        Kgs.remove(fromKgs);
        Kgs.add(toPosition, fromKgs);

        String fromSets = Sets.get(fromPosition);
        Sets.remove(fromSets);
        Sets.add(toPosition, fromSets);

        String fromReps = Reps.get(fromPosition);
        Reps.remove(fromReps);
        Reps.add(toPosition, fromReps);

        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwipe(int position) {
        DatabaseHelper myDb = new DatabaseHelper(this.context);
        myDb.deleteData(Names.get(position), Kgs.get(position), Sets.get(position), Reps.get(position));
        Names.remove(position);
        Kgs.remove(position);
        Sets.remove(position);
        Reps.remove(position);
        notifyItemRemoved(position);
    }

    public void setItemTouchHelperAdapter(ItemTouchHelper touchHelper){
        this.mTouchHelper = touchHelper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnTouchListener,
            GestureDetector.OnGestureListener {

        TextView textViewName;
        TextView textViewKgs;
        TextView textViewSets;
        TextView textViewReps;
        RelativeLayout parentLayout;
        GestureDetector gestureDetector;

        public ViewHolder( @androidx.annotation.NonNull View itemView) {
            super(itemView);

            this.textViewName = itemView.findViewById(R.id.idExName);
            this.textViewKgs = itemView.findViewById(R.id.idKg);
            this.textViewSets = itemView.findViewById(R.id.idSets);
            this.textViewReps = itemView.findViewById(R.id.idReps);
            this.parentLayout = itemView.findViewById(R.id.idRelativeLayout);
            gestureDetector = new GestureDetector(itemView.getContext(), this);
            itemView.setOnTouchListener(this);
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {

            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            mTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return true;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            gestureDetector.onTouchEvent(motionEvent);

            return true;//Consume the touch event
        }

    }
}
