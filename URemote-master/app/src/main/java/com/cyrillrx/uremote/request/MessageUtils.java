package com.cyrillrx.uremote.request;

import com.cyrillrx.logger.Logger;
import com.cyrillrx.uremote.common.device.NetworkDevice;
import com.cyrillrx.uremote.request.protobuf.RemoteCommand.Request;
import com.cyrillrx.uremote.request.protobuf.RemoteCommand.Response;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

/**
 * Class that holds the utils to manage network messages
 *
 * @author Cyril Leroux
 *         Created on 15/05/13.
 */
public class MessageUtils {

    private static final String TAG = MessageUtils.class.getSimpleName();

    /**
     * Sends a request to a device and wait for a reply.<br />
     * This function MUST be called from a background thread.
     *
     * @param request Client request.
     * @param device  The device towards which to send the request.
     * @return The device reply.
     */
    public static Response sendRequest(final Request request, final NetworkDevice device) {

        String errorMessage = "";

        if (device == null) {
            // TODO : to strings.xml
            errorMessage = "Connected device is null. Nowhere to send data";

        } else {
            Socket socket = null;
            try {
                socket = connectToRemoteSocket(device);
                if (socket != null && socket.isConnected()) {
                    return sendRequest(socket, request);
                }
                // TODO : to strings.xml
                errorMessage = "Socket null or not connected";

            } catch (SocketTimeoutException e) {
                // TODO : to strings.xml
                errorMessage = "Can't reach remote device (bad config or server down).";
                Logger.warning(TAG, "Can't reach remote device (" + e.getMessage() + ")");

            } catch (InvalidProtocolBufferException e) {
                errorMessage = "Protocol Buffers exception. Check the log.";
                Logger.error(TAG, "Protocol Buffers exception", e);

            } catch (Exception e) {
                errorMessage = "Unknown exception - " + e;
                Logger.error(TAG, "Unknown exception", e);

            } finally {
                closeSocket(socket);
            }
        }

        return Response.newBuilder()
                .setRequestType(request.getType())
                .setRequestCode(request.getCode())
                .setReturnCode(Response.ReturnCode.RC_ERROR)
                .setMessage(errorMessage)
                .build();
    }

    /**
     * Creates the socket, connects it to the device then returns it.
     *
     * @param device The object that holds device connection settings.
     * @return The socket on which to send the message.
     * @throws IOException exception
     */
    private static Socket connectToRemoteSocket(NetworkDevice device) throws IOException {

        final String host = device.getLocalHost();
        final int port = device.getLocalPort();
        final SocketAddress socketAddress = new InetSocketAddress(host, port);

        Socket socket = new Socket();
        socket.setSoTimeout(device.getReadTimeout());
        socket.connect(socketAddress, device.getConnectionTimeout());

        return socket;
    }

    /**
     * Send a message through a Socket to a device and get the reply.<br />
     * This function MUST be called from a background thread.
     *
     * @param socket  The socket on which to send the message.
     * @param request Client request.
     * @return The device reply.
     * @throws IOException exception.
     */
    private static Response sendRequest(Socket socket, Request request) throws IOException {
        if (!socket.isConnected()) {
            return null;
        }

        //create BAOS for protobuf
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        request.writeDelimitedTo(baos);

        socket.getOutputStream().write(baos.toByteArray());
        socket.getOutputStream().flush();
        socket.shutdownOutput();

        return Response.parseDelimitedFrom(socket.getInputStream());
    }

    /**
     * Closes socket IOs then close the socket.
     */
    private static void closeSocket(Socket socket) {

        if (socket == null) {
            Logger.warning(TAG, "#closeSocketIO - Socket is null.");
            return;
        }

        try {
            socket.shutdownInput();
        } catch (IOException e) {
            Logger.warning(TAG, "#closeSocketIO - On socket.shutdownInput() : " + e);
        }

        try {
            socket.shutdownOutput();
        } catch (IOException e) {
            Logger.warning(TAG, "#closeSocketIO - On socket.shutdownOutput() : " + e);
        }

        try {
            socket.close();
        } catch (IOException e) {
            Logger.warning(TAG, "#closeSocketIO - On socket.close() : " + e);
        }
    }
}
