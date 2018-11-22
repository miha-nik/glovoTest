package com.example.emptytest.testglovo.presentation.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

public class Utils
  {
    public static boolean isInternetConnected(Context context)
    {
      ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

      return
              connectivityManager!=null&&
                      (
                              connectivityManager.getNetworkInfo(0)!=null&&
                                      (
                                              connectivityManager.getNetworkInfo(0).getState()==android.net.NetworkInfo.State.CONNECTED||
                                                      connectivityManager.getNetworkInfo(0).getState()==android.net.NetworkInfo.State.CONNECTING
                                      )||
                                      connectivityManager.getNetworkInfo(1)!=null&&
                                              (
                                                      connectivityManager.getNetworkInfo(1).getState()==android.net.NetworkInfo.State.CONNECTING||
                                                              connectivityManager.getNetworkInfo(1).getState()==android.net.NetworkInfo.State.CONNECTED
                                              )
                      );
    }

      public static double[] decodeLocation(String base64Encoded) {

          double[] d = null;
          try {
              byte b[] = base64Encoded.getBytes("utf-8");
              d = byteToDoubleArray(Base64.decode(base64Encoded.getBytes("utf-8"), Base64.NO_CLOSE));

          } catch (UnsupportedEncodingException e) {
              e.printStackTrace();
          }

          return d;
      }
      private static double[] byteToDoubleArray(byte[] bytes) {
          DoubleBuffer buf = ByteBuffer.wrap(bytes).asDoubleBuffer();
          double[] doubleArray = new double[buf.limit()];
          buf.get(doubleArray);
          return doubleArray;
      }

      public static byte[] getData(Context context, final String filename)
      {
          int size=0;
          byte[] buffer=null;
          InputStream is;

          try
          {
              int readden=0;

              is=context.getAssets().open(filename);
              size=is.available();
              buffer=new byte[size];
              readden=is.read(buffer);
              is.close();
              if(readden!=size)buffer=null;
          }
          catch(IOException e)
          {
              e.printStackTrace();
          }
          return buffer;
      }
  }
