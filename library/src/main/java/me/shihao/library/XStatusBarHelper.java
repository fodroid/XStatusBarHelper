package me.shihao.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * Created by shihao on 2017/2/20.
 */

public class XStatusBarHelper {

    private static float DEFAULT_ALPHA = 0.2f;

    /**
     * 设置默认值
     *
     * @param alpha
     */
    public static void setDefaultAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        DEFAULT_ALPHA = alpha;
    }

    /**
     * Android4.4以上的沉浸式全屏模式
     *
     * @param activity Activity对象
     * @see {@link #immersiveStatusBar(Window, float)}
     */
    public static void immersiveStatusBar(Activity activity) {
        immersiveStatusBar(activity, DEFAULT_ALPHA);
    }

    /**
     * Android4.4以上的沉浸式全屏模式
     *
     * @param activity Activity对象
     * @param alpha    透明栏透明度[0.0-1.0]
     * @see {@link #immersiveStatusBar(Window, float)}
     */
    public static void immersiveStatusBar(Activity activity, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        immersiveStatusBar(activity.getWindow(), alpha);
    }

    /**
     * Android4.4以上的沉浸式全屏模式
     *
     * @param window 一般都是用于Activity的window,也可以是其他的例如Dialog,DialogFragment
     * @see {@link #immersiveStatusBar(Window, float)}
     */
    public static void immersiveStatusBar(Window window) {
        immersiveStatusBar(window, DEFAULT_ALPHA);
    }

    /**
     * Android4.4以上的沉浸式全屏模式
     * <p>
     * * 注:
     * 1.删除fitsSystemWindows属性:Android5.0以上使用该方法如果出现界面展示不正确,删除布局中所有fitsSystemWindows属性
     * 或者调用forceFitsSystemWindows方法
     * 2.不删除fitsSystemWindows属性:也可以区别处理,Android5.0以上使用自己的方式实现,不调用该方法
     *
     * @param window 一般都是用于Activity的window,也可以是其他的例如Dialog,DialogFragment
     * @param alpha  透明栏透明度[0.0-1.0]
     */
    public static void immersiveStatusBar(Window window, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        ViewGroup decorView = (ViewGroup) window.getDecorView();
        ViewGroup contentView = (ViewGroup) window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
        View rootView = contentView.getChildAt(0);
        int statusBarHeight = getStatusBarHeight(window.getContext());
        if (rootView != null) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) rootView.getLayoutParams();
            ViewCompat.setFitsSystemWindows(rootView, true);
            lp.topMargin = -statusBarHeight;
            rootView.setLayoutParams(lp);
        }

        setTranslucentView(decorView, alpha);
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

    /**
     * Android4.4以上的状态栏着色
     *
     * @param activity       Activity对象
     * @param statusBarColor 状态栏颜色
     * @see {@link #tintStatusBar(Window, int, float)}
     */
    public static void tintStatusBar(Activity activity, @ColorInt int statusBarColor) {
        tintStatusBar(activity, statusBarColor, DEFAULT_ALPHA);
    }

    /**
     * Android4.4以上的状态栏着色
     *
     * @param activity       Activity对象
     * @param statusBarColor 状态栏颜色
     * @param alpha          透明栏透明度[0.0-1.0]
     * @see {@link #tintStatusBar(Window, int, float)}
     */
    public static void tintStatusBar(Activity activity, @ColorInt int statusBarColor, @FloatRange(from = 0.0, to = 1.0)
            float alpha) {
        tintStatusBar(activity.getWindow(), statusBarColor, alpha);
    }

    /**
     * Android4.4以上的状态栏着色
     *
     * @param window         一般都是用于Activity的window,也可以是其他的例如Dialog,DialogFragment
     * @param statusBarColor 状态栏颜色
     * @see {@link #tintStatusBar(Window, int, float)}
     */
    public static void tintStatusBar(Window window, @ColorInt int statusBarColor) {
        tintStatusBar(window, statusBarColor, DEFAULT_ALPHA);
    }

    /**
     * Android4.4以上的状态栏着色
     *
     * @param window         一般都是用于Activity的window,也可以是其他的例如Dialog,DialogFragment
     * @param statusBarColor 状态栏颜色
     * @param alpha          透明栏透明度[0.0-1.0]
     */
    public static void tintStatusBar(Window window, @ColorInt int statusBarColor, @FloatRange(from = 0.0, to = 1.0)
            float alpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        ViewGroup decorView = (ViewGroup) window.getDecorView();
        ViewGroup contentView = (ViewGroup) window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
        View rootView = contentView.getChildAt(0);
        if (rootView != null) {
            //设置为true后，布局会自动加上状态栏高度的距离
            ViewCompat.setFitsSystemWindows(rootView, true);
        }
        //设置一个状态栏
        setStatusBar(decorView, statusBarColor, true);
        //设置一个半透明效果，Material Design的效果
        setTranslucentView(decorView, alpha);
    }

    /**
     * Android4.4以上的状态栏着色(针对于DrawerLayout)
     *
     * @param activity       Activity对象
     * @param drawerLayout   DrawerLayout对象
     * @param statusBarColor 状态栏颜色
     * @see {@link #tintStatusBarForDrawer(Activity, DrawerLayout, int, float)}
     */
    public static void tintStatusBarForDrawer(Activity activity, DrawerLayout drawerLayout, @ColorInt int
            statusBarColor) {
        tintStatusBarForDrawer(activity, drawerLayout, statusBarColor, DEFAULT_ALPHA);
    }

    /**
     * Android4.4以上的状态栏着色(针对于DrawerLayout)
     * 注:
     * 1.如果出现界面展示不正确,删除布局中所有fitsSystemWindows属性,尤其是DrawerLayout的fitsSystemWindows属性
     * 2.可以版本判断在5.0以上不调用该方法,使用系统自带
     *
     * @param activity       Activity对象
     * @param drawerLayout   DrawerLayout对象
     * @param statusBarColor 状态栏颜色
     * @param alpha          透明栏透明度[0.0-1.0]
     */
    public static void tintStatusBarForDrawer(Activity activity, DrawerLayout drawerLayout, @ColorInt int
            statusBarColor,
                                              @FloatRange(from = 0.0, to = 1.0) float alpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }

        Window window = activity.getWindow();
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        ViewGroup drawContent = (ViewGroup) drawerLayout.getChildAt(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            drawerLayout.setStatusBarBackgroundColor(statusBarColor);

            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setStatusBar(decorView, statusBarColor, true, true);
        setTranslucentView(decorView, alpha);

        drawerLayout.setFitsSystemWindows(false);
        drawContent.setFitsSystemWindows(true);
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawer.setFitsSystemWindows(false);
    }

    /**
     * 创建假的状态栏View
     */
    private static void setStatusBar(ViewGroup container, @ColorInt int statusBarColor, boolean visible, boolean
            addToFirst) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View statusBarView = container.findViewById(R.id.statusbar_view);
            if (statusBarView == null) {
                statusBarView = new View(container.getContext());
                statusBarView.setId(R.id.statusbar_view);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(container.getContext()));
                if (addToFirst) {
                    container.addView(statusBarView, 0, lp);
                } else {
                    container.addView(statusBarView, lp);
                }
            }

            statusBarView.setBackgroundColor(statusBarColor);
            statusBarView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 创建假的状态栏View
     */
    private static void setStatusBar(ViewGroup container, @ColorInt int statusBarColor, boolean visible) {
        setStatusBar(container, statusBarColor, visible, false);
    }

    /**
     * 创建透明状态栏，实现Material Design的效果
     */
    private static void setTranslucentView(ViewGroup container,
                                           @FloatRange(from = 0.0, to = 1.0) float alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View translucentView = container.findViewById(R.id.translucent_view);
            if (translucentView == null) {
                translucentView = new View(container.getContext());
                translucentView.setId(R.id.translucent_view);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(container.getContext()));
                container.addView(translucentView, lp);
            }

            translucentView.setBackgroundColor(Color.argb((int) (alpha * 255), 0, 0, 0));
        }
    }

    /**
     * 增加View的高度以及paddingTop,增加的值为状态栏高度
     *
     * @param context
     * @param view    一般沉浸式全屏模式下用于Toolbar或者自定义的标题栏
     */
    public static void setHeightAndPadding(Context context, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height += getStatusBarHeight(context);//增高
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    /**
     * 增加View的paddingTop,增加的值为状态栏高度
     *
     * @param context
     * @param view    一般沉浸式全屏模式下用于Toolbar或者自定义的标题栏
     */
    public static void setPadding(Context context, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    /**
     * 设置view的高度，最终高度为view加上状态栏高度
     *
     * @param context
     * @param view    一般沉浸式全屏模式下用于Toolbar或者自定义的标题栏
     */
    public static void setHeight(Context context, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height += getStatusBarHeight(context);//增高
        }
    }

    /**
     * 设置状态栏darkMode,字体颜色及icon变黑(目前支持MIUI6以上,Flyme4以上,Android M以上)
     */
    public static void setStatusBarDarkMode(Activity activity) {
        setStatusBarDarkMode(activity.getWindow());
    }

    /**
     * 设置状态栏为darkMode,字体颜色及icon变黑(目前支持MIUI6以上,Flyme4以上,Android M以上)
     */
    public static void setStatusBarDarkMode(Window window) {
        if (isFlyme4Later()) {
            setStatusBarDarkModeForFlyme4(window, true);
        } else if (isMIUI6Later()) {
            setStatusBarDarkModeForMIUI6(window, true);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarDarkModeForM(window);
        }
    }

    /**
     * android 6.0上设置字体颜色
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void setStatusBarDarkModeForM(Window window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

        int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
        systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    /**
     * 设置Flyme4+的状态栏是否为darkMode模式
     * 来源：http://open-wiki.flyme.cn/index.php?title=Flyme%E7%B3%BB%E7%BB%9FAPI
     */
    public static boolean setStatusBarDarkModeForFlyme4(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams e = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(e);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }

                meizuFlags.setInt(e, value);
                window.setAttributes(e);
                result = true;
            } catch (Exception var8) {
                android.util.Log.e("StatusBar", "setStatusBarDarkIcon: failed");
            }
        }

        return result;
    }

    /**
     * 设置MIUI6+的状态栏是否为darkMode
     * 来源：http://dev.xiaomi.com/doc/p=4769/
     */
    public static void setStatusBarDarkModeForMIUI6(Window window, boolean darkmode) {
        Class<? extends Window> clazz = window.getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断系统是否Flyme4以上
     */
    public static boolean isFlyme4Later() {
        return Build.FINGERPRINT.contains("Flyme_OS_4")
                || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4")
                || Pattern.compile("Flyme OS [4|5]", Pattern.CASE_INSENSITIVE).matcher(Build.DISPLAY).find();
    }

    /**
     * 判断系统是否为MIUI6以上
     */
    public static boolean isMIUI6Later() {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method mtd = clz.getMethod("get", String.class);
            String val = (String) mtd.invoke(null, "ro.miui.ui.version.name");
            val = val.replaceAll("[vV]", "");
            int version = Integer.parseInt(val);
            return version >= 6;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 强制将Activity绑定的xml下面的子View的FitsSystemWindows为false
     */
    public static void forceFitsSystemWindows(Activity activity) {
        forceFitsSystemWindows(activity.getWindow());
    }

    /**
     * 强制将Window绑定的xml下面的子View的FitsSystemWindows为false
     *
     * @param window
     */
    public static void forceFitsSystemWindows(Window window) {
        forceFitsSystemWindows((ViewGroup) window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT));
    }

    /**
     * 强制将ViewGroup下面的子View的FitsSystemWindows为false
     *
     * @param viewGroup
     */
    public static void forceFitsSystemWindows(ViewGroup viewGroup) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup) {
                    forceFitsSystemWindows((ViewGroup) view);
                } else {
                    if (ViewCompat.getFitsSystemWindows(view)) {
                        ViewCompat.setFitsSystemWindows(view, false);
                    }
                }
            }
        }
    }
}
