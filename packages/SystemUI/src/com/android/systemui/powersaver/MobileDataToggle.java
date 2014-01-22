/*
 * Copyright (C) 2014 The KylinMod OpenSource Project
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

package com.android.systemui.powersaver;

import android.content.Context;
import android.provider.Settings;
import android.net.ConnectivityManager;
import android.util.Log;

public class MobileDataToggle extends PowerSaverToggle {

    private static final String TAG = "PowerSaverService_MobileDataToggle";

    public MobileDataToggle(Context context) {
        super(context);
    }

    protected boolean isEnabled() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!cm.isNetworkSupported(ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return Settings.System.getInt(mContext.getContentResolver(), Settings.System.POWER_SAVER_MOBILE_DATA, 0) != 0;
    }

    protected boolean doScreenOnAction() {
        return mDoAction;
    }

    protected boolean doScreenOffAction() {
        if (isMobileDataEnabled()) {
            mDoAction = true;
        } else {
            mDoAction = false;
        }
        return mDoAction;
    }

    private boolean isMobileDataEnabled() {
        ConnectivityManager cm =
                (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getMobileDataEnabled();
    }

    protected Runnable getScreenOffAction() {
        return new Runnable() {
            @Override
            public void run() {
                ConnectivityManager cm =
                    (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                cm.setMobileDataEnabled(false);
                Log.d(TAG, "mobileData = false");
            }
        };
    }
    protected Runnable getScreenOnAction() {
        return new Runnable() {
            @Override
            public void run() {
                ConnectivityManager cm =
                    (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                cm.setMobileDataEnabled(true);
                Log.d(TAG, "mobileData = true");
            }
        };
    }
}