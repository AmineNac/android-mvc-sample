package io.github.hkusu.droidkaigi_demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import io.github.hkusu.droidkaigi_demo.R;
import io.github.hkusu.droidkaigi_demo.common.ModelList;
import io.github.hkusu.droidkaigi_demo.common.ModelManager;
import io.github.hkusu.droidkaigi_demo.event.QiitaItemLoadedEvent;
import io.github.hkusu.droidkaigi_demo.model.QiitaItemEntity;
import io.github.hkusu.droidkaigi_demo.model.QiitaItemModel;

public class ListFragment extends Fragment {

    @InjectView(R.id.button)
    Button mButton;
    @InjectView(R.id.imageView)
    ImageView mImageView;

    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //引数があれば受け取り
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_list, container, false);
        ButterKnife.inject(this, view); // ButterKnife
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Picasso.with(getActivity())
                .load("http://i.imgur.com/DvpvklR.png")
                .resize(200, 200)
                .centerCrop()
                .into(mImageView);
    }

    @Override
    public void onStart() {
        super.onStart();

        ((QiitaItemModel) ModelManager.get(ModelList.QIITA_ITEM)).load();
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

    // ボタンクリック
    @SuppressWarnings("unused")
    @OnClick(R.id.button)
    public void onClickButton(View view) {
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

        List<QiitaItemEntity> list = ((QiitaItemModel) ModelManager.get(ModelList.QIITA_ITEM)).get();

        for (QiitaItemEntity qiitaItemEntity : list) {
            Log.i("qiita", "id=" + qiitaItemEntity.id
                            + " uuid=" + qiitaItemEntity.uuid
                            + " title=" + qiitaItemEntity.title
                            + " url=" + qiitaItemEntity.url
                            + " userId=" + qiitaItemEntity.user.id
                            + " usrUrlName=" + qiitaItemEntity.user.urlName
                            + " userProfileImageUrl=" + qiitaItemEntity.user.profileImageUrl
            );
        }

        //TODO TextVeiwとadapterの更新
        //mFloorListAdapter.notifyDataSetChanged();
    }
}

//TODO Adapter(別ファイル)、独自リスナクラス
