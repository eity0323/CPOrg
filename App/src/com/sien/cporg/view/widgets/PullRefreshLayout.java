package com.sien.cporg.view.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.sien.cporg.R;

public class PullRefreshLayout extends ViewGroup {

	private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
	private static final int DRAG_MAX_DISTANCE = 64;
	private static final int INVALID_POINTER = -1;
	private static final float DRAG_RATE = .5f;

	public static final int STYLE_MATERIAL = 0;
	public static final int STYLE_CIRCLES = 1;
	public static final int STYLE_WATER_DROP = 2;
	public static final int STYLE_RING = 3;
	public static final int MODE_TOP = 0;
	public static final int MODE_BOTTOM = 1;

	private View mTarget;
	private AbsListView mTargetAbsListView;
	private ImageView mRefreshView;
	private Interpolator mDecelerateInterpolator;
	private int mTouchSlop;
	private int mMediumAnimationDuration;
	private int mSpinnerFinalOffset;
	private int mTotalDragDistance;
	private RefreshDrawable mRefreshDrawable;
	private int mCurrentOffsetTop;
	private boolean mRefreshing;
	private int mActivePointerId;
	private boolean mIsBeingDragged;

	/**
	 * add by yafei.chen begin
	 */
	private boolean mIsBeingHorizonalScrooled;
	private float mInitialMotionX;
	private int vertical = 0;
	private int horizonal = 1;
	private int whichVerticalScrollFirst = -1;
	private boolean hasDispatched = false;
	/**
	 * add by yafei.chen end
	 */

	private float mInitialMotionY;
	private int mFrom;
	private boolean mNotify;
	private OnRefreshListener mListener;
	private int[] mColorSchemeColors;

	public PullRefreshLayout(Context context) {
		this(context, null);
	}

