package com.frxs.order.fragment;

import android.graphics.drawable.PaintDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.EmptyView;
import com.ewu.core.widget.PtrFrameLayoutEx;
import com.frxs.order.R;
import com.frxs.order.model.ShopPointIncome;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.util.Arrays;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;

/**
 * <pre>
 *     author : ewu
 *     e-mail : xxx@xx
 *     time   : 2017/03/09
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */
public class PointIncomeFragment extends MaterialStyleFragment {

    private ListView lvPointsIncom;

    private Adapter pointIncomeAdapter;

    private PtrFrameLayoutEx mPtrFrameLayoutEx;

    private int mPageIndex = 1;

    private final int mPageSize = 100;

    private EmptyView emptyView;

    private TextView tvPoint;// 本月可用积分

    private TextView tvLastMonth;// 上月

    private TextView tvCurrentMonth;// 本月

    private int selectorMonth = 1;// 查询条件（1：本月； 2：上月）

    private int pointType = 0; //0：全部，1：订货积分（订单商品积分），4：增值积分（附属店返利积分），2：退货积分，3：其他积分（手工调整积分）

    private PopupWindow filterPointsPw;

    private ListView filterConditionLv;

    private View pointIncomeView;

    private List<String> pointsTypes;

    private TextView usablePointTypeTv;

    @Override
    public int getPtrFrameLayoutId() {
        return R.id.ptr_frame_ll;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_point_income;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);

        mPtrFrameLayoutEx = (PtrFrameLayoutEx) mPtrFrameLayout;
        emptyView = (EmptyView) view.findViewById(R.id.emptyview);
        lvPointsIncom = (ListView) view.findViewById(R.id.lv_points_income);
        pointIncomeView = view.findViewById(R.id.ll_point_income);
        view.findViewById(R.id.ll_point_income).setVisibility(View.VISIBLE);// 积分收入
        view.findViewById(R.id.ll_point_exchange).setVisibility(View.GONE);// 兑换明细
        usablePointTypeTv = (TextView) view.findViewById(R.id.usable_point_type_tv);
        tvPoint = (TextView) view.findViewById(R.id.tv_point);// 本月可用积分
        tvLastMonth = (TextView) view.findViewById(R.id.tv_last_month);// 上月
        tvCurrentMonth = (TextView) view.findViewById(R.id.tv_current_month);// 本月
        tvCurrentMonth.setSelected(true);

