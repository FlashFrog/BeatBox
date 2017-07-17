package com.bignerdranch.android.beatbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

/**
 * Created by Leo on 2017/7/17.
 */

public class BeatBoxFragment extends Fragment {

    private BeatBox mBeatBox;

    private Sound mSound;

    public static BeatBoxFragment newInstance(){
        return new BeatBoxFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mBeatBox = new BeatBox(getActivity());
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_beat_box, container, false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.fragment_beat_box_recycler_view);
        //以网格样式布局,每行显示3个。
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setAdapter(new SoundAdapter(mBeatBox.getSounds()));
        return view;
    }


    private class SoundHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Button mButton;

        public SoundHolder(LayoutInflater inflater, ViewGroup container){
            super(inflater.inflate(R.layout.list_item_sound,container,false));

            mButton = (Button)itemView.findViewById(R.id.list_item_sound_button);
            mButton.setOnClickListener(this);
        }

        public void bindSound(Sound sound){
            mSound = sound;
            mButton.setText(mSound.getName());
        }

        @Override
        public void onClick(View v){
            mBeatBox.play(mSound);
        }
    }


    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder>{

        private List<Sound> mSounds;
        //将SoundAdapter与Sound数组关联起来
        public SoundAdapter(List<Sound> sounds){
            mSounds = sounds;
        }
        @Override
        public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new SoundHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(SoundHolder soundHolder, int position) {
            Sound sound = mSounds.get(position);
            soundHolder.bindSound(sound);
        }

        @Override
        public int getItemCount() {
            return mSounds.size();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mBeatBox.release();
    }
}
