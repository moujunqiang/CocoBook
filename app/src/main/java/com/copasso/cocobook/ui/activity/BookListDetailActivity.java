package com.copasso.cocobook.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.copasso.cocobook.App;
import com.copasso.cocobook.R;
import com.copasso.cocobook.base.BaseMVPActivity;
import com.copasso.cocobook.model.bean.BookListDetailBean;
import com.copasso.cocobook.presenter.BookListDetailPresenter;
import com.copasso.cocobook.presenter.contract.BookListDetailContract;
import com.copasso.cocobook.ui.adapter.BookListDetailAdapter;
import com.copasso.cocobook.utils.Constant;
import com.copasso.cocobook.widget.RefreshLayout;
import com.copasso.cocobook.widget.adapter.WholeAdapter;
import com.copasso.cocobook.widget.itemdecoration.DividerItemDecoration;
import com.copasso.cocobook.widget.transform.CircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhouas666 on 18-2-1.
 * 书单详情activity
 */

public class BookListDetailActivity extends BaseMVPActivity<BookListDetailContract.Presenter>
        implements BookListDetailContract.View {

    private static final String EXTRA_DETAIL_ID = "extra_detail_id";
    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.refresh_rv_content)
    RecyclerView mRvContent;
    /************************************************************/
    private BookListDetailAdapter mDetailAdapter;
    private DetailHeader mDetailHeader;
    private List<BookListDetailBean.BooksBean> mBooksList;
    /***************************变量********************************/
    private String mDetailId;
    private int start = 0;
    private int limit = 20;

    public static void startActivity(Context context,String detailId){
        Intent intent  =new Intent(context,BookListDetailActivity.class);
        intent.putExtra(EXTRA_DETAIL_ID,detailId);
        context.startActivity(intent);
    }

    /******************************初始化******************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_refresh_list;
    }

    @Override
    protected BookListDetailContract.Presenter bindPresenter() {
        return new BookListDetailPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (savedInstanceState != null){
            mDetailId = savedInstanceState.getString(EXTRA_DETAIL_ID);
        }else {
            mDetailId = getIntent().getStringExtra(EXTRA_DETAIL_ID);
        }
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().setTitle("书单详情");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initAdapter();
    }

    private void initAdapter(){
        mDetailAdapter = new BookListDetailAdapter(this,new WholeAdapter.Options());
        mDetailHeader = new DetailHeader();
        mDetailAdapter.addHeaderView(mDetailHeader);
        mDetailAdapter.setOnItemClickListener(
                (view, pos) -> {
                    String bookId = mBooksList.get(pos).getBook().get_id();
                    BookDetailActivity.startActivity(this,bookId,mBooksList.get(pos).getBook().getTitle());
                }
        );

        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.addItemDecoration(new DividerItemDecoration(this));
        mRvContent.setAdapter(mDetailAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();
        mDetailAdapter.setOnLoadMoreListener(
                () -> loadBook()
        );
    }

    /*****************************业务逻辑*******************************/
    @Override
    protected void processLogic() {
        super.processLogic();
        mRefreshLayout.showLoading();
        mPresenter.refreshBookListDetail(mDetailId);
    }

    @Override
    public void finishRefresh(BookListDetailBean bean) {
        mDetailHeader.setBookListDetail(bean);
        mBooksList = bean.getBooks();
        refreshBook();
    }

    private void refreshBook(){
        start = 0;
        List<BookListDetailBean.BooksBean.BookBean> books = getBookList();
        mDetailAdapter.refreshItems(books);
        start = books.size();
    }

    private void loadBook(){
        List<BookListDetailBean.BooksBean.BookBean> books = getBookList();
        mDetailAdapter.addItems(books);
        start += books.size();
    }

    private List<BookListDetailBean.BooksBean.BookBean> getBookList(){
        int end = start + limit;
        if (end > mBooksList.size()){
            end = mBooksList.size();
        }
        List<BookListDetailBean.BooksBean.BookBean> books = new ArrayList<>(limit);
        for (int i=start; i < end; ++i){
            books.add(mBooksList.get(i).getBook());
        }
        return books;
    }

    @Override
    public void showError() {
        mRefreshLayout.showError();
    }

    @Override
    public void complete() {
        mRefreshLayout.showFinish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_DETAIL_ID, mDetailId);
    }


    class DetailHeader implements WholeAdapter.ItemView{
        @BindView(R.id.book_list_info_tv_title)
        TextView tvTitle;
        @BindView(R.id.book_list_detail_tv_desc)
        TextView tvDesc;
        @BindView(R.id.book_list_info_iv_cover)
        ImageView ivPortrait;
        @BindView(R.id.book_list_detail_tv_create)
        TextView tvCreate;
        @BindView(R.id.book_list_info_tv_author)
        TextView tvAuthor;
        @BindView(R.id.book_list_detail_tv_share)
        TextView tvShare;

        BookListDetailBean detailBean;

        Unbinder detailUnbinder = null;
        @Override
        public View onCreateView(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_book_list_detail,parent,false);
            return view;
        }

        @Override
        public void onBindView(View view) {
            if (detailUnbinder == null){
                detailUnbinder = ButterKnife.bind(this,view);
            }
            //如果没有值就直接返回
            if (detailBean == null){
                return;
            }
            //标题
            tvTitle.setText(detailBean.getTitle());
            //描述
            tvDesc.setText(detailBean.getDesc());
            //头像
            Glide.with(App.getContext())
                    .load(Constant.IMG_BASE_URL+detailBean.getAuthor().getAvatar())
                    .placeholder(R.drawable.ic_loadding)
                    .error(R.drawable.ic_load_error)
                    .transform(new CircleTransform(App.getContext()))
                    .into(ivPortrait);
            //作者
            tvAuthor.setText(detailBean.getAuthor().getNickname());
        }

        public void setBookListDetail(BookListDetailBean bean){
            detailBean = bean;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDetailHeader.detailUnbinder != null){
            mDetailHeader.detailUnbinder.unbind();
        }
    }
}
