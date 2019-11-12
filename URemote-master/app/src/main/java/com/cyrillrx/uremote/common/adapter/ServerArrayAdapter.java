package com.cyrillrx.uremote.common.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyrillrx.uremote.R;
import com.cyrillrx.uremote.common.device.NetworkDevice;
import com.cyrillrx.uremote.component.ConnectedDeviceDrawable;

import java.util.List;

/**
 * Adapter used to display server list.
 *
 * @author Cyril Leroux
 *         Created on 22/05/13.
 */
public class ServerArrayAdapter extends ArrayAdapter<NetworkDevice> {

    private final LayoutInflater inflater;

    /**
     * Default constructor
     *
     * @param context The application context.
     * @param devices The list of {@link com.cyrillrx.uremote.common.device.NetworkDevice} to display.
     */
    public ServerArrayAdapter(Context context, List<NetworkDevice> devices) {
        super(context, 0, devices);
        inflater = LayoutInflater.from(context);
    }

    /** Template for the list items. */
    public static class ViewHolder {
        ImageView ivThumbnail;
        TextView tvName;
        TextView tvLocalhost;
        TextView tvRemoteHost;
        TextView tvMacAddress;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_server, null);
            holder = new ViewHolder();
            holder.ivThumbnail = convertView.findViewById(R.id.thumbnail);
            holder.tvName = convertView.findViewById(R.id.server_name);
            holder.tvLocalhost = convertView.findViewById(R.id.local_host);
            holder.tvRemoteHost = convertView.findViewById(R.id.remote_host);
            holder.tvMacAddress = convertView.findViewById(R.id.mac_address);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final NetworkDevice device = getItem(position);

        holder.ivThumbnail.setImageDrawable(new ConnectedDeviceDrawable(device));
        holder.tvName.setText(device.getName());
        holder.tvLocalhost.setText(device.toStringLocal());
        holder.tvRemoteHost.setText(device.toStringRemote());
        holder.tvMacAddress.setText(device.getMacAddress());

        return convertView;
    }
}
