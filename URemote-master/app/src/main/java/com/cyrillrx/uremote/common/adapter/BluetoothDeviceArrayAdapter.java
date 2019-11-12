package com.cyrillrx.uremote.common.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cyrillrx.uremote.R;
import com.cyrillrx.uremote.common.device.BluetoothDevice;

import java.util.List;

/**
 * Adapter used to display bluetooth device list.
 *
 * @author Cyril Leroux
 *         Created on 23/10/12.
 */
public class BluetoothDeviceArrayAdapter extends ArrayAdapter<BluetoothDevice> {

    private final LayoutInflater inflater;

    /**
     * Adapter constructor
     *
     * @param context The application context.
     * @param devices The list of {@link com.cyrillrx.uremote.common.device.BluetoothDevice} to display.
     */
    public BluetoothDeviceArrayAdapter(Context context, final List<BluetoothDevice> devices) {
        super(context, 0, devices);
        inflater = LayoutInflater.from(context);
    }

    /** Device information view */
    public static class ViewHolder {
        TextView tvDeviceName;
        TextView tvDeviceAddress;
        TextView tvDeviceType;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_device_bt, null);
            holder = new ViewHolder();
            holder.tvDeviceName = convertView.findViewById(R.id.tvDeviceName);
            holder.tvDeviceAddress = convertView.findViewById(R.id.tvDeviceAddress);
            holder.tvDeviceType = convertView.findViewById(R.id.tvDeviceType);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BluetoothDevice device = getItem(position);
        holder.tvDeviceName.setText(device.getName());
        holder.tvDeviceAddress.setText(device.getAddress());
        holder.tvDeviceType.setText(device.getType());

        return convertView;
    }
}
