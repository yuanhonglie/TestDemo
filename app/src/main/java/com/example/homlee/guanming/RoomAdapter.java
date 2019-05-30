package com.example.homlee.guanming;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.homlee.R;
import com.example.homlee.Utils.ListUtils;

import java.util.List;

/**
 * Created by homlee on 2018/11/24.
 */

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private Context mContext;
    private List<Room> mRooms;
    private View.OnClickListener onClickListener;

    public RoomAdapter(Context context, List<Room> list) {
        mContext = context;
        mRooms = list;
    }

    public void setData(List<Room> list) {
        mRooms = list;
        //notifyDataSetChanged();
    }

    public List<Room> getData() {
        return mRooms;
    }

    public Room getRoom(int position) {
        return mRooms.get(position);
    }

    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_room_layout, parent, false);
        return new RoomViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RoomViewHolder holder, int position) {
        Room room = mRooms.get(position);
        holder.parent.setTag(position);
        holder.tvRoom.setText(""+room);
        setupStyle(holder, room);
    }

    private void setupStyle(RoomViewHolder holder, Room room) {
        //holder.tvRoom.getPaint().setFlags(room.isSelected() ? Paint.STRIKE_THRU_TEXT_FLAG : Paint.ANTI_ALIAS_FLAG);
        //int colorId = ((position / 8) % 2  == 0) ? R.color.white : R.color.com_text_gray_2;
        holder.parent.setBackgroundColor(mContext.getResources().getColor(room.isSelected() ? R.color.com_text_red : R.color.white));
    }

    @Override
    public int getItemCount() {
        return ListUtils.size(mRooms);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View parent;
        TextView tvRoom;

        public RoomViewHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            tvRoom = itemView.findViewById(R.id.tv_room);
            parent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }
    }


}
