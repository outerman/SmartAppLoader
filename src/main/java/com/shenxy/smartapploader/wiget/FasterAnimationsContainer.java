package com.shenxy.smartapploader.wiget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.widget.ImageView;

import com.shenxy.smartapploader.utils.ThreadPoolUtil;

import java.lang.ref.SoftReference;
import java.util.ArrayList;


public class FasterAnimationsContainer {
    private class AnimationFrame{
        private int mResourceId;
        private int mDuration;
        AnimationFrame(int resourceId, int duration){
            mResourceId = resourceId;
            mDuration = duration;
        }
        public int getResourceId() {
            return mResourceId;
        }
        public int getDuration() {
            return mDuration;
        }
    }
    private ArrayList<AnimationFrame> mAnimationFrames; // list for all frames of animation
    private int mIndex; // index of current frame

    private boolean mShouldRun; // true if the animation should continue running. Used to stop the animation
    private boolean mIsRunning; // true if the animation prevents starting the animation twice
    private SoftReference<ImageView> mSoftReferenceImageView; // Used to prevent holding ImageView when it should be dead.
    private Handler mHandler; // Handler to communication with UIThread

    // Listeners
    private OnAnimationStoppedListener mOnAnimationStoppedListener;
    private OnAnimationFrameChangedListener mOnAnimationFrameChangedListener;

    public FasterAnimationsContainer(ImageView imageView) {
        init(imageView);
    };

    // single instance procedures
//    private static FasterAnimationsContainer sInstance;

//    public static FasterAnimationsContainer getInstance(ImageView imageView) {
//        if (sInstance == null)
//            sInstance = new FasterAnimationsContainer(imageView);
//        return sInstance;
//    }

    /**
     * initialize imageview and frames
     * @param imageView
     */
    public void init(ImageView imageView){
        mAnimationFrames = new ArrayList<AnimationFrame>();
        mSoftReferenceImageView = new SoftReference<ImageView>(imageView);

        mHandler = new Handler();
        if(mIsRunning == true){
            stop();
        }

        mShouldRun = false;
        mIsRunning = false;

        mIndex = -1;
    }

    /**
     * add a frame of animation
     * @param index index of animation
     * @param resId resource id of drawable 
     * @param interval milliseconds 
     */
    public void addFrame(int index, int resId, int interval){
        mAnimationFrames.add(index, new AnimationFrame(resId, interval));
    }

    /**
     * add a frame of animation
     * @param resId resource id of drawable 
     * @param interval milliseconds
     */
    public void addFrame(int resId, int interval){
        mAnimationFrames.add(new AnimationFrame(resId, interval));
    }

    /**
     * add all frames of animation
     * @param resIds resource id of drawable
     * @param interval milliseconds
     */
    public void addAllFrames(int[] resIds, int interval){
        for(int resId : resIds){
            mAnimationFrames.add(new AnimationFrame(resId, interval));
        }
    }

    /**
     * remove a frame with index
     * @param index index of animation
     */
    public void removeFrame(int index){
        mAnimationFrames.remove(index);
    }

    /**
     * clear all frames
     */
    public void removeAllFrames(){
        mAnimationFrames.clear();
    }

    /**
     * change a frame of animation
     * @param index index of animation
     * @param resId resource id of drawable 
     * @param interval milliseconds
     */
    public void replaceFrame(int index, int resId, int interval){
        mAnimationFrames.set(index, new AnimationFrame(resId, interval));
    }

    private AnimationFrame getNext() {
        mIndex++;
        if (mIndex >= mAnimationFrames.size()){
            if (isOneShot){     //shenxy 如果只播放一次，则播放到最后一帧以后，返回null
                return null;
            }
            else {
                mIndex = 0;
            }
        }

        return mAnimationFrames.get(mIndex);
    }
    
    /**
     * Listener of animation to detect stopped
     *
     */
    public interface OnAnimationStoppedListener{
        public void onAnimationStopped();
    }
    
    /**
     * Listener of animation to get index
     *
     */
    public interface OnAnimationFrameChangedListener{
        public void onAnimationFrameChanged(int index);
    }


