package com.ondrejruttkay.weather.adapter;

/**
 * Created by Onko on 5/21/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ondrejruttkay.weather.R;
import com.ondrejruttkay.weather.entity.DrawerListItem;

import java.util.List;

public class DrawerListAdapter extends BaseAdapter {

    private List<DrawerListItem> mList;
    private Context mContext;

    public DrawerListAdapter(Context context, List<DrawerListItem> list) {
        this.mContext = context;
        this.mList = list;
    }


    @Override
    public int getCount() {
        return mList.size();
    }


    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.drawer_list_item, null);
            holder = new ViewHolder();
            holder.menuItem = (TextView)convertView.findViewById(R.id.menuItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.menuItem.setText(mList.get(position).getTitle());
        holder.menuItem.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(mList.get(position).getIcon()), null, null, null);

        return convertView;
    }


    class ViewHolder {
        public TextView menuItem;
    }
}
