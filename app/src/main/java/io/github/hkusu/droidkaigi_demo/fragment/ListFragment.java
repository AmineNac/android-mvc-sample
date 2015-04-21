package io.github.hkusu.droidkaigi_demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;
import io.github.hkusu.droidkaigi_demo.R;
import io.github.hkusu.droidkaigi_demo.common.Const;
import io.github.hkusu.droidkaigi_demo.common.FragmentManager;
import io.github.hkusu.droidkaigi_demo.common.ModelManager;
import io.github.hkusu.droidkaigi_demo.event.QiitaItemLoadedEvent;
import io.github.hkusu.droidkaigi_demo.model.QiitaItemEntity;
import io.github.hkusu.droidkaigi_demo.model.QiitaItemModel;

public class ListFragment extends Fragment {

    @InjectView(R.id.itemCountTextView)
    TextView mItemCountTextView;
    @InjectView(R.id.qiitaItemListView)
    ListView mQiitaItemListView;

    private List<QiitaItemEntity> mQiitaItemList;
    private QiitaItemListAdapter mQiitaItemListAdapter;

    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (getArguments() != null) {
        //    //引数があれば受け取り
        //}
        mQiitaItemList = ((QiitaItemModel) ModelManager.get(ModelManager.ModelList.QIITA_ITEM)).getItemList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.inject(this, view); // ButterKnife
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mQiitaItemListAdapter = new QiitaItemListAdapter(
                getActivity(),
                R.layout.adapter_qiita_item_list,
                mQiitaItemList
        );
        mQiitaItemListView.setAdapter(mQiitaItemListAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((QiitaItemModel) ModelManager.get(ModelManager.ModelList.QIITA_ITEM)).load();
        updateView();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this); // EventBus
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this); // EventBus
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this); // ButterKnife
    }

    // ListView 中の項目選択
    @SuppressWarnings("unused")
    @OnItemClick(R.id.qiitaItemListView)
    public void onItemClickQiitaItemListView(int position) {
        FragmentManager fragmentManager = new FragmentManager(getActivity(), R.id.container);
        Bundle args = new Bundle();
        args.putString(Const.BundleKey.URL.toString(), mQiitaItemList.get(position).url);
        fragmentManager.replace(FragmentManager.FragmentList.DETAIL, args, FragmentManager.Animation.SLIDE_IN_BOTTOM);
    }

    // EventBus からの通知
    @SuppressWarnings("unused")
    public void onEventMainThread(QiitaItemLoadedEvent event) {
        if (event.isSuccess()) {
            updateView();
        }
    }

    // Viewの表示を更新するプライベートメソッド
    private void updateView() {
        mItemCountTextView.setText(((QiitaItemModel)ModelManager.get(ModelManager.ModelList.QIITA_ITEM)).getItemCount() + " 件");
        mQiitaItemListAdapter.notifyDataSetChanged();
    }
}