        view.findViewById(R.id.usable_points_layout).setOnClickListener(this);
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.drop_down_view, null);
        filterPointsPw = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        filterConditionLv = (ListView)contentView.findViewById(R.id.list_view);
        filterPointsPw.setBackgroundDrawable(new PaintDrawable());
        filterPointsPw.setFocusable(true);
        filterPointsPw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvPoint.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mActivity, R.mipmap.navi_fold), null);
            }
        });

        String[] pointsTypeArray = getResources().getStringArray(R.array.select_points_type);
        usablePointTypeTv.setText(pointsTypeArray[0]);
        pointsTypes = Arrays.asList(pointsTypeArray);
        filterConditionLv.setAdapter(new Adapter<String>(mActivity, pointsTypes, R.layout.pop_menu_item) {
            @Override
            protected void convert(AdapterHelper helper, String item) {
                helper.setText(R.id.menu_item_tv, item);
            }
        });
        filterConditionLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getAdapter().getItem(position);
                if (!TextUtils.isEmpty(item)) {
                    usablePointTypeTv.setText(item);
                    mPageIndex = 1;
                    switch (position) {
                        case 0: {
                            pointType = 0;
                            break;
                        }
                        case 1: {
                            pointType = 1;
                            break;
                        }
                        case 2: {
                            pointType = 4;
                            break;
                        }
                        case 3: {
                            pointType = 2;
                            break;
                        }
                        case 4: {
                            pointType = 3;
                            break;
                        }
                        default:
                            break;
                    }

                    mActivity.showProgressDialog();
                    reqCreditDetails();
                }

                filterPointsPw.dismiss();
            }
        });
    }

    @Override
    protected void initEvent() {
        mPtrFrameLayoutEx.setOnLoadListener(new PtrFrameLayoutEx.OnLoadListener() {
            @Override
            public void onLoad() {
                mPageIndex += 1;
                mActivity.showProgressDialog();
                reqCreditDetails();
            }
        });

        mPtrFrameLayout.setPinContent(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                boolean iReturn = PtrDefaultHandler.checkContentCanBePulledDown(frame, lvPointsIncom, header);
                return iReturn;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageIndex = 1;
                reqCreditDetails();
            }
        });

        tvLastMonth.setOnClickListener(this);
        tvCurrentMonth.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mActivity.showProgressDialog();
        reqCreditDetails();
        addFooterView();
        pointIncomeAdapter = new Adapter<ShopPointIncome.ItemListBean>(getActivity(), R.layout.item_point_month) {
            @Override
            protected void convert(AdapterHelper helper, ShopPointIncome.ItemListBean item) {
                if (item.getPointType() == 4) {
                    helper.setText(R.id.tv_goods_name, item.getRemark());//增值积分名称
                } else {
                    helper.setText(R.id.tv_goods_name, item.getProductName());//商品名称
                }
                helper.setText(R.id.tv_goods_code, item.getSKU());//商品编码
                helper.setText(R.id.tv_goods_count, "X " + MathUtils.integerString(item.getQty()));//商品数量
                helper.setText(R.id.tv_order_time, item.getPointTime().replace("-", "/"));//积分收入时间
                if (!TextUtils.isEmpty(item.getProductImageUrl200())) {
                    helper.setImageUrl(R.id.iv_goods, item.getProductImageUrl200());//商品图片
                } else {
                    helper.setImageDrawable(R.id.iv_goods, getResources().getDrawable(R.mipmap.showcase_product_default));//商品图片
                }
                //商品积分 订单类型
                if (item.getPointQty() >= 0) {
                    helper.setText(R.id.tv_goods_point, "+ " + MathUtils.twolittercountString(item.getPointQty()));
                    helper.setText(R.id.tv_order_id, "订单:" + item.getBillNO());//订单编码
                } else {
                    helper.setText(R.id.tv_goods_point, MathUtils.twolittercountString(item.getPointQty()));
                    helper.setText(R.id.tv_order_id, "退货单:" + item.getBillNO());//退货单编码
                }
            }
        };
        lvPointsIncom.setAdapter(pointIncomeAdapter);

    }

    /**
     * 请求积分收入数据
     */
    private void reqCreditDetails() {
        AjaxParams params = new AjaxParams();
        params.put("WID", getWID());
        params.put("ShopID", getShopID());
        params.put("QueryType", selectorMonth);
        params.put("PointType",pointType);
        params.put("PageIndex", mPageIndex);
        params.put("PageSize", mPageSize);
        getService().GetShopPointDetails(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<ShopPointIncome>>() {

            @Override
            public void onResponse(ApiResponse<ShopPointIncome> result, int code, String msg) {
                refreshComplete();
                mActivity.dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    if (result.getData() != null) {
                        ShopPointIncome data = result.getData();
                        tvPoint.setText(MathUtils.twolittercountString(data.getPointQtyTotay()));
                        List<ShopPointIncome.ItemListBean> itemList = data.getItemList();
                        if (itemList != null && itemList.size() > 0) {
                            emptyView.setVisibility(View.GONE);
                            if (mPageIndex == 1) {
                                pointIncomeAdapter.replaceAll(itemList);
                            } else {
                                pointIncomeAdapter.addAll(itemList);
                            }
                        } else {
                            if (1 == mPageIndex) {
                                initNoData();
                            } else {
                                ToastUtils.show(mActivity, R.string.tips_pageending);
                            }
                        }

                        boolean hasMoreItems = (pointIncomeAdapter.getCount() < data.getTotal());
                        mPtrFrameLayoutEx.onFinishLoading(hasMoreItems);
                    } else {
                        if (1 == mPageIndex) {
                            initNoData();
                        } else {
                            ToastUtils.show(mActivity, R.string.tips_pageending);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ShopPointIncome>> call, Throwable t) {
                super.onFailure(call, t);
                refreshComplete();
                boolean hasMoreItems = !(pointIncomeAdapter.getCount() < mPageIndex * mPageSize);
                mPtrFrameLayoutEx.onFinishLoading(hasMoreItems);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setMode(EmptyView.MODE_ERROR);
                emptyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.showProgressDialog();
                        reqCreditDetails();
                    }
                });
                mActivity.dismissProgressDialog();
            }
        }

    );
}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_last_month: {
                selectorMonth = 2;
                tvLastMonth.setSelected(true);
                tvCurrentMonth.setSelected(false);
                mActivity.showProgressDialog();
                reqCreditDetails();
                break;
            }
            case R.id.tv_current_month: {
                selectorMonth = 1;
                tvLastMonth.setSelected(false);
                tvCurrentMonth.setSelected(true);
                mActivity.showProgressDialog();
                reqCreditDetails();
                break;
            }
            case R.id.usable_points_layout: {
                if (filterPointsPw.isShowing()) {
                    filterPointsPw.dismiss();
                } else {
                    tvPoint.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mActivity, R.mipmap.navi_unfold), null);
                    filterConditionLv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    filterPointsPw.setWidth(filterConditionLv.getMeasuredWidth());
                    filterPointsPw.showAsDropDown(pointIncomeView);
                }
                break;
            }
            default:
                break;
        }
    }

    private void initNoData() {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setMode(EmptyView.MODE_NODATA);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.showProgressDialog();
                reqCreditDetails();
            }
        });
    }

    /**
     * 添加脚布局
     */
    private void addFooterView() {
        TextView footerView = new TextView(getActivity());
        footerView.setText("亲，已经到底了~");
        footerView.setPadding(10, 10, 10, 10);
        footerView.setGravity(Gravity.CENTER);
        lvPointsIncom.addFooterView(footerView);

    }
}
