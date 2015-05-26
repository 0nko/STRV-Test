package com.ondrejruttkay.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ondrejruttkay.weather.R;
import com.ondrejruttkay.weather.entity.DrawerListItem;
import com.ondrejruttkay.weather.entity.api.ForecastDetails;
import com.ondrejruttkay.weather.utility.Units;

import java.util.List;

/**
 * Created by Onko on 5/25/2015.
 */
public class ForecastListAdapter extends BaseAdapter {
    private ForecastDetails[] mData;
    private Context mContext;

    public ForecastListAdapter(Context context, ForecastDetails[] data) {
        this.mContext = context;
        this.mData = data;
    }


    @Override
    public int getCount() {
        return mData.length;
    }


    @Override
    public Object getItem(int position) {
        return mData[position];
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.forecast_list_item, null);
            holder = new ViewHolder();
            holder.day = (TextView)convertView.findViewById(R.id.forecast_day);
            holder.temperature = (TextView)convertView.findViewById(R.id.forecast_temperature);
            holder.description = (TextView)convertView.findViewById(R.id.forecast_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.day.setText(mData[position].getDayOfWeek());
        holder.temperature.setText(Units.getTemperature(mData[position].getTemperature().getDayTemperature()));
        holder.description.setText(mData[position].getWeatherData().getDescription());

        return convertView;
    }


    class ViewHolder {
        public TextView day;
        public TextView temperature;
        public TextView description;
    }
}
