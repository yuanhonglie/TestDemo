package com.example.homlee.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class WanYueShanFragment extends Fragment {
    private static final String BUILDING_TYPE = "building";
    /**
     * 0 - 4栋
     * 1 - 5栋
     * 2 - 6栋
     * 3 - 7栋
     */
    private int mBuildingType;
    private List<TextView> titleTextViews = new ArrayList<>(16);
    private RecyclerView mRecyclerView;
    private TableLayout mTableLayout;
    private TableRow mTableRow;
    private RoomAdapter mRoomAdapter;
    private CompositeDisposable mDisposables = new CompositeDisposable();
    private DataHelper mDataHelper;
    private String[] building4 = {"3房(89)-西北","3房(112)-西南", "3房(89)-东南","2房(88)-东南", "2房(88)-东南"};
    private String[] building5 = {"4房(140)-西南","3房(89)-东南", "3房(89)-西南","3房(119)-西南"};
    private String[] building6 = {"3房(114)-西北","3房(114)-西南", "3房(89)-东南","3房(89)-西南"};
    private String[] building7 = {"3房(89)-西北","3房(112)-西南", "3房(89)-东南","2房(88)-东南", "2房(88)-东南"};

    public static WanYueShanFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(BUILDING_TYPE, position);
        WanYueShanFragment fragment = new WanYueShanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_wanyueshan, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        mRecyclerView = root.findViewById(R.id.rv_rooms);
        mTableLayout = root.findViewById(R.id.table_header);
        mTableRow = root.findViewById(R.id.table_row);
    }

    private void updateOutTextView(int buildingType) {
        String[] titles = getTitles();
        for (int i = 0; i < titles.length; i++) {
            TextView tv = createTextView();
            tv.setText(titles[i]);
            mTableRow.addView(tv);
        }
        mTableRow.setGravity(Gravity.CENTER);
        mTableLayout.setStretchAllColumns(true);
    }

    private TextView createTextView() {
        TextView tv = new TextView(getContext());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tv.setTextColor(Color.BLACK);
        tv.setBackgroundColor(Color.parseColor("#b4eeb4"));
        return tv;
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
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), getRoomPerFloor()));
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
            for (int j = 0; j < getRoomPerFloor(); j++) {
                Room room = new Room(mBuildingType, i, j);
                //room.setSelected(mDataHelper.isReserved(room.getRoomId()));
                rooms.add(room);
            }
        }

        return rooms;
    }

    private int getFloorCount() {
        int count = 0;
        switch (mBuildingType) {
            case 0:
                count = 18;
                break;
            case 1:
                count = 20;
                break;
            case 2:
                count = 23;
                break;
            case 3:
            default:
                count = 24;
                break;
        }
        return count;
    }

    private int getFloorStart() {
        return 4;
    }

    private int getRoomPerFloor() {
        if (mBuildingType == 0 || mBuildingType == 3) {
            return 5;
        } else {
            return 4;
        }
    }

    private String[] getTitles() {
        String[] titles;
        switch (mBuildingType) {
            case 0:
                titles = building4;
                break;
            case 1:
                titles = building5;
                break;
            case 2:
                titles = building6;
                break;
            case 3:
            default:
                titles = building7;
                break;
        }
        return titles;
    }

    private String getBuildingName() {
        String name;
        switch (mBuildingType) {
            case 0:
                name = getString(R.string.label_wanyueshan_building_name1);
                break;
            case 1:
                name = getString(R.string.label_wanyueshan_building_name2);
                break;
            case 2:
                name = getString(R.string.label_wanyueshan_building_name3);
                break;
            default:
            case 3:
                name = getString(R.string.label_wanyueshan_building_name4);
                break;

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
                        message.append('\n');
                    }
                    message.append(candidate);
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
            //room.setSelected(mDataHelper.isReserved(room.getRoomId()));
        }
        mRoomAdapter.notifyDataSetChanged();
    }

}
