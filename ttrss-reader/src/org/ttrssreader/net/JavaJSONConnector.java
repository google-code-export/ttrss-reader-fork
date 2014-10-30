/*
 * ttrss-reader-fork for Android
 * 
 * Copyright (C) 2010 Nils Braden
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 3 as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 */

package org.ttrssreader.net;

import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.SocketException;
import java.net.URL;
import java.util.Map;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import org.json.JSONObject;
import org.ttrssreader.controllers.Controller;
import org.ttrssreader.utils.Utils;
import android.content.Context;
import android.util.Log;

public class JavaJSONConnector extends JSONConnector {
    
    protected static final String TAG = JavaJSONConnector.class.getSimpleName();
    
    public JavaJSONConnector(Context context) {
        super(context);
    }
    
    protected InputStream doRequest(Map<String, String> params) {
        try {
            if (sessionId != null)
                params.put(SID, sessionId);
            
            JSONObject json = new JSONObject(params);
            byte[] outputBytes = json.toString().getBytes("UTF-8");
            
            logRequest(json);
            refreshHTTPAuth();
            
            URL url = Controller.getInstance().url();
            url = new URL("https://ttrss.example.de/");
            
            // Create Connection
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // DEBUG: ((HttpsURLConnection)con).setSSLSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault());
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Length", Integer.toString(outputBytes.length));
            
            int timeoutSocket = (int) ((Controller.getInstance().lazyServer()) ? 15 * Utils.MINUTE : 10 * Utils.SECOND);
            con.setReadTimeout(timeoutSocket);
            con.setConnectTimeout((int) (8 * Utils.SECOND));
            
            // Add POST data
            con.getOutputStream().write(outputBytes);
            // DEBUG: ((HttpsURLConnection)con).getSSLSocketFactory().getSupportedCipherSuites();
            // DEBUG: ((HttpsURLConnection)con).getCipherSuite();
            
            // Try to check for HTTP Status codes
            int code = con.getResponseCode();
            if (code >= 400 && code < 600) {
                hasLastError = true;
                lastError = "Server returned status: " + code + " (Message: " + con.getResponseMessage() + ")";
                return null;
            }
            return con.getInputStream();
            
        } catch (SSLPeerUnverifiedException e) {
            // Probably related: http://stackoverflow.com/questions/6035171/no-peer-cert-not-sure-which-route-to-take
            // Not doing anything here since this error should happen only when no certificate is received from the
            // server.
            Log.w(TAG, "SSLPeerUnverifiedException in doRequest(): " + formatException(e));
        } catch (SSLException e) {
            e.printStackTrace();
            if ("No peer certificate".equals(e.getMessage())) {
                // Handle this by ignoring it, this occurrs very often when the connection is instable.
                Log.w(TAG, "SSLException in doRequest(): " + formatException(e));
            } else {
                hasLastError = true;
                lastError = "SSLException in doRequest(): " + formatException(e);
            }
        } catch (InterruptedIOException e) {
            Log.w(TAG, "InterruptedIOException in doRequest(): " + formatException(e));
        } catch (SocketException e) {
            // http://stackoverflow.com/questions/693997/how-to-set-httpresponse-timeout-for-android-in-java/1565243#1565243
            Log.w(TAG, "SocketException in doRequest(): " + formatException(e));
        } catch (Exception e) {
            hasLastError = true;
            lastError = "Exception in doRequest(): " + formatException(e);
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    protected void refreshHTTPAuth() {
        super.refreshHTTPAuth();
        if (!httpAuth)
            return;
        
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(httpUsername, httpPassword.toCharArray());
            }
        });
    }
    
}
