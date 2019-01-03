package com.example.homlee.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.homlee.R;
import com.example.homlee.guanming.Candidate;
import com.example.homlee.guanming.DataHelper;
import com.example.homlee.guanming.Room;
import com.example.homlee.guanming.RoomAdapter;
import com.example.homlee.guanming.RoomItemDecoration;
import com.example.homlee.guanming.SelectedRoom;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by homlee on 2018/11/24.
 */

public class BuildingFragment extends Fragment {
    private static final String BUILDING_TYPE = "building";
    private static final int ROOM_PER_FLOOR = 8;
    private int mBuildingType;
    private List<TextView> abOutTextViews = new ArrayList<>(4);
    private List<TextView> dOutTextViews = new ArrayList<>(4);
    private RecyclerView mRecyclerView;
    private RoomAdapter mRoomAdapter;
    private CompositeDisposable mDisposables = new CompositeDisposable();
    private DataHelper mDataHelper;


    public static BuildingFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(BUILDING_TYPE, position);
        BuildingFragment fragment = new BuildingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_guanming, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        mRecyclerView = root.findViewById(R.id.rv_rooms);
        abOutTextViews.add(findTextView(root, R.id.tv_out_east_ab1));
        //abOutTextViews.add(findTextView(root, R.candidateId.tv_out_east_ab2));
        abOutTextViews.add(findTextView(root, R.id.tv_out_west_ab1));
        //abOutTextViews.add(findTextView(root, R.candidateId.tv_out_west_ab2));
        dOutTextViews.add(findTextView(root, R.id.tv_out_east_d1));
        //dOutTextViews.add(findTextView(root, R.candidateId.tv_out_east_d2));
        dOutTextViews.add(findTextView(root, R.id.tv_out_west_d1));
        //dOutTextViews.add(findTextView(root, R.candidateId.tv_out_west_d2));
    }

    private void updateOutTextView(int buildingType) {
        List<TextView> textViews = (buildingType == 2) ? dOutTextViews : abOutTextViews;
        for (TextView tv : textViews) {
            tv.setTextColor(getResources().getColor(R.color.com_text_red));
        }
    }

    private TextView findTextView(View root, int id) {
        return (TextView) root.findViewById(id);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        mDataHelper = DataHelper.getInstance();
        Bundle args = getArguments();
        mBuildingType = args.getInt(BUILDING_TYPE);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 8));
        mRoomAdapter = new RoomAdapter(getContext(), new ArrayList<Room>());
        mRoomAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object data = v.getTag();
                if (data != null && data instanceof Integer) {
                    final int position = (Integer) v.getTag();
                    final Room room = mRoomAdapter.getRoom(position);
                    showEditDialog(position, room);
                }
            }
        });
        mRecyclerView.setAdapter(mRoomAdapter);
        mRecyclerView.addItemDecoration(new RoomItemDecoration());
        updateOutTextView(mBuildingType);
    }

    private List<Room> createRooms() {
        List<Room> rooms = new ArrayList<>(240);
        int start = getFloorStart();
        int count = getFloorCount();

        for (int i = start; i < start + count; i++) {
            for (int j = 0; j < ROOM_PER_FLOOR; j++) {
                Room room = new Room(mBuildingType, i, j);
                room.setSelected(mDataHelper.isReserved(room.getRoomId()));
                rooms.add(room);
            }
        }

        return rooms;
    }

    private int getFloorCount() {
        return mBuildingType == 2 ? 27 : 30;
    }

    private int getFloorStart() {
        return mBuildingType == 2 ? 5 : 4;
    }

    private String getBuildingName() {
        String name = getString(R.string.label_building_name1);
        switch (mBuildingType) {
            case 0:
                name = getString(R.string.label_building_name1);
                break;
            case 1:
                name = getString(R.string.label_building_name2);
                break;
            case 2:
                name = getString(R.string.label_building_name3);
                break;
                default:
        }
        return name;
    }

    private void showEditDialog(final int position, final Room room) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(getBuildingName()+room);
        SelectedRoom selectedRoom = mDataHelper.getSelectedRoom(room.getRoomId());
        if (selectedRoom != null) {
            String seqNum = selectedRoom.getCandidateId();
            if (!TextUtils.isEmpty(seqNum)) {
                StringBuilder message = new StringBuilder(128);
                List<Candidate> candidates = DataHelper.getInstance().getCandidatesBySeqNum(seqNum);
                for (int i = 0; i < candidates.size(); i++) {
                    Candidate candidate = candidates.get(i);
                    if (i > 0) {
                        message.append("\n");
                    }
                    message.append(""+candidate);
                }

                builder.setMessage(message);
            }


        }
        builder.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*
                if (room.isSelected()) {
                    mDataHelper.cancel(room.getRoomId());
                    room.setSelected(false);
                } else {
                    mDataHelper.reserve(room.getRoomId());
                    room.setSelected(true);
                }
                mRoomAdapter.notifyItemChanged(position);
                */
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposables.dispose();
    }

    public void refreshAfterDataSetChanged() {
        mRoomAdapter.setData(createRooms());
        for (Room room : mRoomAdapter.getData()) {
            room.setSelected(mDataHelper.isReserved(room.getRoomId()));
        }
        mRoomAdapter.notifyDataSetChanged();
    }

}
