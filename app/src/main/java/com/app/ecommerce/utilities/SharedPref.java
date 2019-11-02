package com.app.ecommerce.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.app.ecommerce.R;

public class SharedPref {

    private Context ctx;
    private SharedPreferences default_prefence;

    public SharedPref(Context context) {
        this.ctx = context;
        default_prefence = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private String str(int string_id) {
        return ctx.getString(string_id);
    }

    //1.Name
    public void setYourName(String name) {
        default_prefence.edit().putString(str(R.string.pref_title_name), name).apply();
    }

    public String getYourName() {
        return default_prefence.getString(str(R.string.pref_title_name), str(R.string.default_your_name));
    }


    //2.Email
    public void setYourEmail(String name) {
        default_prefence.edit().putString(str(R.string.pref_title_email), name).apply();
    }

    public String getYourEmail() {
        return default_prefence.getString(str(R.string.pref_title_email), str(R.string.default_your_email));
    }

    //3.Phone
    public void setYourPhone(String name) {
        default_prefence.edit().putString(str(R.string.pref_title_phone), name).apply();
    }

    public String getYourPhone() {
        return default_prefence.getString(str(R.string.pref_title_phone), str(R.string.default_your_phone));
    }

    //4.Shop Address
    public void setYourShopAddress(String name) {
        default_prefence.edit().putString(str(R.string.pref_title_shopAddress), name).apply();
    }

    public String getYourShopAddress() {
        return default_prefence.getString(str(R.string.pref_title_shopAddress), str(R.string.default_your_shopAddress));
    }

    //5.Shop Name
    public void setYourShopName(String shopName) {
        default_prefence.edit().putString(str(R.string.pref_title_shopName), shopName).apply();
    }

    public String getYourShopName() {
        return default_prefence.getString(str(R.string.pref_title_shopName), str(R.string.default_your_shopName));
    }

    //5.NID
    public void setYourNID(String nid) {
        default_prefence.edit().putString(str(R.string.pref_title_NID), nid).apply();
    }

    public String getYourNID() {
        return default_prefence.getString(str(R.string.pref_title_NID), str(R.string.default_your_NID));
    }


}
