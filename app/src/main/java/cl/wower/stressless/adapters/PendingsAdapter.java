package cl.wower.stressless.adapters;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import cl.wower.stressless.PendingClickListener;
import cl.wower.stressless.R;
import cl.wower.stressless.data.Queries;
import cl.wower.stressless.models.Pending;


public class PendingsAdapter extends RecyclerView.Adapter<PendingsAdapter.ViewHolder> {

    private List<Pending> pendings = new Queries().pendings();

    private PendingClickListener listener;

    public PendingsAdapter(PendingClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pending, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Pending pending = pendings.get(position);
        holder.textView.setText(pending.getName());
        holder.checkBox.setChecked(pending.isDone());


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int auxPosition = holder.getAdapterPosition();
                            Pending auxPending = pendings.get(auxPosition);
                            auxPending.setDone(true);
                            auxPending.save();
                            pendings.remove(auxPosition);
                            notifyItemRemoved(auxPosition);
                        }
                    },400);
                }

            }
        });

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Pending auxPending = pendings.get(holder.getAdapterPosition());
                listener.clickedID(auxPending.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return pendings.size();
    }



    public void update(Pending pending){

        pendings.add(pending);
        notifyDataSetChanged();

    }



    static class ViewHolder extends RecyclerView.ViewHolder {


        private CheckBox checkBox;
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.pendingCb);
            textView = itemView.findViewById(R.id.pendingTv);
        }
    }
}
