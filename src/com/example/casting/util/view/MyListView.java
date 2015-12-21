package com.example.casting.util.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casting_android.R;

public class MyListView extends ListView implements OnScrollListener {

	private final String LABLE_PAGE_COUNT = "pagecount";
	private final String LABLE_ADD_SCROLL_LISTENER = "listener";
	private Context context;
	/** 执行动作 刷新 */
	public final static int REFRESH = 1;
	/** 执行动作 加载更多 */
	public final static int LOADMORE = 2;

	/** 区分下拉和下拉刷新的距离，小于20的时候则不会进行刷新 */
	private static final int SPACE = 15;
	/** 头部view的动作：初始状态 */
	private final int NONE = 3;
	/** 头部view的动作：下拉状态 */
	private final int PULL = 4;
	/** 头部view的动作：下拉即将刷新状态 */
	private final int RELEASE = 5;
	/** 头部view的动作：正在刷新，刷新结束后回到初始状态 */
	private final int REFRESHING = 6;

	private int state = NONE;

	private int startY = 0;

	private View header;
	private View footer;
	private ProgressBar  footer_progressBar;
	private TextView header_tip, header_last_update, footer_show;
	private ImageView header_img;

	private int firstVisibleItem;
	private int scrollState;

	/** 刷新时箭头特效 */
	private RotateAnimation animation;
	private RotateAnimation reverseAnimation,mRotateAnimation;

	private LayoutInflater inflater;
	/** 头部的高度 */
	private int headerContentHeight;
	/** 每页显示条数，默认为30 */
	private int pageItemCount = 30;

	private OnRefreshListener onRefreshListener;
	private OnLoadListener onLoadListener;
	   /**旋转动画的时间*/
    static final int ROTATION_ANIMATION_DURATION = 1200;
    /**动画插值*/
    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
	/**
	 * isNeedLoad:根据此字段判断是否需要加载更多 isLoading:是否正在加载
	 */
	public boolean isNeedLoad = true, isLoading = false, onRefreshing = false;
	/** 只有在listview第一个item显示的时候（listview滑到了顶部）才进行下拉刷新， 否则此时的下拉只是滑动listview */
	private boolean isRecorded;
	/** 是否绑定滚动监听，即上下刷新事件 */
	private boolean onScrollListener;

	/** 设置每页显示条数 */
	// public void setPageItemCount(int count)
	// {
	// this.pageItemCount = count;
	// }
	 
	public void initView() {
		initAnim();
		inflater = LayoutInflater.from(context);
		header = inflater.inflate(R.layout.listview_refresh_header, null);
		footer = inflater.inflate(R.layout.listview_footer, null);
//		header_progressBar = (ProgressBar) header.findViewById(R.id.refreshing);
		footer_progressBar = (ProgressBar) footer.findViewById(R.id.loading);
		header_tip = (TextView) header.findViewById(R.id.tip);
		header_img = (ImageView) header.findViewById(R.id.arrow);
		header_last_update = (TextView) header.findViewById(R.id.lastUpdate);
		footer_show = (TextView) footer.findViewById(R.id.more);

		// 为listview添加头部和尾部，并进行初始化
		// headerContentInitialHeight = header.getPaddingTop();
		measureView(header);
		headerContentHeight = header.getMeasuredHeight();
		// 调整header的大小。其实调整的只是距离顶部的高度。
		topPadding(headerContentHeight);
		if (onScrollListener) {
			this.setOnScrollListener(this);
			this.addHeaderView(header);
			this.addFooterView(footer,null,false);
			this.removeFooterView(footer);
		}
		setBackgroundColor(getResources().getColor(R.color.smssdk_transparent));
		refreshHeaderViewByState();
	}

	public void initAnim() {
		// 设置箭头特效
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(100);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(100);
		reverseAnimation.setFillAfter(true);
	    float pivotValue = 0.5f;    // SUPPRESS CHECKSTYLE
	    float toDegree = 720.0f;   
	    mRotateAnimation = new RotateAnimation(0.0f, toDegree, Animation.RELATIVE_TO_SELF, pivotValue,
                Animation.RELATIVE_TO_SELF, pivotValue);
        mRotateAnimation.setFillAfter(true);
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setRepeatMode(Animation.RESTART);
	}

	public void onRefresh() {
		if (this.onRefreshListener != null && !onRefreshing) {
			onRefreshing = true;
			isLoading = true;
			onRefreshListener.onRefresh();
		}
	}

