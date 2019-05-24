package ro.pub.cs.systems.eim.lab06.practicaltest02.myapplication;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private String address;
    private int port;
    private String text;
    private Socket socket;
    private TextView  textView = null;

    public ClientThread(String address, int port, String text, TextView textView) {
        this.address = address;
        this.port = port;
        this.text = text;
        this.textView = textView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            /* send to server */
            printWriter.println(text);
            printWriter.flush();

            /* wait 4 response */
            String information;
            while ((information = bufferedReader.readLine()) != null) {
                final String finalizedInformation = information;
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(finalizedInformation);
                    }
                });
            }

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}