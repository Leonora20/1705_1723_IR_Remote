package com.cyrillrx.uremote.network;

import android.os.AsyncTask;

import com.cyrillrx.logger.Logger;
import com.cyrillrx.uremote.common.device.NetworkDevice;
import com.cyrillrx.uremote.request.MessageUtils;
import com.cyrillrx.uremote.request.protobuf.RemoteCommand.Request;
import com.cyrillrx.uremote.request.protobuf.RemoteCommand.Response;
import com.cyrillrx.uremote.utils.TaskCallbacks;

import java.util.concurrent.Semaphore;

/**
 * Class that handle asynchronous messages to send to the server.
 *
 * @author Cyril Leroux
 *         Created before first commit (08/04/12).
 */
public class AsyncMessageMgr extends AsyncTask<Request, int[], Response> {

    private static final String TAG = AsyncMessageMgr.class.getSimpleName();

    protected static Semaphore semaphore = new Semaphore(2, true);

    protected final NetworkDevice remoteDevice;
    private final TaskCallbacks taskCallbacks;

    /**
     * @param device        The device towards which to send the request.
     * @param taskCallbacks An object to call back during the task lifecycle
     */
    public AsyncMessageMgr(NetworkDevice device, TaskCallbacks taskCallbacks) {
        remoteDevice = device;
        this.taskCallbacks = taskCallbacks;
    }

    @Override
    protected void onPreExecute() {
        try {
            semaphore.acquire();
            Logger.info(TAG, "#onPreExecute - Semaphore acquire. " + semaphore.availablePermits() + " left.");
        } catch (InterruptedException e) {
            Logger.error(TAG, "#onPreExecute - Semaphore acquire error.");
        }

        if (taskCallbacks != null) {
            taskCallbacks.onPreExecute();
        }
    }

    @Override
    protected Response doInBackground(Request... requests) {
        return MessageUtils.sendRequest(requests[0], remoteDevice);
    }

    /**
     * Runs on the UI thread after {@link #doInBackground(Request...)}.
     * The specified result is the value returned by {@link #doInBackground(Request...)}.
     * This method won't be invoked if the task was canceled.
     * It releases the semaphore acquired in OnPreExecute method.
     *
     * @param response The response from the server returned by {@link #doInBackground(Request...)}.
     */
    @Override
    protected void onPostExecute(Response response) {
        semaphore.release();
        Logger.info(TAG, "Semaphore release");

        if (response == null) {
            Logger.error(TAG, "#onPostExecute - Response is null.");
            return;
        }

        if (!response.getMessage().isEmpty()) {
            Logger.info(TAG, "#onPostExecute - message : " + response.getMessage());
        } else {
            Logger.info(TAG, "#onPostExecute - empty message.");
        }

        if (taskCallbacks != null) {
            taskCallbacks.onPostExecute(response);
        }
    }

    @Override
    protected void onProgressUpdate(int[]... values) {
        if (taskCallbacks != null) {
            taskCallbacks.onProgressUpdate(values[0][0]);
        }
    }

    @Override
    protected void onCancelled(Response response) {
        if (taskCallbacks != null) {
            taskCallbacks.onCancelled(response);
        }
    }

    /**
     * @return The count of available permits.
     */
    public static int availablePermits() { return semaphore.availablePermits(); }
}