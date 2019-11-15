package com.example.projectnewjava.view;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import com.example.projectnewjava.R;
import com.example.projectnewjava.bluetooth.BluetoothController;
import com.example.projectnewjava.bluetooth.BluetoothDiscoveryDeviceListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BluetoothDevice} and makes a call to the
 * specified {@link ListInteractionListener} when the element is tapped.
 */
public class DeviceRecyclerViewAdapter
        extends RecyclerView.Adapter<DeviceRecyclerViewAdapter.ViewHolder>
        implements BluetoothDiscoveryDeviceListener {

    /**
     * The devices shown in this {@link RecyclerView}.
     */
    private final List<BluetoothDevice> devices;

    /**
     * Callback for handling interaction events.
     */
    private final ListInteractionListener<BluetoothDevice> listener;

    /**
     * Controller for Bluetooth functionalities.
     */
    private BluetoothController bluetooth;

    /**
     * Instantiates a new DeviceRecyclerViewAdapter.
     *
     * @param listener an handler for interaction events.
     */
    public DeviceRecyclerViewAdapter(ListInteractionListener<BluetoothDevice> listener) {
        this.devices = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_device_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = devices.get(position);
        holder.mImageView.setImageResource(getDeviceIcon(devices.get(position)));
        holder.mDeviceNameView.setText(devices.get(position).getName());
        holder.mDeviceAddressView.setText(devices.get(position).getAddress());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onItemClick(holder.mItem);
                }
            }
        });
    }

    /**
     * Returns the icon shown on the left of the device inside the list.
     *
     * @param device the device for the icon to get.
     * @return a resource drawable id for the device icon.
     */
    private int getDeviceIcon(BluetoothDevice device) {
        if (bluetooth.isAlreadyPaired(device)) {
            return R.drawable.ic_bluetooth_connected_black_24dp;
        } else {
            return R.drawable.ic_bluetooth_black_24dp;
        }
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    @Override
    public void onDeviceDiscovered(BluetoothDevice device) {
        listener.endLoading(true);
        devices.add(device);
        notifyDataSetChanged();
    }

    @Override
    public void onDeviceDiscoveryStarted() {
        cleanView();
        listener.startLoading();
    }

    /**
     * Cleans the view.
     */
    public void cleanView() {
        devices.clear();
        notifyDataSetChanged();
    }

    @Override
    public void setBluetoothController(BluetoothController bluetooth) {
        this.bluetooth = bluetooth;
    }

    @Override
    public void onDeviceDiscoveryEnd() {
        listener.endLoading(false);
    }

    @Override
    public void onBluetoothStatusChanged() {
        // Notifies the Bluetooth controller.
        bluetooth.onBluetoothStatusChanged();
    }

    @Override
    public void onBluetoothTurningOn() {
        listener.startLoading();
    }

    @Override
    public void onDevicePairingEnded() {
        if (bluetooth.isPairingInProgress()) {
            BluetoothDevice device = bluetooth.getBoundingDevice();
            switch (bluetooth.getPairingDeviceStatus()) {
                case BluetoothDevice.BOND_BONDING:
                    // Still pairing, do nothing.
                    break;
                case BluetoothDevice.BOND_BONDED:
                    // Successfully paired.
                    listener.endLoadingWithDialog(false, device);

                    // Updates the icon for this element.
                    notifyDataSetChanged();
                    break;
                case BluetoothDevice.BOND_NONE:
                    // Failed pairing.
                    listener.endLoadingWithDialog(true, device);
                    break;
            }
        }
    }

    /**
     * ViewHolder for a BluetoothDevice.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * The inflated view of this ViewHolder.
         */
        final View mView;

        /**
         * The icon of the device.
         */
        final ImageView mImageView;

        /**
         * The name of the device.
         */
        final TextView mDeviceNameView;

        /**
         * The MAC address of the BluetoothDevice.
         */
        final TextView mDeviceAddressView;

        /**
         * The item of this ViewHolder.
         */
        BluetoothDevice mItem;

        /**
         * Instantiates a new ViewHolder.
         *
         * @param view the inflated view of this ViewHolder.
         */
        ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.device_icon);
            mDeviceNameView = (TextView) view.findViewById(R.id.device_name);
            mDeviceAddressView = (TextView) view.findViewById(R.id.device_address);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + BluetoothController.deviceToString(mItem) + "'";
        }
    }
}
