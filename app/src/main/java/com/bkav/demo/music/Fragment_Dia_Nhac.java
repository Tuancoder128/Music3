package com.bkav.demo.music;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vst on 17/06/2018.
 */

public class Fragment_Dia_Nhac extends Fragment {

    View mView;
    CircleImageView mCircleImageView;
    ObjectAnimator mObjectAnimator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_dia_nhac, container, false);
        mCircleImageView = mView.findViewById(R.id.circleimageview);
        mObjectAnimator = ObjectAnimator.ofFloat(mCircleImageView,"rotation",0f,360f);
        mObjectAnimator.setDuration(10000);
        mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimator.setInterpolator(new LinearInterpolator());
        

        return mView;
    }
}
