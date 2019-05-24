package ro.pub.cs.systems.eim.lab06.practicaltest02.myapplication;

import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicationThread extends Thread {
    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            /* citesti din socket */
            BufferedReader bufferedReader = Utilities.getReader(socket);

            /* scrii pe socket */
            PrintWriter printWriter = Utilities.getWriter(socket);

            /* citesti pana la \n */
            String line = bufferedReader.readLine();

            /* scrii pe socket */
//            printWriter.println(line);

            /* http */
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet("http://api.geonames.org/earthquakesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&username=pdsd");
            ResponseHandler<String> responseHandlerGet = new BasicResponseHandler();

            String pageSourceCode = httpClient.execute(httpGet, responseHandlerGet);

            if (pageSourceCode == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
                return;
            }

            JSONObject content = new JSONObject(pageSourceCode);
            JSONArray results = content.getJSONArray("earthquakes");

            //  printWriter.println(results.getJSONObject(0).toString());

            /* luam o cheie */
            JSONObject test = results.getJSONObject(0);

            printWriter.println(test.getString("datetime"));

            /* trimiti */
            printWriter.flush();

        } catch (IOException ioException) {

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}