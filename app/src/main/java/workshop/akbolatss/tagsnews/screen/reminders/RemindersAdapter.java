package workshop.akbolatss.tagsnews.screen.reminders;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.repositories.source.ReminderItem;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.RemindersVH> {

    private List<ReminderItem> mList;

    private final OnReminderClickInterface mClickInterface;

    public RemindersAdapter(OnReminderClickInterface mClickInterface) {
        mList = new ArrayList<>();
        this.mClickInterface = mClickInterface;
    }

    public interface OnReminderClickInterface {

        public void onReminderOptions(ReminderItem rItem, View view, int position);
    }

    private final View.OnClickListener mInternalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ReminderItem rItem = (ReminderItem) view.getTag();
            mClickInterface.onReminderOptions(rItem, view, 2);
        }
    };

    @Override
    public RemindersVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        View view = mLayoutInflater.inflate(R.layout.rv_reminder_item, parent, false);
        return new RemindersVH(view);
    }

    @Override
    public void onBindViewHolder(RemindersVH holder, int position) {
        ReminderItem rItem = mList.get(position);
        holder.bind(rItem);

        holder.mImgButton.setOnClickListener(mInternalListener);
        holder.mImgButton.setTag(rItem);

        holder.mCb.setOnClickListener(mInternalListener);
        holder.mCb.setTag(rItem);
    }

    public void onAddItems(List<ReminderItem> rItems) {
        if (rItems != null) {
            mList.clear();
            mList.addAll(rItems);
            notifyDataSetChanged();
        }
    }

    public void onAddItem(ReminderItem rItem) {
        if (rItem != null) {
            mList.add(rItem);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public class RemindersVH extends RecyclerView.ViewHolder {

        @BindView(R.id.cbActivate)
        CheckBox mCb;
        @BindView(R.id.tvTime)
        TextView mTime;
        @BindView(R.id.imgOptions)
        ImageView mImgButton;

        public RemindersVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ReminderItem rItem) {
            mTime.setText(onTimeFixer(rItem.getHour()) + ":" + onTimeFixer(rItem.getMinute()));

            if (rItem.getIsActive()) {
                mCb.setChecked(true);
            } else {
                mCb.setChecked(false);
            }
        }

        private String onTimeFixer(int time) {
            if (time == 0) {
                return "00";
            }
            Pattern ptr = Pattern.compile("^(?:[1-9]|0[1-9])$");
            if (ptr.matcher(String.valueOf(time)).matches()) {
                return "0" + time;
            }
            return String.valueOf(time);
        }
    }
}
