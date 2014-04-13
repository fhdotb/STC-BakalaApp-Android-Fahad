package sa.com.stc.SimRegister;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alfaifi on 1/18/14.
 */
public class AnimatingButton extends Button
{
    private List<Animation> mAnimationBuffer = new ArrayList<Animation>();

    private boolean mIsAnimating;

    AnimationCoordinations touchDownCoordinations = new AnimationCoordinations(0.9f, 0.9f);
    AnimationCoordinations touchUpCoordinations = new AnimationCoordinations(0.9f, 1);

    private class AnimationCoordinations
    {
        private float from;
        private float to;

        private AnimationCoordinations(float from, float to)
        {
            this.from = from;
            this.to = to;
        }

        public float getFrom()
        {
            return from;
        }

        public void setFrom(float from)
        {
            this.from = from;
        }

        public float getTo()
        {
            return to;
        }

        public void setTo(float to)
        {
            this.to = to;
        }
    }

    public AnimatingButton(Context context)
    {
        super(context);
    }

    public AnimatingButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public AnimatingButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void fitAnimation(AnimatingButton button)
    {
        if (button.getLayoutParams().width > 150 || button.getLayoutParams().width == button.getLayoutParams().MATCH_PARENT)
        {
            touchDownCoordinations = new AnimationCoordinations(0.95f, 0.95f);
            touchUpCoordinations = new AnimationCoordinations(0.95f, 1);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            generateAnimation(touchDownCoordinations);
            triggerNextAnimation();
        }
        else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP)
        {
            generateAnimation(touchUpCoordinations);
            triggerNextAnimation();
        }

        return super.onTouchEvent(event);
    }

    private void generateAnimation(AnimationCoordinations ac)
    {
        ScaleAnimation scaleAnimation = new ScaleAnimation(ac.getFrom(), ac.getTo(), ac.getFrom(), ac.getTo(), Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(250);
        scaleAnimation.setAnimationListener(new ScaleAnimation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                mIsAnimating = false;
                triggerNextAnimation();
            }
        });

        mAnimationBuffer.add(scaleAnimation);
    }

    private void triggerNextAnimation()
    {
        if (mAnimationBuffer.size() > 0 && !mIsAnimating)
        {
            mIsAnimating = true;
            Animation currAnimation = mAnimationBuffer.get(0);
            mAnimationBuffer.remove(0);

            startAnimation(currAnimation);
        }
    }

}