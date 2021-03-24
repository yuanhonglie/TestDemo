package com.example.homlee.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.homlee.R;
import com.example.homlee.fragments.BuildingFragment;
import com.example.homlee.fragments.WanYueShanFragment;
import com.example.homlee.guanming.Candidate;
import com.example.homlee.guanming.DataHelper;
import com.example.homlee.guanming.SelectedRoom;
import com.example.homlee.utils.ListUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by homlee on 2018/11/24.
 */

public class WanYueShanActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Button mBtnClear;

    private TextView view1, view2, view3;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合
    private List<WanYueShanFragment> mFragmentList;
    private int[] mTitleIds = {
        R.string.label_wanyueshan_building_name1,
        R.string.label_wanyueshan_building_name2,
        R.string.label_wanyueshan_building_name3,
        R.string.label_wanyueshan_building_name4
    };
    private CompositeDisposable mDisposables = new CompositeDisposable();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guan_ming);
        initData();
        initView();
    }

    private void initData() {
        DataHelper.initialize(this);
    }

    private void initView() {
        mViewPager = findViewById(R.id.vp_view);
        mTabLayout = findViewById(R.id.tabs);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_load).setOnClickListener(this);
        findViewById(R.id.btn_analyse).setOnClickListener(this);
        findViewById(R.id.btn_recommend).setOnClickListener(this);
        findViewById(R.id.btn_selected_count).setOnClickListener(this);

        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mFragmentList = createFragments();
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), mFragmentList));
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。

        Disposable disposable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                DataHelper.getInstance().loadAllData();
                emitter.onNext(true);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean roomList) throws Exception {
                        for (WanYueShanFragment fragment : mFragmentList) {
                            fragment.refreshAfterDataSetChanged();
                        }

                    }
                });
        mDisposables.add(disposable);
    }

    private List<WanYueShanFragment> createFragments() {
        List<WanYueShanFragment> fragments = new ArrayList<>(3);
        fragments.add(WanYueShanFragment.newInstance(0));
        fragments.add(WanYueShanFragment.newInstance(1));
        fragments.add(WanYueShanFragment.newInstance(2));
        fragments.add(WanYueShanFragment.newInstance(3));
        return fragments;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear:
                showDeleteAllDialog();
                break;
            case R.id.btn_load:
                loadReservationData();
                break;
            case R.id.btn_analyse:
                loadCandidatesData();
                break;
            case R.id.btn_recommend:
                testCandidates();
                break;
            case R.id.btn_selected_count:
                showSelectedRoomNum();
                break;
            default:
                break;
        }
    }

    private void showSelectedRoomNum() {
        Collection<SelectedRoom> rooms = DataHelper.getInstance().getSelectedRooms();
        Toast.makeText(this, getString(R.string.tip_selected_room_num) + rooms.size(), Toast.LENGTH_SHORT).show();

    }

    private void loadReservationData() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                DataHelper.getInstance().clearAllData();
                Collection<SelectedRoom> roomIds = DataHelper.getInstance().loadDataFromAsset(WanYueShanActivity.this);
                DataHelper.getInstance().reserveRooms(roomIds);
                emitter.onNext(true);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        for (WanYueShanFragment fragment : mFragmentList) {
                            fragment.refreshAfterDataSetChanged();
                        }
                    }
                });
        mDisposables.add(disposable);
    }

    private void loadCandidatesData() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                DataHelper.getInstance().deleteAllCandidates();
                boolean success = DataHelper.getInstance()
                        .loadCandidatesFromAsset(WanYueShanActivity.this);
                emitter.onNext(success);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        Toast.makeText(WanYueShanActivity.this, result
                                ? R.string.load_candidates_completed
                                : R.string.load_candidates_failed,
                                Toast.LENGTH_SHORT).show();
                    }
                });
        mDisposables.add(disposable);
    }

    private void testCandidates() {
        List<Candidate> candidates = DataHelper.getInstance().getCandidatesBySeqNum("BHR012256");
        for (Candidate candidate : candidates) {
            Log.i(className, "testCandidates: " + candidate);
        }
    }


    private void showDeleteAllDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(R.string.dialog_title_delete_all_data);
        builder.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAll();
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

    private void deleteAll() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                DataHelper.getInstance().clearAllData();
                emitter.onNext(true);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        for (WanYueShanFragment fragment : mFragmentList) {
                            fragment.refreshAfterDataSetChanged();
                        }
                    }
                });
        mDisposables.add(disposable);
    }

    public class FragmentAdapter extends FragmentStatePagerAdapter {
        List<? extends Fragment> mFragments;
        public FragmentAdapter(FragmentManager fm, List<? extends Fragment> fragments) {
            super(fm);
            this.mFragments = fragments;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return ListUtils.size(mFragments);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(mTitleIds[position]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposables.dispose();
        DataHelper.destory();
    }
}
