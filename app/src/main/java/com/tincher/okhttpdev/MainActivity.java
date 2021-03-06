package com.tincher.okhttpdev;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.CacheType;
import com.okhttplib.annotation.Encoding;
import com.okhttplib.cookie.PersistentCookieJar;
import com.okhttplib.cookie.cache.SetCookieCache;
import com.okhttplib.cookie.persistence.SharedPrefsCookiePersistor;
import com.okhttplib.interceptor.ExceptionInterceptor;
import com.okhttplib.interceptor.ResultInterceptor;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private boolean debug = BuildConfig.DEBUG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        String downloadFileDir = Environment.getExternalStorageDirectory().getPath() + "/okHttp_download/";
        String cacheDir = Environment.getExternalStorageDirectory().getPath() + "/okHttp_cache";

        ResultInterceptor resultInterceptor = new ResultInterceptor() {
            @Override
            public HttpInfo intercept(HttpInfo info) throws Exception {
                return null;
            }
        };

        ExceptionInterceptor exceptionInterceptor = new ExceptionInterceptor() {
            @Override
            public HttpInfo intercept(HttpInfo info) throws Exception {
                return null;
            }
        };
        OkHttpUtil.init(this.getApplication())
                .setConnectTimeout(15)//连接超时时间
                .setWriteTimeout(15)//写超时时间
                .setReadTimeout(15)//读超时时间
                .setMaxCacheSize(10 * 1024 * 1024)//缓存空间大小
                .setCacheType(CacheType.FORCE_NETWORK)//缓存类型
                .setHttpLogTAG("HttpLog")//设置请求日志标识
                .setIsGzip(false)//Gzip压缩，需要服务端支持
                .setShowHttpLog(true)//显示请求日志
                .setShowLifecycleLog(false)//显示Activity销毁日志
                .setRetryOnConnectionFailure(false)//失败后不自动重连
                .setCachedDir(new File(cacheDir))//设置缓存目录
                .setDownloadFileDir(downloadFileDir)//文件下载保存目录
                .setResponseEncoding(Encoding.UTF_8)//设置全局的服务器响应编码
                .setRequestEncoding(Encoding.UTF_8)//设置全局的请求参数编码
                .addResultInterceptor(debug ? resultInterceptor : null)//请求结果拦截器
                .addInterceptors(new ChuckInterceptor(this))//应用拦截器
                .addExceptionInterceptor(debug ? exceptionInterceptor : null)//请求链路异常拦截器
                .setCookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this)))//持久化cookie
                .build();
    }


}
