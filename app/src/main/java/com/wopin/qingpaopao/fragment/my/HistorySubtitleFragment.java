package com.wopin.qingpaopao.fragment.my;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.ExploreListRsp;
import com.wopin.qingpaopao.fragment.ExploreListNorFragment;
import com.wopin.qingpaopao.model.ExploreModel;

import io.reactivex.Observable;

public class HistorySubtitleFragment extends ExploreListNorFragment {

    @Override
    protected String setBarTitle() {
        return getString(R.string.history_list);
    }

    @Override
    public Observable<ExploreListRsp> getExploreListObservable(int page, int number) {
        ExploreModel exploreModel = new ExploreModel();
        return exploreModel.listHistoryExplores(page, number);
    }
}
