package com.thmub.cocobook.presenter.contract;

import com.thmub.cocobook.model.bean.PageNodeDetailBean;
import com.thmub.cocobook.base.BaseContract;

import java.util.List;

/**
 * Created by zhouas666 on 18-1-23.
 */

public interface PageNodeContract {
    interface View extends BaseContract.BaseView{
        void finishRefresh(List<PageNodeDetailBean> beans);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void refreshFeatureDetail(String nodeId);
    }
}
