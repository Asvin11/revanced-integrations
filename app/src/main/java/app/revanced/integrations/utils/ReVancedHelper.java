package app.revanced.integrations.utils;

import static app.revanced.integrations.utils.ResourceUtils.identifier;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.TextView;

import java.util.Objects;

import app.revanced.integrations.settings.SettingsEnum;

public class ReVancedHelper {
    private static final String DEFAULT_APP_NAME = "ReVanced_Extended";
    private static final int DEFAULT_VERSION_CODE = 1537729984; // 18.19.36
    private static final String DEFAULT_VERSION_NAME = "18.19.36";
    private static final int TARGET_VERSION_CODE = 1538115008; // 18.22.32

    private ReVancedHelper() {
    } // utility class

    public static void test(TextView textview) {
        var id = textview.getId();
    }

    public static String getAppName() {
        var packageInfo = getPackageInfo();
        return packageInfo == null
                ? DEFAULT_APP_NAME
                : (String) packageInfo.applicationInfo.loadLabel(getPackageManager());
    }

    private static PackageInfo getPackageInfo() {
        try {
            var context = Objects.requireNonNull(ReVancedUtils.getContext());
            return getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PackageManager getPackageManager() {
        var context = Objects.requireNonNull(ReVancedUtils.getContext());
        return context.getPackageManager();
    }

    private static int getVersionCode() {
        var packageInfo = getPackageInfo();
        return packageInfo == null
                ? DEFAULT_VERSION_CODE
                : packageInfo.versionCode;
    }

    public static String getVersionName() {
        var packageInfo = getPackageInfo();
        return packageInfo == null
                ? DEFAULT_VERSION_NAME
                : packageInfo.versionName;
    }

    public static String[] getStringArray(Context context, String key) {
        return context.getResources().getStringArray(identifier(key, ResourceType.ARRAY));
    }

    public static boolean isPackageEnabled(Context context, String packageName) {
        boolean packageEnabled = false;

        try {
            assert context != null;
            packageEnabled = context.getPackageManager().getApplicationInfo(packageName, 0).enabled;
        } catch (PackageManager.NameNotFoundException ignored) {}

        return packageEnabled;
    }

    public static boolean isFullscreenHidden() {
        boolean isFullscreenHidden = isTablet() &&
                !SettingsEnum.ENABLE_PHONE_LAYOUT.getBoolean();
        final SettingsEnum[] hideFullscreenSettings = {
                SettingsEnum.ENABLE_TABLET_LAYOUT,
                SettingsEnum.HIDE_QUICK_ACTIONS,
                SettingsEnum.HIDE_FULLSCREEN_PANELS
        };
        for (SettingsEnum s : hideFullscreenSettings) {
            isFullscreenHidden |= s.getBoolean();
        }
        return isFullscreenHidden;
    }

    public static boolean isSupportSplashAnimation() {
        final boolean aboveAndroidS = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S;

        return aboveAndroidS || getVersionCode() >= TARGET_VERSION_CODE;
    }

    public static boolean isTablet() {
        var context = Objects.requireNonNull(ReVancedUtils.getContext());
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }
}