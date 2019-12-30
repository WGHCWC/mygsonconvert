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

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * A {@linkplain Converter.Factory converter} which uses Gson for JSON.
 * <p>
 * Because Gson is so flexible in the types it supports, this converter assumes that it can handle
 * all types. If you are mixing JSON serialization with something else (such as protocol buffers),
 * you must {@linkplain Retrofit.Builder#addConverterFactory(Converter.Factory) add this instance}
 * last to allow the other converters a chance to see their types.
 */
public final class MyGsonConvertFactory extends Converter.Factory {
    private ServerErrorHandleListener listener;

    public static MyGsonConvertFactory create() {
        return create( null, new Gson());
    }

    public static MyGsonConvertFactory create(ServerErrorHandleListener listener) {
        return create(listener, new Gson());
    }

    public static MyGsonConvertFactory create(ServerErrorHandleListener listener, Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        if (listener == null) {
            listener = new ServerErrorHandleListener() {
                @Override
                public IOException needThrow(JSONObject jsonObject) throws JSONException {
                    return null;
                }
            };
        }
        return new MyGsonConvertFactory(listener, gson);
    }


    private final Gson gson;

    private MyGsonConvertFactory(ServerErrorHandleListener listener, Gson gson) {
        this.gson = gson;
        this.listener = listener;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (type == RequestBody.class) {
            return null;
        }
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonResponseBodyConverter<>(listener, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return null;
    }
}
