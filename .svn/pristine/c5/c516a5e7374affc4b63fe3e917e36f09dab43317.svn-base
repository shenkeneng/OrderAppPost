package com.frxs.order.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frxs.order.R;
import com.frxs.order.model.ApplySaleBackInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chentie on 2017/6/15.
 */

public class ApplyRoutlItemAdapter extends BaseAdapter {

    private Context context;

    private List<ApplySaleBackInfo.TracksBean> info = new ArrayList<ApplySaleBackInfo.TracksBean>();

    private ApplyRoutlItemAdapter.OrderOptionListener listener;

    public ApplyRoutlItemAdapter(Context context, List<ApplySaleBackInfo.TracksBean> applyTrack, ApplyRoutlItemAdapter.OrderOptionListener listener)
    {
        this.context = context;
        this.info = applyTrack;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public Object getItem(int position) {
        return info.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ApplyRoutlItemAdapter.ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = View.inflate(context, R.layout.item_route_view, null);
            holder = new ApplyRoutlItemAdapter.ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_route_icon);// 位置点
            holder.icon_top_line = convertView.findViewById(R.id.icon_top_line);// 位置点上线
            holder.icon_bottom_line = convertView.findViewById(R.id.icon_bottom_line);// 位置点下线
            holder.ll_bottom_line = (LinearLayout) convertView.findViewById(R.id.ll_bottom_line);// 底部分隔线
            holder.tvStatusTime = (TextView) convertView.findViewById(R.id.tv_status_time);// 订单状态时间
            holder.tvStatusInfo = (TextView) convertView.findViewById(R.id.tv_status_info);// 订单状态信息

            convertView.setTag(holder);
        } else
        {
            holder = (ApplyRoutlItemAdapter.ViewHolder) convertView.getTag();
        }
        /**
         * 订单跟踪信息
         */
        /**
         * 下单日期处理：2016-05-07 10:00:00.033000 --> 2016/05/07 10:00
         */
        String strOrderDate = info.get(position).getCreateTime();
        //strOrderDate = strOrderDate.substring(0, strOrderDate.lastIndexOf(".")).replace("-", "/");
        holder.tvStatusTime.setText(strOrderDate);// 设置订单状态时间
        holder.tvStatusInfo.setText(info.get(position).getRemark().replace("-","/"));// 设置订单状态信息
        /**
         * 订单跟踪左侧状态
         */
        convertView.setBackgroundColor(Color.parseColor("#F4F5F5"));
        if (position == 0)
        {
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.mipmap.logistics_track_arrive));
            // 当前状态字体设置红色
            holder.tvStatusTime.setTextColor(Color.parseColor("#e60012"));
            holder.tvStatusInfo.setTextColor(Color.parseColor("#e60012"));
            holder.icon_top_line.setVisibility(View.INVISIBLE);
            holder.icon_bottom_line.setVisibility(View.VISIBLE);
            holder.ll_bottom_line.setVisibility(View.VISIBLE);
        } else if (position == info.size() - 1)
        {
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.mipmap.logistics_track_point));
            holder.icon_bottom_line.setVisibility(View.INVISIBLE);
            holder.ll_bottom_line.setVisibility(View.INVISIBLE);
            holder.icon_top_line.setVisibility(View.VISIBLE);
        } else
        {
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.mipmap.logistics_track_point));
            holder.icon_top_line.setVisibility(View.VISIBLE);
            holder.icon_bottom_line.setVisibility(View.VISIBLE);
            holder.ll_bottom_line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHolder
    {

        private ImageView icon;// 状态

        private View icon_top_line;

        private View icon_bottom_line;

        private LinearLayout ll_bottom_line;

        private TextView tvStatusTime;

        private TextView tvStatusInfo;

    }

    public interface OrderOptionListener
    {
        void onSuccess();
    }
}
