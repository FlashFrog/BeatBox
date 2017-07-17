package com.bignerdranch.android.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2017/7/17.
 */

public class BeatBox {

    //用于日志记录
    private static final String TAG = "BeatBox";

    //用于存储声音资源文件目录名
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    //访问assets需要用到AssetManager类，可以从context中获取到它，这里添加一个带Contex参数的构造函数获取并留存它。
    private AssetManager mAssets;

    //创建一个Sound列表
    private List<Sound> mSounds = new ArrayList<>();

    private SoundPool mSoundPool;

    public BeatBox(Context context){
        /*
         * 访问assets时,可以不用关心究竟使用哪个Contex对象，
         * 而且在实际开发的任何场景下，所有Context中的AssetManager管理的都是同一套assets资源。
         */
        mAssets = context.getAssets();
        /*
         * Lollipop引入了新的方式创建SoundPool：使用SoundPool.Builder。为了兼容api 16最低级别，只能选择使用SoundPool（int int int）这个老构造方法。
         * 第一个参数指定同时播放多少个音频，这里指定了5个。在播放5个音频时，如果在尝试播放第6个，SoundPool会停止播放原来的音频。
         * 第二个参数确定音频流类型。
         * 最后一个参数指定采样率转换品质。 不起作用 ，传入 0；
         */
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    private void loadSounds(){
        String[] soundNames;
        try{
            //list(S)方法取得assets中的资源清单。能够列出指定目录中的所有文件名。
            //只要传入声音资源所在的目录，就能看到其中所有的.wav文件。
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found "+ soundNames.length + " sounds");
        }catch (IOException ioe){
            Log.e(TAG, "Could not list assets",ioe );
            return;
        }

        for(String fileName : soundNames){
            try{
                String assetPath = SOUNDS_FOLDER + "/" + fileName;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            }catch (IOException ioe){
                Log.e(TAG, "Could not load sound" + fileName, ioe);
            }
        }
    }

    public  List<Sound> getSounds(){
        return mSounds;
    }

    private void load(Sound sound)throws IOException{
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        //调用load(AssetFileDescriptor, int)方法可以把文件载入SoundPool待播、
        //此方法返回一个int性ID。这就是存储在mSoundId中的ID。
        int soundId = mSoundPool.load(afd,1);
        sound.setSoundId(soundId);
    }

    public void play(Sound sound){
        Integer soundId = sound.getSoundId();
        //先检查并确保soundId是不是null值，Sound加载失败会导致soundId出现null值。
        if(soundId == null)
            return;
        /*
        * 调用play方法播放音频。
        * 这些参数依次是：音频ID，左音量，右音量，优先级（无效）,是否循环以及播放速率。
        * 我们需要最大音量和长速播放，所以传入1.0，是否循环参数传入0值，代表不循环。 -1代表循环。
         */
        mSoundPool.play(soundId,1.0f,1.0f,1,0,1.0f);
    }

    public void release(){
        mSoundPool.release(); //释放音频
    }
}