    /**
     * set a listener for OnAnimationStoppedListener
     * @param listener OnAnimationStoppedListener
     */
    public void setOnAnimationStoppedListener(OnAnimationStoppedListener listener){
        mOnAnimationStoppedListener = listener;
    }

    /**
     * set a listener for OnAnimationFrameChangedListener
     * @param listener OnAnimationFrameChangedListener
     */
    public void setOnAnimationFrameChangedListener(OnAnimationFrameChangedListener listener){
        mOnAnimationFrameChangedListener = listener;
    }

    /**
     * Starts the animation
     */
    private Runnable framesSequenceAnimationRunnable;
    public synchronized void start() {
        mShouldRun = true;
        if (mIsRunning)
            return;

        //shenxy 考虑非循环播放的情况
        mIndex = -1;

        //shenxy 增加启动延时
        if (framesSequenceAnimationRunnable == null) {
            framesSequenceAnimationRunnable = new FramesSequenceAnimation();
        }
        mHandler.removeCallbacks(framesSequenceAnimationRunnable);
        if (startDelay > 0){
            mHandler.postDelayed(framesSequenceAnimationRunnable, startDelay);
        }
        else {
            mHandler.post(framesSequenceAnimationRunnable);
        }
    }

    /**
     * Stops the animation
     */
    public synchronized void stop() {
        mShouldRun = false;
    }

    private class FramesSequenceAnimation implements Runnable{

        @Override
        public void run() {
            ImageView imageView = mSoftReferenceImageView.get();
            if (!mShouldRun || imageView == null) {
                mIsRunning = false;
                if (mOnAnimationStoppedListener != null) {
                    mOnAnimationStoppedListener.onAnimationStopped();
                }
                return;
            }
            mIsRunning = true;

            if (imageView.isShown()) {
                AnimationFrame frame = getNext();
                if (frame == null){ //shenxy 如果没有下一帧了，就结束
                    mIsRunning = false;

                    if (mOnAnimationStoppedListener != null)
                        mOnAnimationStoppedListener.onAnimationStopped();
                    return;
                }
                GetImageDrawableTask task = new GetImageDrawableTask(imageView, this, frame.getDuration());
                task.executeOnExecutor(ThreadPoolUtil.getInstance().getExecutorServiceForImage(), frame.getResourceId());
            }
            else {
                mIsRunning = false;
            }
        }
    }

    private class GetImageDrawableTask extends AsyncTask<Integer, Void, Drawable>{

        private ImageView mImageView;
        private Runnable nextRunnable;
        private int nextDuration;
        private Context context;

        public GetImageDrawableTask(ImageView imageView, FramesSequenceAnimation framesSequenceAnimation, int duration) {
            mImageView = imageView;
            nextRunnable = framesSequenceAnimation;
            nextDuration = duration;
            context = mImageView.getContext();
        }

        @Override
        protected Drawable doInBackground(Integer... params) {
            try {
                BitmapFactory.Options defaultOptions = new BitmapFactory.Options();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    defaultOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
                } else {
                    defaultOptions.inPreferredConfig = Bitmap.Config.ARGB_4444;
                }

                currentBitmap = BitmapFactory.decodeResource(context.getResources(), params[0], defaultOptions);
                return new BitmapDrawable(context.getResources(), currentBitmap);
            }
            catch (OutOfMemoryError error) {
                error.printStackTrace();

                return null;
            }
        }

        @Override
        protected void onPostExecute(Drawable result) {
            super.onPostExecute(result);
            if(result!=null) {
                mImageView.setImageDrawable(result);
            }
            if (mOnAnimationFrameChangedListener != null) {
                mOnAnimationFrameChangedListener.onAnimationFrameChanged(mIndex);
            }

            mHandler.postDelayed(nextRunnable, nextDuration);
        }

    }


    //shenxy
    private Bitmap currentBitmap;
    public void releaseBitmap(){
        if (currentBitmap != null){
            currentBitmap.recycle();
            currentBitmap = null;
        }
    }

    public boolean isRunning() {
        return mIsRunning;
    }

    private long startDelay = -1; //启动延时

    public void setStartDelay(long startDelay){
        this.startDelay = startDelay;
    }

    private boolean isOneShot = false; //是否只播放一次   默认false

    public void setOneShot(boolean isOneShot){
        this.isOneShot = isOneShot;
    }

}
