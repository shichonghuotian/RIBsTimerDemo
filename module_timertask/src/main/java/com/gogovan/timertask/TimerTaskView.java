package com.gogovan.timertask;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.uber.rib.core.Initializer;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Top level view for {@link TimerTaskBuilder.TimeTaskScope}.
 */
class TimerTaskView extends LinearLayout implements TimerTaskInteractor.TimeTaskPresenter {

    public TimerTaskView(Context context) {
        this(context, null);
    }

    public TimerTaskView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimerTaskView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    private EditText mTaskNameEditText;
    private TextView mTimeTextView;
    private Button mStartButton;
    private Button mUpButton;
    private Button mDownButton;
    private Button mResetButton;


    /**
     * 取消点击时使用
     */
    private PublishSubject<Object> mTouchCancelSubject;

    @Initializer
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTaskNameEditText = findViewById(R.id.task_name_edit);
        mTimeTextView = findViewById(R.id.time_count_text);
        mStartButton = findViewById(R.id.start_button);

        mUpButton = findViewById(R.id.up_button);
        mDownButton = findViewById(R.id.down_button);
        mResetButton = findViewById(R.id.reset_button);
        mTouchCancelSubject = PublishSubject.create();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    /**
     * 重写touch分发事件，处理长按后手指松开不能监听的问题
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent
                .ACTION_CANCEL) {
            mTouchCancelSubject.onNext(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public Observable<Object> upRequest() {

        return RxView.clicks(mUpButton)
                .throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Object> upLongRequest() {
        return RxView.longClicks(mUpButton)
                .subscribeOn(AndroidSchedulers
                .mainThread());
    }

    @Override
    public Observable<Object> touchupObservable() {
        return mTouchCancelSubject;
    }




    @Override
    public Observable downRequest() {


        return RxView.clicks(mDownButton)
                .throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Object> downLongRequest() {
        return RxView.longClicks(mDownButton)
                .subscribeOn(AndroidSchedulers
                        .mainThread());
    }

    @Override
    public Observable<Object> resetRequest() {
        return RxView.clicks(findViewById(R.id.reset_button)).subscribeOn(AndroidSchedulers
                .mainThread());
    }


    @Override
    public String getTimerTaskName() {
        return mTaskNameEditText.getText().toString().trim();
    }

    @Override
    public Observable<Object> startRequest() {
        return RxView.clicks(mStartButton)
                .throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void setTimeText(String text) {

        mTimeTextView.setText(text);
    }

    @Override
    public void chageStartButtonStatus(int textResId) {

        mStartButton.setText(textResId);

    }

    @Override
    public void resetTimerTask() {

        mTaskNameEditText.setText("");

        setUpAndDownEabled(true);
        showResetButton(false);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEditTextError(String text) {
        mTaskNameEditText.setError(text);
    }

    @Override
    public void setUpAndDownEabled(boolean enabled) {

        mUpButton.setEnabled(enabled);
        mDownButton.setEnabled(enabled);
        mTaskNameEditText.setEnabled(enabled);

    }

    @Override
    public void showResetButton(boolean show) {
        mResetButton.setVisibility(show? View.VISIBLE: View.INVISIBLE);
    }


}
