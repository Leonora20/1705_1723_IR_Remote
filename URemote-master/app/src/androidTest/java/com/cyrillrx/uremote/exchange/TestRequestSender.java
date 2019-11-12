package com.cyrillrx.uremote.exchange;

import android.test.InstrumentationTestCase;

import com.cyrillrx.uremote.common.device.NetworkDevice;
import com.cyrillrx.uremote.network.AsyncMessageMgr;
import com.cyrillrx.uremote.request.protobuf.RemoteCommand;
import com.cyrillrx.logger.Logger;

import junit.framework.Assert;

import java.util.concurrent.ExecutionException;

/**
 * @author Cyril Leroux
 *         Created on 15/05/2014.
 */
public class TestRequestSender extends InstrumentationTestCase {

    private static final String TAG = TestRequestSender.class.getSimpleName();
    private static final String SECURITY_TOKEN = "123";

    NetworkDevice mDevice;

    //    @Before
    @Override
    protected void setUp() throws Exception {

        mDevice = NetworkDevice.newBuilder()
                .setName("TestDevice")
                .setLocalHost("localhost")
                .setLocalPort(10000)
                .setBroadcast("192.168.0.255")
                .setRemoteHost("localhost")
                .setRemotePort(20000)
                .setMacAddress("00-FF-50-15-86-07")
                .setConnectionTimeout(500)
                .setReadTimeout(500)
                .setSecurityToken(SECURITY_TOKEN)
                .setConnectionType(NetworkDevice.ConnectionType.LOCAL)
                .build();
    }

    //    @Test
    public void testRequest() {
        sendRequest(RemoteCommand.Request.newBuilder()
                .setSecurityToken(SECURITY_TOKEN)
                .setType(RemoteCommand.Request.Type.KEYBOARD)
                .setCode(RemoteCommand.Request.Code.DEFINE)
                .setStringExtra("A")
                .build());
    }

    /**
     * Initializes the async task manager then send a request with it.
     *
     * @param request The request to send.
     */
    public void sendRequest(RemoteCommand.Request request) {

        if (AsyncMessageMgr.availablePermits() > 0) {
            AsyncMessageMgr task = new AsyncMessageMgr(mDevice, null);
            task.execute(request);
            Logger.info(TAG, "#sendRequest - Command sent.");

            try {
                checkResponse(task.get());
            } catch (InterruptedException e) {
                Assert.fail("#sendRequest - InterruptedException : " + e.getMessage());
            } catch (ExecutionException e) {
                Assert.fail("#sendRequest - ExecutionException : " + e.getMessage());
            }

        } else {
            Assert.fail("#sendRequest - No more permit available!");
        }
    }

    /**
     * Checks the response validity.
     *
     * @param response
     */
    private static void checkResponse(RemoteCommand.Response response) {

        if (RemoteCommand.Response.ReturnCode.RC_ERROR.equals(response.getReturnCode())) {
            Logger.error(TAG, "#onPostExecute - response : " + response);
        } else {
            Logger.info(TAG, "#onPostExecute - response : " + response);
        }

        Assert.assertEquals(
                "Request failed : " + response.getMessage(),
                RemoteCommand.Response.ReturnCode.RC_SUCCESS,
                response.getReturnCode());

    }
}
