package com.frxs.order.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.EmptyView;
import com.ewu.core.widget.PtrFrameLayoutEx;
import com.frxs.order.R;
import com.frxs.order.model.ShopPointExchangeDetails;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

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
public class PointExchangeFragment extends MaterialStyleFragment {

    private ListView lvPointsIncom;

    private PtrFrameLayoutEx mPtrFrameLayoutEx;

    private Adapter pointExchangeAdapter;

    private int mPageIndex = 1;

    private final int mPageSize = 100;

    private EmptyView emptyView;

    private TextView tvYear;//　一年

    private TextView tvHalfYear;// 近半年

    private TextView tvAll;// 全部

    private String selectorType = "1";// 查询条件（空：全部； 1：近半年； 2：一年）

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
        view.findViewById(R.id.ll_point_income).setVisibility(View.GONE);// 积分收入
        view.findViewById(R.id.ll_point_exchange).setVisibility(View.VISIBLE);// 兑换明细
        tvAll = (TextView) view.findViewById(R.id.tv_all);// 全部
        tvHalfYear = (TextView) view.findViewById(R.id.tv_half_year);// 近半年
        tvYear = (TextView) view.findViewById(R.id.tv_year);//　全年
        tvHalfYear.setSelected(true);
    }

    @Override
    protected void initEvent() {
        mPtrFrameLayoutEx.setOnLoadListener(new PtrFrameLayoutEx.OnLoadListener() {
            @Override
            public void onLoad() {
                mPageIndex += 1;
                mActivity.showProgressDialog();
                reqPointExchange();
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
                reqPointExchange();
            }
        });

        tvAll.setOnClickListener(this);
        tvHalfYear.setOnClickListener(this);
        tvYear.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        addFooterView();
        pointExchangeAdapter = new com.pacific.adapter.Adapter<ShopPointExchangeDetails.ItemListBean>(getActivity(), R.layout.item_point_exchange) {
            @Override
            protected void convert(AdapterHelper helper, ShopPointExchangeDetails.ItemListBean item) {
                helper.setText(R.id.tv_point_time, item.getPostingTime().replace("-", "/"));// 时间
                helper.setText(R.id.tv_point_deadline, "积分有效期到:" + item.getExchEndTime().replace("-", "/"));// 积分有效时间
                helper.setText(R.id.tv_point_exchange, item.getRemark());// 备注
                helper.setText(R.id.tv_point, MathUtils.integerString(item.getExchPoint()));// 积分
                helper.setText(R.id.tv_point_info, "已兑:" + MathUtils.integerString(item.getExchAmt()) + "元");// 已兑金额

                /**
                 * 积分状态（不显示未兑换状态，0：已兑换；1：已结算）
                 */
                if (item.getSettleFlag() == 0){// 已兑换
                    helper.setText(R.id.tv_point_state, "已兑换");
                    helper.setTextColor(R.id.tv_point, getResources().getColor(R.color.frxs_black_dark));
                }

                if (item.getSettleFlag() == 1){// 已结算
                    helper.setText(R.id.tv_point_state, "已结算");
                    helper.setTextColor(R.id.tv_point, getResources().getColor(R.color.red));
                }
            }
        };
        lvPointsIncom.setAdapter(pointExchangeAdapter);
        mActivity.showProgressDialog();
        reqPointExchange();
    }

    /**
     * 请求兑换明细（默认请求近半年的数据）
     */
    private void reqPointExchange() {
        AjaxParams params = new AjaxParams();
        params.put("WID", getWID());
        params.put("ShopID", getShopID());
        params.put("QueryType", selectorType);//空：全部；1 近半年；2 全年
        params.put("PageIndex", mPageIndex);
        params.put("PageSize", mPageSize);
        getService().GetShopPointExchangeDetails(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<ShopPointExchangeDetails>>() {
            @Override
            public void onResponse(ApiResponse<ShopPointExchangeDetails> result, int code, String msg) {
                refreshComplete();
                mActivity.dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    if (result.getData() != null) {
                        ShopPointExchangeDetails data = result.getData();
                        List<ShopPointExchangeDetails.ItemListBean> itemList = data.getItemList();
                        if (itemList != null && itemList.size() > 0){
                            emptyView.setVisibility(View.GONE);
                            if (mPageIndex == 1) {
                                pointExchangeAdapter.replaceAll(itemList);
                            } else {
                                pointExchangeAdapter.addAll(itemList);
                            }
                        }else{
                            if (1 == mPageIndex) {
                                initNoData();
                            } else {
                                ToastUtils.show(mActivity, R.string.tips_pageending);
                            }
                        }
                        boolean hasMoreItems = (pointExchangeAdapter.getCount() < data.getTotal());
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
            public void onFailure(Call<ApiResponse<ShopPointExchangeDetails>> call, Throwable t) {
                super.onFailure(call, t);
                refreshComplete();
                mActivity.dismissProgressDialog();
                ToastUtils.show(mActivity, t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_all:{// 全部
                selectorType = "";
                tvAll.setSelected(true);
                tvHalfYear.setSelected(false);
                tvYear.setSelected(false);
                break;
            }

            case R.id.tv_half_year:{// 近半年
                selectorType = "1";
                tvAll.setSelected(false);
                tvHalfYear.setSelected(true);
                tvYear.setSelected(false);
                break;
            }

            case R.id.tv_year:{// 全年
                selectorType = "2";
                tvAll.setSelected(false);
                tvHalfYear.setSelected(false);
                tvYear.setSelected(true);
                break;
            }
        }

        mActivity.showProgressDialog();
        reqPointExchange();
    }

    private void initNoData() {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setMode(EmptyView.MODE_NODATA);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.showProgressDialog();
                reqPointExchange();
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