	public void onload() {
		if (this.onLoadListener != null) {
			footer.setVisibility(View.VISIBLE);
			footer_progressBar.setVisibility(View.VISIBLE);
			footer_show.setText(context.getString(R.string.more));
			onRefreshing = true;
			isLoading = true;
			onLoadListener.onLoad();
		}
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.EditText);
		// this.pageItemCount
		// =Integer.parseInt(a.getString(R.styleable.EditText_pagecount));
		this.onScrollListener = a.getBoolean(R.styleable.EditText_listener,
				true);
		a.recycle();
		initView();
	}

	public MyListView(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	/** 用来计算header大小的。比较隐晦。因为header的初始高度就是0,貌似可以不用。 */
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
		ifNeedLoad(view, scrollState);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
	}

	// 下拉刷新监听
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	// 加载更多监听
	public void setOnLoadListener(OnLoadListener onLoadListener) {
		this.onLoadListener = onLoadListener;
	}

	/** 用于下拉刷新结束后的回调 */
	public void onRefreshComplete() {
		String currentTime = getCurrentTime("yyyy-MM-dd  HH:mm:ss");
		header_last_update.setText(context.getString(R.string.lastUpdateTime)+currentTime);
		state = NONE;
		onRefreshing = false;
		isLoading = false;
		refreshHeaderViewByState();
	}

	/** 用于加载更多结束后的回调 */
	public void onLoadComplete(String msg) {
		isLoading = false;
		onRefreshing = false;
		setFooter(msg);
	}

	/**
	 * 监听触摸事件，解读手势
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (firstVisibleItem == 0) {
				isRecorded = true;
				startY = (int) ev.getY();
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (state == PULL) {
				state = NONE;
				refreshHeaderViewByState();
			} else if (state == RELEASE) {
				state = REFRESHING;
				refreshHeaderViewByState();
				onRefresh();
			}
			isRecorded = false;
			break;
		case MotionEvent.ACTION_MOVE:
			whenMove(ev);
			break;
		}
		return super.onTouchEvent(ev);
	}

	// 解读手势，刷新header状态
	private void whenMove(MotionEvent ev) {
		if (!isRecorded) {
			return;
		}
		int tmpY = (int) ev.getY();
		int space = tmpY - startY;
		int topPadding = space - headerContentHeight;
		switch (state) {
		case NONE:
			if (space > 0 && !isLoading) {
				state = PULL;
				refreshHeaderViewByState();
			}
			break;
		case PULL:
			topPadding(topPadding);
			if (scrollState == SCROLL_STATE_TOUCH_SCROLL
					&& space > headerContentHeight + SPACE) {
				state = RELEASE;
				refreshHeaderViewByState();
			}
			break;
		case RELEASE:
			topPadding(topPadding);
			if (space > 0 && space < headerContentHeight + SPACE) {
				state = PULL;
				refreshHeaderViewByState();
			} else if (space <= 0) {
				state = NONE;
				refreshHeaderViewByState();
			}
			break;
		}

	}

	/** 根据listview滑动的状态判断是否需要加载更多 */
	private void ifNeedLoad(AbsListView view, int scrollState) {
		try {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& !isLoading
					&& isNeedLoad) {
				footer.setVisibility(View.VISIBLE);
				addFooterView(footer,null, false);
				onload();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 调整header的大小。其实调整的只是距离顶部的高度。
	private void topPadding(int topPadding) {
		header.setPadding(header.getPaddingLeft(), topPadding,
				header.getPaddingRight(), header.getPaddingBottom());
		header.invalidate();
	}

	// 根据当前状态，调整header
	private void refreshHeaderViewByState() {
		switch (state) {
		case NONE:
			topPadding(-headerContentHeight);
			header_tip.setText(R.string.pull_to_refresh);
//			header_progressBar.setVisibility(View.GONE);
			header_tip.setVisibility(View.GONE);
			header_img.setVisibility(View.GONE);
			header_img.clearAnimation();
			header_img.setImageResource(R.drawable.default_ptr_rotate);
			break;
		case PULL:
			header_img.setVisibility(View.VISIBLE);
			header_tip.setVisibility(View.VISIBLE);
			header_last_update.setVisibility(View.VISIBLE);
//			header_progressBar.setVisibility(View.GONE);
			header_tip.setText(R.string.pull_to_refresh);
			header_img.clearAnimation();
			header_img.setAnimation(mRotateAnimation);
			state = RELEASE;
			break;
		case RELEASE:
			header_img.setVisibility(View.VISIBLE);
			header_tip.setVisibility(View.VISIBLE);
			header_last_update.setVisibility(View.VISIBLE);
//			header_progressBar.setVisibility(View.GONE);
			header_tip.setText(R.string.pull_to_refresh);
			header_tip.setText(R.string.release_to_refresh);
//			header_img.clearAnimation();
//			header_img.setAnimation(mRotateAnimation);
			break;
		case REFRESHING:
			topPadding(headerContentHeight);
//			header_progressBar.setVisibility(View.VISIBLE);
//			header_img.clearAnimation();
//			header_img.setVisibility(View.GONE);
			// header_tip.setVisibility(View.GONE);
			header_tip.setText("正在刷新");
			header_last_update.setVisibility(View.VISIBLE);
			break;
		}
	}

	/**
	 * 这个方法是根据结果的大小来决定footer显示的。
	 * <p>
	 * 这里假定每次请求的条数为10。如果请求到了10条。则认为还有数据。如过结果不足10条，则认为数据已经全部加载，这时footer显示已经全部加载
	 * </p>
	 * 
	 * @param resultSize
	 */
	public void setFooter(String msg) {
		// isNeedLoad = true;
		footer_show.setText(context.getString(R.string.load_full));
		footer_progressBar.setVisibility(View.GONE);
		footer.setVisibility(View.GONE);
		removeFooterView(footer);
	}

	public interface OnRefreshListener {
		public void onRefresh();
	};

	public interface OnLoadListener {
		public void onLoad();
	};

	public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}
    /**
     * 开始刷新，通常用于调用者主动刷新，典型的情况是进入界面，开始主动刷新，这个刷新并不是由用户拉动引起的
     * 
     */
    public void doPullRefreshing(final boolean smoothScroll, final long delayMillis) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
            	
            }
        }, delayMillis);
    }

}