	public PullRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullRefreshLayout);
		final int colorsId = a.getResourceId(R.styleable.PullRefreshLayout_colors, R.array.google_colors);
		a.recycle();

		mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mMediumAnimationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);
		mSpinnerFinalOffset = mTotalDragDistance = dp2px(DRAG_MAX_DISTANCE);

		mRefreshView = new ImageView(context);
		mColorSchemeColors = context.getResources().getIntArray(colorsId);
		setRefreshStyle(null);
		mRefreshView.setVisibility(View.GONE);
		addView(mRefreshView);

		setWillNotDraw(false);
		ViewCompat.setChildrenDrawingOrderEnabled(this, true);
	}

	public void setColorSchemeColors(int[] colorSchemeColors) {
		mColorSchemeColors = colorSchemeColors;
		mRefreshDrawable.setColorSchemeColors(colorSchemeColors);
	}

	public void setRefreshStyle(RefreshDrawable refreshDrawable) {
		setRefreshing(false);
		
		if(refreshDrawable == null){
			mRefreshDrawable = new CirclesDrawable(getContext(), this);
		}
		
		mRefreshDrawable.setColorSchemeColors(mColorSchemeColors);
		mRefreshView.setImageDrawable(refreshDrawable);
	}

	public void setRefreshDrawable(RefreshDrawable drawable) {
		setRefreshing(false);
		mRefreshDrawable = drawable;
		mRefreshDrawable.setColorSchemeColors(mColorSchemeColors);
		mRefreshView.setImageDrawable(mRefreshDrawable);
	}

	public int getFinalOffset() {
		return mSpinnerFinalOffset;
	}

	/**
	 * 主动下拉刷新 delayMillis 建议200
	 */
	public void doPullDownRefresh(int delayMillis) {
		postDelayed(new Runnable() {
			@Override
			public void run() {

				mRefreshing = false;
				setRefreshing(true, true);
			}
		}, delayMillis);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		ensureTarget();
		if (mTarget == null)
			return;

		widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingRight() - getPaddingLeft(), MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
		mTarget.measure(widthMeasureSpec, heightMeasureSpec);
		mRefreshView.measure(widthMeasureSpec, heightMeasureSpec);
	}

	private void ensureTarget() {
		if (mTarget != null)
			return;
		if (getChildCount() > 0) {
			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);
				if (child != mRefreshView)
					mTarget = child;
			}
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		if (mRefreshing) {
			onTouchEventIntercepter(ev, mRefreshing);
		}

		if (!isEnabled() || canChildScrollUp() || mRefreshing) {
			return false;
		}
		return onTouchEventIntercepter(ev, false);

	}

	private boolean onTouchEventIntercepter(MotionEvent ev, boolean refreshing) {

		final int action = MotionEventCompat.getActionMasked(ev);

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			hasDispatched = false;
			if (!refreshing) {
				setTargetOffsetTop(0, true);
			}
			mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
			mIsBeingDragged = false;
			mIsBeingHorizonalScrooled = false;
			whichVerticalScrollFirst = -1;
			final float initialMotionX = getMotionEventX(ev, mActivePointerId);
			final float initialMotionY = getMotionEventY(ev, mActivePointerId);
			if (initialMotionY == -1) {
				return false;
			}
			mInitialMotionX = initialMotionX;
			mInitialMotionY = initialMotionY;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mActivePointerId == INVALID_POINTER) {
				return false;
			}

			/**
			 * add by yafei.chen
			 */
			final float x = getMotionEventX(ev, mActivePointerId);
			final float xDiff = x - mInitialMotionX;
			if (Math.abs(xDiff) > mTouchSlop && !mIsBeingHorizonalScrooled && whichVerticalScrollFirst == -1) {
				whichVerticalScrollFirst = horizonal;
				mIsBeingHorizonalScrooled = true;
			}
			/**
			 * end
			 */

			final float y = getMotionEventY(ev, mActivePointerId);
			if (y == -1) {
				return false;
			}
			final float yDiff = y - mInitialMotionY;
			if (yDiff > mTouchSlop && !mIsBeingDragged && whichVerticalScrollFirst == -1) {
				whichVerticalScrollFirst = vertical;
				mIsBeingDragged = true;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			whichVerticalScrollFirst = -1;
			mIsBeingHorizonalScrooled = false;
			mIsBeingDragged = false;
			mActivePointerId = INVALID_POINTER;
			break;
		case MotionEventCompat.ACTION_POINTER_UP:
			onSecondaryPointerUp(ev);
			break;
		}
		if (action == MotionEvent.ACTION_MOVE && !mIsBeingDragged && mIsBeingHorizonalScrooled && null != onHorizonalTouchEventListener && !hasDispatched) {
			onHorizonalTouchEventListener.onHorizonalTouchEvent();
			hasDispatched = true;
		}
		return mIsBeingDragged;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		if (!mIsBeingDragged) {
			return super.onTouchEvent(ev);
		}

		final int action = MotionEventCompat.getActionMasked(ev);

		switch (action) {
		case MotionEvent.ACTION_MOVE: {
			final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
			if (pointerIndex < 0) {
				return false;
			}

			final float y = MotionEventCompat.getY(ev, pointerIndex);
			final float yDiff = y - mInitialMotionY;
			final float scrollTop = yDiff * DRAG_RATE;
			float originalDragPercent = scrollTop / mTotalDragDistance;
			if (originalDragPercent < 0) {
				return false;
			}
			float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
			float extraOS = Math.abs(scrollTop) - mTotalDragDistance;
			float slingshotDist = mSpinnerFinalOffset;
			float tensionSlingshotPercent = Math.max(0, Math.min(extraOS, slingshotDist * 2) / slingshotDist);
			float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow((tensionSlingshotPercent / 4), 2)) * 2f;
			float extraMove = (slingshotDist) * tensionPercent * 2;
			int targetY = (int) ((slingshotDist * dragPercent) + extraMove);
			if (mRefreshView.getVisibility() != View.VISIBLE) {
				mRefreshView.setVisibility(View.VISIBLE);
			}
			if (scrollTop < mTotalDragDistance) {
				mRefreshDrawable.setPercent(dragPercent);
			}
			setTargetOffsetTop(targetY - mCurrentOffsetTop, true);
			break;
		}
		case MotionEventCompat.ACTION_POINTER_DOWN:
			final int index = MotionEventCompat.getActionIndex(ev);
			mActivePointerId = MotionEventCompat.getPointerId(ev, index);
			break;
		case MotionEventCompat.ACTION_POINTER_UP:
			onSecondaryPointerUp(ev);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (mActivePointerId == INVALID_POINTER) {
				return false;
			}
			final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
			final float y = MotionEventCompat.getY(ev, pointerIndex);
			final float overscrollTop = (y - mInitialMotionY) * DRAG_RATE;
			mIsBeingDragged = false;
			if (overscrollTop > mTotalDragDistance) {
				setRefreshing(true, true);
			} else {
				mRefreshing = false;
				animateOffsetToStartPosition();
			}
			mActivePointerId = INVALID_POINTER;
			return false;
		}
		return true;
	}

	private void animateOffsetToStartPosition() {
		mFrom = mCurrentOffsetTop;
		mAnimateToStartPosition.reset();
		mAnimateToStartPosition.setDuration(mMediumAnimationDuration);
		mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
		mAnimateToStartPosition.setAnimationListener(mToStartListener);
		mRefreshView.clearAnimation();
		mRefreshView.startAnimation(mAnimateToStartPosition);
	}

	private void animateOffsetToCorrectPosition() {
		mFrom = mCurrentOffsetTop;
		mAnimateToCorrectPosition.reset();
		mAnimateToCorrectPosition.setDuration(mMediumAnimationDuration);
		mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
		mAnimateToCorrectPosition.setAnimationListener(mRefreshListener);
		mRefreshView.clearAnimation();
		mRefreshView.startAnimation(mAnimateToCorrectPosition);
	}

	private final Animation mAnimateToStartPosition = new Animation() {
		@Override
		public void applyTransformation(float interpolatedTime, Transformation t) {
			moveToStart(interpolatedTime);
		}
	};

	private final Animation mAnimateToCorrectPosition = new Animation() {
		@Override
		public void applyTransformation(float interpolatedTime, Transformation t) {
			int targetTop = 0;
			int endTarget = (int) mSpinnerFinalOffset;
			targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
			int offset = targetTop - mTarget.getTop();
			setTargetOffsetTop(offset, false /* requires update */);
		}
	};

	private void moveToStart(float interpolatedTime) {
		int targetTop = mFrom - (int) (mFrom * interpolatedTime);
		int offset = targetTop - mTarget.getTop();
		setTargetOffsetTop(offset, false);
	}

	public void setRefreshing(boolean refreshing) {
		if (mRefreshing != refreshing) {
			setRefreshing(refreshing, false /* notify */);
		}
	}

	private void setRefreshing(boolean refreshing, final boolean notify) {
		if (mRefreshing != refreshing) {
			mNotify = notify;
			ensureTarget();
			mRefreshing = refreshing;
			if (mRefreshing) {
				mRefreshDrawable.setPercent(1f);
				animateOffsetToCorrectPosition();
			} else {
				animateOffsetToStartPosition();
			}
		}
	}

	private Animation.AnimationListener mRefreshListener = new Animation.AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
			mRefreshView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (mRefreshing) {
				mRefreshDrawable.start();
				if (mNotify) {
					if (mListener != null) {
						mListener.onRefresh();
					}
				}
			} else {
				mRefreshDrawable.stop();
				mRefreshView.setVisibility(View.GONE);
				animateOffsetToStartPosition();
			}
			mCurrentOffsetTop = mTarget.getTop();
		}
	};

	private Animation.AnimationListener mToStartListener = new Animation.AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			mRefreshDrawable.stop();
			mRefreshView.setVisibility(View.GONE);
			mCurrentOffsetTop = mTarget.getTop();
		}
	};

	private void onSecondaryPointerUp(MotionEvent ev) {
		final int pointerIndex = MotionEventCompat.getActionIndex(ev);
		final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
		if (pointerId == mActivePointerId) {
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
		}
	}

	private float getMotionEventX(MotionEvent ev, int activePointerId) {
		final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
		if (index < 0) {
			return -1;
		}
		return MotionEventCompat.getX(ev, index);
	}

	private float getMotionEventY(MotionEvent ev, int activePointerId) {
		final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
		if (index < 0) {
			return -1;
		}
		return MotionEventCompat.getY(ev, index);
	}

	private void setTargetOffsetTop(int offset, boolean requiresUpdate) {
		mRefreshView.bringToFront();
		mTarget.offsetTopAndBottom(offset);
		mRefreshDrawable.offsetTopAndBottom(offset);
		mCurrentOffsetTop = mTarget.getTop();
		if (requiresUpdate && android.os.Build.VERSION.SDK_INT < 11) {
			invalidate();
		}
	}

	private boolean canChildScrollUp() {
		if (android.os.Build.VERSION.SDK_INT < 14) {
			if (mTarget instanceof AbsListView) {
				final AbsListView absListView = (AbsListView) mTarget;
				return absListView.getChildCount() > 0
						&& (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
			} else {
				return mTarget.getScrollY() > 0;
			}
		} else {
			if (null != mTargetAbsListView) {
				return mTargetAbsListView.getChildCount() > 0
						&& (mTargetAbsListView.getFirstVisiblePosition() > 0 || mTargetAbsListView.getChildAt(0).getTop() < mTargetAbsListView.getPaddingTop());
			} else {
				return ViewCompat.canScrollVertically(mTarget, -1);
			}
		}
	}

	private boolean canChildScrollDown() {
		if (android.os.Build.VERSION.SDK_INT < 14) {
			if (mTarget instanceof AbsListView) {
				final AbsListView absListView = (AbsListView) mTarget;
				return absListView.getChildCount() > 0
						&& (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
			} else {
				return mTarget.getScrollY() > 0;
			}
		} else {
			return ViewCompat.canScrollVertically(mTarget, -1);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		ensureTarget();
		if (mTarget == null)
			return;

		int height = getMeasuredHeight();
		int width = getMeasuredWidth();
		int left = getPaddingLeft();
		int top = getPaddingTop();
		int right = getPaddingRight();
		int bottom = getPaddingBottom();

		mTarget.layout(left, top + mCurrentOffsetTop, left + width - right, top + height - bottom + mCurrentOffsetTop);
		mRefreshView.layout(left, top, left + width - right, top + height - bottom);
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
	}

	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	public static interface OnRefreshListener {
		public void onRefresh();
	}

	public View getTargetAbsListView() {
		return mTargetAbsListView;
	}

	public void setTargetAbsListView(AbsListView mTargetAbsListView) {
		this.mTargetAbsListView = mTargetAbsListView;
	}

	public OnHorizonalTouchEventListener onHorizonalTouchEventListener;

	public void setOnHorizonalTouchEventListener(OnHorizonalTouchEventListener onHorizonalTouchEventListener) {
		this.onHorizonalTouchEventListener = onHorizonalTouchEventListener;
	}

	public interface OnHorizonalTouchEventListener {
		public void onHorizonalTouchEvent();
	}
}
