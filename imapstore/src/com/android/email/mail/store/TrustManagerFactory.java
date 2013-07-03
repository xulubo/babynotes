/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.email.mail.store;

import android.net.http.DomainNameChecker;
import android.util.Log;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;

import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public final class TrustManagerFactory {
    private static X509TrustManager sUnsecureTrustManager
            = new SimpleX509TrustManager();
    private static KeyStore keyStore;
    private static X509TrustManager localTrustManager;
    private static X509TrustManager defaultTrustManager;

    static
    {
      try
      {
        javax.net.ssl.TrustManagerFactory tmf = javax.net.ssl.TrustManagerFactory.getInstance("X509");
        keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try {
          keyStore.load(null, "".toCharArray());
        } catch (IOException e) {
          Log.e("TrustManagerFactory", "KeyStore IOException while initializing TrustManagerFactory ", e);
          keyStore = null;
        } catch (CertificateException e) {
          Log.e("TrustManagerFactory", "KeyStore CertificateException while initializing TrustManagerFactory ", e);
          keyStore = null;
        }
        tmf.init(keyStore);
        TrustManager[] tms = tmf.getTrustManagers();
        if (tms != null) {
          for (TrustManager tm : tms) {
            if ((tm instanceof X509TrustManager)) {
              localTrustManager = (X509TrustManager)tm;
              break;
            }
          }
        }
        tmf = javax.net.ssl.TrustManagerFactory.getInstance("X509");
        tmf.init((ManagerFactoryParameters)null);
        tms = tmf.getTrustManagers();
        if (tms != null) {
          for (TrustManager tm : tms)
            if ((tm instanceof X509TrustManager)) {
              defaultTrustManager = (X509TrustManager)tm;
              break;
            }
        }
      }
      catch (NoSuchAlgorithmException e)
      {
        Log.e("TrustManagerFactory", "Unable to get X509 Trust Manager ", e);
      } catch (KeyStoreException e) {
        Log.e("TrustManagerFactory", "Key Store exception while initializing TrustManagerFactory ", e);
      } catch (InvalidAlgorithmParameterException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
    
    private static class SimpleX509TrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    private static class SecureX509TrustManager implements X509TrustManager {
        private X509TrustManager mTrustManager;
        private String mHost;

        SecureX509TrustManager(X509TrustManager trustManager, String host) {
            mTrustManager = trustManager;
            mHost = host;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            mTrustManager.checkClientTrusted(chain, authType);
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {

            mTrustManager.checkServerTrusted(chain, authType);

            if (!DomainNameChecker.match(chain[0], mHost)) {
                throw new CertificateException("Certificate domain name does not match " 
                        + mHost);
            }
        }

        public X509Certificate[] getAcceptedIssuers() {
            return mTrustManager.getAcceptedIssuers();
        }
    }

    private TrustManagerFactory() {
    }

    public static X509TrustManager get(String host, boolean secure) {
        return secure ? new SecureX509TrustManager(
        		defaultTrustManager, host) 
                : sUnsecureTrustManager;
    }
}
