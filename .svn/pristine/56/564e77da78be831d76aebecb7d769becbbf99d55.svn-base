package com.frxs.order;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ewu.core.utils.DateUtil;
import com.ewu.core.widget.EmptyView;
import com.ewu.core.widget.NoScrollGridView;
import com.ewu.core.widget.PtrFrameLayoutEx;
import com.frxs.order.model.WarehouseMessage;
import com.frxs.order.model.WarehouseMessageShopGetListRespData;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.utils.DensityUtils;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;

/**
 * Created by ewu on 2016/5/4.
 */
public class MessageListActivity extends MaterialStyleActivity {

    private EmptyView emptyView;

    private TextView tvTitle;

    private ListView messageLv;

    private Adapter<WarehouseMessage> quickAdapter;

    private int mPageIndex = 1;

    private final int mPageSize = 30;

    private String[] mBillTime;

    private NoScrollGridView mPopGrid;

    private LinearLayout mPopContent;

    private PopupWindow mWindow;

    private View mPopView;

    private int mCurrentType = 0;

    private int searchTime = 365;

    private PtrFrameLayoutEx mPtrFrameLayoutEx;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_list;
    }

    @Override
    public int getPtrFrameLayoutId() {
        return R.id.messages_ptr_frame_ll;
    }

    @Override
    protected void initViews() {
        super.initViews();

        emptyView = (EmptyView) findViewById(R.id.emptyview);
        mPtrFrameLayoutEx = (PtrFrameLayoutEx) mPtrFrameLayout;
        tvTitle = (TextView) findViewById(R.id.title_tv);
        tvTitle.setText("全部消息");
        mBillTime = getResources().getStringArray(R.array.select_time);
        tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_white_down, 0);
        mPopView = LayoutInflater.from(this).inflate(R.layout.pop_ordertype, null);
        mPopView.setPadding(0, 0, 0, 0);
        mPopContent = (LinearLayout) mPopView.findViewById(R.id.content);
        mPopGrid = (NoScrollGridView) mPopView.findViewById(R.id.gridView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_btn, R.id.btn, mBillTime);
        mPopGrid.setAdapter(adapter);
        mWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mWindow.setAnimationStyle(R.style.ZoomAnimation);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mWindow.setBackgroundDrawable(dw);
        mWindow.setOutsideTouchable(true);

        messageLv = (ListView) findViewById(R.id.message_list_view);
    }

    @Override
    protected void initEvent() {
        tvTitle.setOnClickListener(this);

        /**
         * 弹窗监听事件
         */
        mPopView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mWindow.dismiss();
            }
        });

        /**
         * 弹窗内容选择监听事件
         */
        mPopGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取某个指定position的view，并对该view进行刷新。
                mPopGrid.getChildAt(mCurrentType).findViewById(R.id.btn).setSelected(false);
                view.findViewById(R.id.btn).setSelected(true);
                /**
                 *消息时间查询处理
                 */
                switch (position) {
                    // 全部消息
                    case 0:
                        tvTitle.setText(mBillTime[position]);
                        searchTime = 365;
                        showProgressDialog();
                        requestGetMessageList(searchTime);
                        setPosition(position);
                        break;
                    // 最近一周
                    case 1:
                        tvTitle.setText(mBillTime[position]);
                        searchTime = 7;
                        showProgressDialog();
                        requestGetMessageList(searchTime);
                        setPosition(position);
                        break;
                    //最近一月
                    case 2:
                        tvTitle.setText(mBillTime[position]);
                        searchTime = 30;
                        showProgressDialog();
                        requestGetMessageList(searchTime);
                        setPosition(position);
                        break;
                    //最近三月
                    case 3:
                        tvTitle.setText(mBillTime[position]);
                        searchTime = 90;
                        showProgressDialog();
                        requestGetMessageList(searchTime);
                        setPosition(position);
                        break;
                    //最近半年
                    case 4:
                        tvTitle.setText(mBillTime[position]);
                        searchTime = 180;
                        showProgressDialog();
                        requestGetMessageList(searchTime);
                        setPosition(position);
                        break;
                    default:
                        break;
                }
                mWindow.dismiss();
            }

        });

        mWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_white_down, 0);
            }
        });


        mPtrFrameLayout.setPinContent(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                boolean iReturn = PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                return iReturn;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageIndex = 1;
                requestGetMessageList(searchTime);
            }
        });

        quickAdapter = new Adapter<WarehouseMessage>(this, R.layout.view_message_list_item) {
            @Override
            protected void convert(AdapterHelper helper, WarehouseMessage item) {
                helper.setText(R.id.msg_title_tv, item.getTitle());
                switch (item.getMessageType()) //0：重要消息;1:促销;2:其他
                {
                    case 0: {
                        helper.setImageResource(R.id.msg_type_iv, R.mipmap.msg_notification);
                        break;
                    }
                    case 1: {
                        helper.setImageResource(R.id.msg_type_iv, R.mipmap.msg_promotion);
                        break;
                    }
                    case 2: {
                        helper.setImageResource(R.id.msg_type_iv, R.mipmap.msg_others);
                        break;
                    }
                    default:
                        break;
                }

                Date beginTime = DateUtil.string2Date(item.getBeginTime(), "yyyy-MM-dd HH:mm");
                String time = DateUtil.format(beginTime, "yyyy-MM-dd HH:mm");
                helper.setText(R.id.time_tv, time);

                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timeStr=format.format(new Date());
                Date endTime = DateUtil.string2Date(timeStr, "yyyy-MM-dd HH:mm");
//                Log.i("当前时间",endTime.toString());

                // 消息开始的时间与系统当前时间作比对，如果小于7天就显示红点，否则隐藏红点。
                if (DateUtil.getGapDays(beginTime,endTime) <= 7) {
//                    Log.i("判断时间差",DateUtil.getGapDays(beginTime,endTime)+"");
                    helper.setVisible(R.id.new_msg_flag_tv, View.VISIBLE);
                }else
                {
                    helper.setVisible(R.id.new_msg_flag_tv, View.GONE);
                }
            }
        };

        messageLv.setAdapter(quickAdapter);

        messageLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*
                 * 此处position的位置不是从0开始的，如果Head添加了一个View从1开始，如果Head添加了两个View,则从2开始
				 * ，依次类推
				 * 遇到这种问题，我们直接采用parent.getAdapter().getItem(position)获得被点击Item对象
				 */
                WarehouseMessage item = (WarehouseMessage) parent.getAdapter().getItem(position);
                if (null != item) {
                    Intent intent = new Intent(MessageListActivity.this, MessageDetailActivity.class);
                    intent.putExtra("message", item);
                    MessageListActivity.this.startActivity(intent);
                }
            }
        });

        mPtrFrameLayoutEx.setOnLoadListener(new PtrFrameLayoutEx.OnLoadListener() {
            @Override
            public void onLoad() {
                mPageIndex += 1;
                showProgressDialog();
                requestGetMessageList(searchTime);
            }
        });
    }


    @Override
    protected void initData() {
        showProgressDialog();
        requestGetMessageList(searchTime);
    }

    private void requestGetMessageList(int time) {
        AjaxParams params = new AjaxParams();
        params.put("UserID", getUserID());
        params.put("WID", getWID());
        params.put("SearchTime", time);
        params.put("ShopID", getShopID());
        params.put("PageIndex", mPageIndex);
        params.put("PageSize", mPageSize);

        getService().WarehouseMessageShopGetList(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<WarehouseMessageShopGetListRespData>>() {
            @Override
            public void onResponse(ApiResponse<WarehouseMessageShopGetListRespData> result, int code, String msg) {
                mPtrFrameLayout.refreshComplete();
                dismissProgressDialog();
                WarehouseMessageShopGetListRespData respData = result.getData();
                if (null != respData) {
                    List<WarehouseMessage> messageList = respData.getItemList();
                    if (null != messageList && messageList.size() > 0) {
                        emptyView.setVisibility(View.GONE);

                        quickAdapter.replaceAll(messageList);
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setMode(EmptyView.MODE_NODATA);

                        quickAdapter.clear();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<WarehouseMessageShopGetListRespData>> call, Throwable t) {
                super.onFailure(call, t);
                mPtrFrameLayout.refreshComplete();
                dismissProgressDialog();
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setMode(EmptyView.MODE_ERROR);
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.title_tv: {
                mWindow.showAsDropDown(view, 0, DensityUtils.dip2px(this, 1));
                mPopContent.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pop_zoomin2));
                tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_white_up, 0);
                break;
            }
            default:
                break;
        }
    }

    public void setPosition(int position) {
        mCurrentType = position;
    }
}
