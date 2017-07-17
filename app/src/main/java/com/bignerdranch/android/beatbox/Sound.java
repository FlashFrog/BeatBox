package com.bignerdranch.android.beatbox;

/**
 * Created by Leo on 2017/7/17.
 */

public class Sound {

    private String mAssetPath;
    private String mName;
    private Integer mSoundId;//这里使用的类型是Integer而不是int 这样在Sound的mSoundId没有值时可以设置其为null值。

    public Sound(String assetPath){
        mAssetPath = assetPath;
        //分理出文件名
        String[] components = assetPath.split("/");
        String filename = components[components.length - 1];
        //删除.wav后缀
        mName = filename.replace(".wav","");
    }

    public String getAssetPath(){
        return  mAssetPath;
    }

    public String getName(){
        return mName;
    }

    public Integer getSoundId() {
        return mSoundId;
    }

    public void setSoundId(Integer soundId) {
        mSoundId = soundId;
    }
}
