package com.thmub.cocobook.ui.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thmub.cocobook.R;
import com.thmub.cocobook.model.bean.RankBookBean;
import com.thmub.cocobook.presenter.BookRankDetailPresenter;
import com.thmub.cocobook.presenter.contract.BookRankDetailContract;
import com.thmub.cocobook.ui.activity.BookDetailActivity;
import com.thmub.cocobook.ui.adapter.BillBookAdapter;
import com.thmub.cocobook.base.BaseMVPFragment;
import com.thmub.cocobook.widget.RefreshLayout;
import com.thmub.cocobook.widget.itemdecoration.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhouas666 on 18-1-23.
 * 分类fragment
 */

public class BookRankDetailFragment extends BaseMVPFragment<BookRankDetailContract.Presenter>
        implements BookRankDetailContract.View{
    /***************************常量********************************/
    private static final String EXTRA_BILL_ID = "extra_bill_id";

    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.refresh_rv_content)
    RecyclerView mRvContent;
    /***************************视图********************************/
    private BillBookAdapter mBillBookAdapter;

    /***************************参数********************************/
    private String mBillId;

    /***************************公共方法********************************/
    public static Fragment newInstance(String billId){
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_BILL_ID,billId);
        Fragment fragment = new BookRankDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /***************************初始化********************************/
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_list;
    }

    @Override
    protected BookRankDetailContract.Presenter bindPresenter() {
        return new BookRankDetailPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mBillId = getArguments().getString(EXTRA_BILL_ID);
    }

    @Override
    protected void initClick() {
        super.initClick();
        mBillBookAdapter.setOnItemClickListener(
                (view, pos) -> {
                    String bookId = mBillBookAdapter.getItem(pos).get_id();
                    String bookTitle = mBillBookAdapter.getItem(pos).getTitle();
                    BookDetailActivity.startActivity(getContext(),bookId,bookTitle);
                }
        );
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mBillBookAdapter = new BillBookAdapter();
        mRvContent.setAdapter(mBillBookAdapter);
    }

    /***************************业务逻辑********************************/
    @Override
    protected void processLogic() {
        super.processLogic();
        mRefreshLayout.showLoading();
        mPresenter.refreshBookBrief(mBillId);
    }

    @Override
    public void finishRefresh(List<RankBookBean> beans) {
        mBillBookAdapter.refreshItems(beans);
    }

    @Override
    public void showError() {
        mRefreshLayout.showError();
    }

    @Override
    public void complete() {
        mRefreshLayout.showFinish();
    }
}
