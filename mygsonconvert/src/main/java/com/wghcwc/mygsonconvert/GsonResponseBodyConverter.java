/*
 * Copyright (C) 2015 Square, Inc.
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
package com.wghcwc.mygsonconvert;

import com.google.gson.TypeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;
    private ServerErrorHandleListener listener;


    GsonResponseBodyConverter(ServerErrorHandleListener listener, TypeAdapter<T> adapter) {
        this.adapter = adapter;
        this.listener = listener;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String json = value.string();
        try {
            JSONObject object = new JSONObject(json);
            IOException serverException = listener.needThrow(object,json);
            if (serverException == null) {
                return adapter.fromJson(json);
            } else {
                throw serverException;
            }
        } catch (JSONException e) {
            throw new ServiceErrorException(e);
        } finally {
            value.close();
        }
    }

}
