package com.tuansbook.lvxing.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuansbook.lvxing.R;

/**
 * Created by Administrator on 2017/3/9
 * 全部帖子
 */
public class MyTieZiFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =inflater.inflate(R.layout.test,container,false);

        return view;
    }
}
