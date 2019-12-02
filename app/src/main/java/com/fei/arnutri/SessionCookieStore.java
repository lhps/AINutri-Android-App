package com.fei.arnutri;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.util.Log;

import com.fei.arnutri.Api.Consts;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SessionCookieStore implements CookieStore {

    private static final String SESSION = "session";

    private Map<String, Map<String,String>> mapCookies = new HashMap<String, Map<String,String>>();

    private final SharedPreferences sharedPreferences;

    public void add(URI uri, HttpCookie cookie){

        String domain = cookie.getDomain();

        Log.i(SESSION, "adding ( " + domain +", " + cookie.toString());

        Map<String, String> cookies = mapCookies.get(domain);

        if(cookies == null){
            cookies = new HashMap<String, String>();
            mapCookies.put(domain, cookies);
        }

        cookies.put(cookie.getName(), cookie.getValue());

        if(cookie.getName().startsWith("session") && !cookie.getValue().equals("")){
            Log.i(SESSION, "Saving rememberMeCookie = " + cookie.getValue() );

            Editor e = sharedPreferences.edit();
            e.putString(Consts.SESSION_COOKIE, cookie.toString());
            e.commit();

        }
    }


    public SessionCookieStore(Context context){

        sharedPreferences = context.getSharedPreferences(Consts.SESSION_COOKIE, Context.MODE_PRIVATE);
    }


    public List<HttpCookie> get(URI uri) {

        List<HttpCookie> cookieList = new ArrayList<HttpCookie>();

        String domain = uri.getHost();

        // Log.i(LOGTAG, "getting ( " + domain +" )" );

        Map<String,String> cookies = mapCookies.get(domain);
        if (cookies == null) {
            cookies = new HashMap<String, String>();
            mapCookies.put(domain, cookies);
        }

        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            cookieList.add(new HttpCookie(entry.getKey(), entry.getValue()));
            // Log.i(LOGTAG, "returning cookie: " + entry.getKey() + "="+ entry.getValue());
        }
        return cookieList;

    }


    public boolean removeAll() {

        // Log.i(LOGTAG, "removeAll()" );

        mapCookies.clear();
        return true;

    }

    public List<HttpCookie> getCookies() {

        Log.i(SESSION, "getCookies()" );

        Set<String> mapKeys = mapCookies.keySet();

        List<HttpCookie> result = new ArrayList<HttpCookie>();
        for (String key : mapKeys) {
            Map<String,String> cookies =    mapCookies.get(key);
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                result.add(new HttpCookie(entry.getKey(), entry.getValue()));
                Log.i(SESSION, "returning cookie: " + entry.getKey() + "="+ entry.getValue());
            }
        }

        return result;

    }


    public List<URI> getURIs() {

        Log.i(SESSION, "getURIs()" );

        Set<String> keys = mapCookies.keySet();
        List<URI> uris = new ArrayList<URI>(keys.size());
        for (String key : keys){
            URI uri = null;
            try {
                uri = new URI(key);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            uris.add(uri);
        }
        return uris;

    }


    public boolean remove(URI uri, HttpCookie cookie) {

        String domain = cookie.getDomain();

        Log.i(SESSION, "remove( " + domain +", " + cookie.toString() );

        Map<String,String> lstCookies = mapCookies.get(domain);

        if (lstCookies == null)
            return false;

        return lstCookies.remove(cookie.getName()) != null;

    }

}
