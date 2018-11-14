package com.gogovan.history;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.gogovan.data.entities.TimeTaskEntity;
import com.uber.rib.core.Initializer;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史 view
 * Top level view for {@link HistoryBuilder.HistoryScope}.
 */
class HistoryView extends LinearLayout implements HistoryInteractor.HistoryPresenter {

    public HistoryView(Context context) {
        this(context, null);
    }

    public HistoryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistoryView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private RecyclerView mRecyclerView;
    private HostoryListAdapter mAdapter;
    private List<TimeTaskEntity> mList;

    @Initializer
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mRecyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        mList = new ArrayList<>();
        mAdapter = new HostoryListAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void loadData(List<TimeTaskEntity> list) {

        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();

    }
}
