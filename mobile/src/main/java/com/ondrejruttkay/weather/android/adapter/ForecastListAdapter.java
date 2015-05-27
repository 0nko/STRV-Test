package com.ondrejruttkay.weather.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ondrejruttkay.weather.android.R;
import com.ondrejruttkay.weather.android.WeatherConfig;
import com.ondrejruttkay.weather.android.entity.api.ForecastDetails;
import com.ondrejruttkay.weather.android.utility.Units;
import com.squareup.picasso.Picasso;

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_forecast_list_item, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.forecast_image);
            holder.day = (TextView) convertView.findViewById(R.id.forecast_day);
            holder.temperature = (TextView) convertView.findViewById(R.id.forecast_temperature);
            holder.description = (TextView) convertView.findViewById(R.id.forecast_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(mContext).load(WeatherConfig.API_IMAGE_URL + mData[position].getWeatherData().getIcon() + ".png").into(holder.image);
        holder.day.setText(mData[position].getDayOfWeek());
        holder.temperature.setText(Units.getTemperature(mData[position].getTemperature().getDayTemperature()));
        holder.description.setText(mData[position].getWeatherData().getDescription());

        return convertView;
    }


    class ViewHolder {
        public ImageView image;
        public TextView day;
        public TextView temperature;
        public TextView description;
    }
}
