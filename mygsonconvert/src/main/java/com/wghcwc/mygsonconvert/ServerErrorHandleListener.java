package com.wghcwc.mygsonconvert;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author wghcwc
 * @date 19-12-30
 */
public interface ServerErrorHandleListener {
    /**
     * 自定义抛出服务器定义错误
     * @param jsonObject json
     * @param body       body
     * @return 自定义异常抛出
     * @throws JSONException
     */
    IOException needThrow(JSONObject jsonObject, String body) throws JSONException;

}
