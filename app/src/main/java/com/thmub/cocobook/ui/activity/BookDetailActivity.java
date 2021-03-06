package com.thmub.cocobook.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.OnClick;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thmub.cocobook.R;
import com.thmub.cocobook.base.BaseMVPActivity;
import com.thmub.cocobook.model.bean.*;
import com.thmub.cocobook.model.local.BookRepository;
import com.thmub.cocobook.presenter.BookDetailPresenter;
import com.thmub.cocobook.presenter.contract.BookDetailContract;
import com.thmub.cocobook.ui.adapter.BookListAdapter;
import com.thmub.cocobook.ui.adapter.LabelAdapter;
import com.thmub.cocobook.ui.adapter.RecommendBookAdapter;
import com.thmub.cocobook.utils.Constant;
import com.thmub.cocobook.utils.ToastUtils;
import com.thmub.cocobook.utils.UiUtils;
import com.thmub.cocobook.widget.RefreshLayout;
import com.thmub.cocobook.widget.itemdecoration.DividerGridItemDecoration;
import com.thmub.cocobook.widget.itemdecoration.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhouas666 on 18-1-23.
 * 书籍详情activity
 */

public class BookDetailActivity extends BaseMVPActivity<BookDetailContract.Presenter>
        implements BookDetailContract.View {
    /************************************Constant************************************/
    public static final String RESULT_IS_COLLECTED = "result_is_collected";
    private static final String EXTRA_BOOK_ID = "extra_book_id";
    private static final String EXTRA_BOOK_TITLE = "extra_book_title";
    private static final int REQUEST_READ = 1;

    /************************************View************************************/
    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.book_detail_iv_cover)
    ImageView mIvCover;
    @BindView(R.id.book_detail_tv_author)
    TextView mTvAuthor;
    @BindView(R.id.book_detail_tv_type)
    TextView mTvType;
    @BindView(R.id.book_detail_tv_word_count)
    TextView mTvWordCount;
    @BindView(R.id.book_detail_tv_add)
    TextView mTvAddBook;
    @BindView(R.id.book_detail_tv_open)
    TextView mTvOpenBook;
    @BindView(R.id.book_detail_rv_label)
    RecyclerView mRvLabel;
    @BindView(R.id.book_detail_tv_brief)
    TextView mTvBrief;
    @BindView(R.id.book_list_tv_recommend_books)
    TextView mTvRecommendBooks;
    @BindView(R.id.book_detail_rv_recommend_books)
    RecyclerView mRvRecommendBooks;
    @BindView(R.id.book_list_tv_recommend_book_list)
    TextView mTvRecommendBookList;
    @BindView(R.id.book_detail_rv_recommend_book_list)
    RecyclerView mRvRecommendBookList;

    private LabelAdapter mLabelAdapter;
    private RecommendBookAdapter mBooksAdapter;
    private BookListAdapter mBookListAdapter;
    private CollBookBean mCollBookBean;
    private ProgressDialog mProgressDialog;

    /************************************Variable************************************/
    private String mBookId;
    private String mBookTitle;
    private boolean isBriefOpen = false;
    private boolean isCollected = false;
    private boolean isTxt = false;

    /**********************************Public Method**********************************/
    public static void startActivity(Context context, String bookId, String bookTitle) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(EXTRA_BOOK_ID, bookId);
        intent.putExtra(EXTRA_BOOK_TITLE, bookTitle);
        context.startActivity(intent);
    }

    /************************************Initialization***********************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        //标签
        mLabelAdapter=new LabelAdapter();
        mRvLabel.setLayoutManager(new GridLayoutManager(mContext,6));
        mRvLabel.setAdapter(mLabelAdapter);

        //推荐如图书列表
        mBooksAdapter = new RecommendBookAdapter();
        mRvRecommendBooks.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRvRecommendBooks.addItemDecoration(new DividerGridItemDecoration(mContext
                , R.drawable.shape_divider_row, R.drawable.shape_divider_col));
        mRvRecommendBooks.setAdapter(mBooksAdapter);

        //推荐书单列表
        mBookListAdapter = new BookListAdapter();
        mRvRecommendBookList.setLayoutManager(new LinearLayoutManager(mContext));
        mRvRecommendBookList.addItemDecoration(new DividerItemDecoration(mContext));
        mRvRecommendBookList.setAdapter(mBookListAdapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (savedInstanceState != null) {
            mBookId = savedInstanceState.getString(EXTRA_BOOK_ID);
            mBookTitle = savedInstanceState.getString(EXTRA_BOOK_TITLE);
        } else {
            mBookId = getIntent().getStringExtra(EXTRA_BOOK_ID);
            mBookTitle = getIntent().getStringExtra(EXTRA_BOOK_TITLE);
        }
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().setTitle(mBookTitle);
    }

    @Override
    protected void initClick() {
        super.initClick();
        //监听推荐图书
        mBooksAdapter.setOnItemClickListener((view, pos) -> {
            BookDetailActivity.startActivity(mContext,
                    mBooksAdapter.getItem(pos).get_id(), mBooksAdapter.getItem(pos).getTitle());
        });
        //监听推荐书单
        mBookListAdapter.setOnItemClickListener((view, pos) -> {
            BookListDetailActivity.startActivity(mContext, mBookListAdapter.getItem(pos).getId());
        });
    }

    /************************************Transaction************************************/
    @Override
    protected BookDetailContract.Presenter bindPresenter() {
        return new BookDetailPresenter();
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mRefreshLayout.showLoading();
        mPresenter.refreshBookDetail(mBookId);
    }

    /**
     * 书籍详情
     *
     * @param bean
     */
    @Override
    public void finishRefresh(BookDetailBean bean) {
        //封面
        Glide.with(this)
                .load(Constant.IMG_BASE_URL + bean.getCover())
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.ic_default_book_cover)
                        .error(R.mipmap.ic_load_error)
                        .centerCrop())
                .into(mIvCover);
        //作者
        mTvAuthor.setText(bean.getAuthor());
        //类型
        mTvType.setText(bean.getMajorCate());
        //总字数
        mTvWordCount.setText(getResources().getString(R.string.book_word_count, bean.getWordCount() / 10000));
        //Tags
        mLabelAdapter.addItems(bean.getTags());
        //简介
        mTvBrief.setText(bean.getLongIntro());

        mCollBookBean = BookRepository.getInstance().getCollBook(bean.get_id());

        //判断是不是txt格式,默认false
        if (bean.getContentType().equals("txt"))
            isTxt = true;

        //判断是否收藏
        if (mCollBookBean != null) {
            isCollected = true;
            mTvAddBook.setText(UiUtils.getString(R.string.book_detail_give_up));
            mTvOpenBook.setText(UiUtils.getString(R.string.book_detail_go_on));
        } else {
            mCollBookBean = bean.getCollBookBean();
        }
    }


    /**
     * 推荐图书
     *
     * @param beans
     */
    @Override
    public void finishRecommendBooks(List<RankBookBean> beans) {
        if (beans.isEmpty()) return;
        mBooksAdapter.addItems(beans.subList(0, 6));
    }

    /**
     * 推荐书单
     *
     * @param beans
     */
    @Override
    public void finishRecommendBookList(List<BookListBean> beans) {
        if (beans.isEmpty()) {
            mTvRecommendBookList.setVisibility(View.GONE);
            return;
        }
        mBookListAdapter.addItems(beans);
    }

    @Override
    public void waitToBookShelf() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("正在添加到书架中");
        }
        mProgressDialog.show();
    }

    @Override
    public void errorToBookShelf() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        ToastUtils.show("加入书架失败，请检查网络");
    }

    @Override
    public void succeedToBookShelf() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        ToastUtils.show("加入书架成功");
    }

    @Override
    public void showError() {
        mRefreshLayout.showError();
    }

    @Override
    public void complete() {
        mRefreshLayout.showFinish();
    }

    /**************************Event*****************************/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_BOOK_ID, mBookId);
    }

    /**
     * 处理返回事件
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果进入阅读页面收藏了，就需要返回改变收藏按钮
        if (requestCode == REQUEST_READ) {
            if (data == null) return;

            isCollected = data.getBooleanExtra(RESULT_IS_COLLECTED, false);

            if (isCollected) {
                mTvAddBook.setText(UiUtils.getString(R.string.book_detail_give_up));
                mTvOpenBook.setText(UiUtils.getString(R.string.book_detail_go_on));
            }
        }
    }

    /**
     * 监听点击事件
     *
     * @param view
     */
    @OnClick({R.id.book_detail_tv_brief, R.id.fl_add_bookcase,
            R.id.fl_open_book, R.id.book_detail_tv_more_books})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.book_detail_tv_brief: //展开简介
                if (isBriefOpen) {
                    mTvBrief.setMaxLines(4);
                    isBriefOpen = false;
                } else {
                    mTvBrief.setMaxLines(10);
                    isBriefOpen = true;
                }
                break;
            case R.id.fl_add_bookcase:  //加入书架
                addShelf();
                break;
            case R.id.fl_open_book:  //开始阅读
                if (isTxt) {
                    startActivityForResult(new Intent(this, ReadActivity.class)
                            .putExtra(ReadActivity.EXTRA_IS_COLLECTED, isCollected)
                            .putExtra(ReadActivity.EXTRA_COLL_BOOK, mCollBookBean), REQUEST_READ);
                } else {
                    ToastUtils.show("暂不支持本书格式");
                }
                break;
            case R.id.book_detail_tv_more_books:  //更多推荐图书
                RecommendBookActivity.startActivity(mContext, mBookId);
                break;
        }
    }

    /**
     * 追更：加入书架
     */
    private void addShelf() {
        if (isCollected) {
            //放弃点击
            BookRepository.getInstance().deleteCollBookInRx(mCollBookBean);
            mTvAddBook.setText(UiUtils.getString(R.string.book_detail_add_bookcase));
            mTvOpenBook.setText(UiUtils.getString(R.string.book_detail_open_book));
            isCollected = false;
        } else {
            mPresenter.addToBookShelf(mCollBookBean);
            mTvAddBook.setText(UiUtils.getString(R.string.book_detail_give_up));
            mTvOpenBook.setText(UiUtils.getString(R.string.book_detail_go_on));
            isCollected = true;
        }
    }

}
