package com.java.xiongzeen.service;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class TaskRunner {

    @FunctionalInterface
    public interface Callback<R> { //这个接口来自2022年科协暑培的代码
        void complete(Result<R> res);
    }

    public static class Result<R> { //这个函数来自2022年科协暑培的代码
        private final boolean ok;
        private final R result;
        private final Throwable error;

        public Result(boolean b, R res, Throwable o) {
            ok = b;
            result = res;
            error = o;
        }

        public static <T> Result<T> ofResult(T res) {
            return new Result<>(true, res, null);
        }

        public static <T> Result<T> ofError(Throwable error) {
            return new Result<>(false, null, error);
        }

        public boolean isOk() {
            return ok;
        }

        public R getResult() {
            return result;
        }

        public Throwable getError() {
            return error;
        }
    }

    private final static TaskRunner instance = new TaskRunner();
    private final Executor workers = Executors.newSingleThreadExecutor();
    private final Handler uiThread = new Handler(Looper.getMainLooper());

    private TaskRunner() { //这个函数来自2022年科协暑培的代码
    }

    public static TaskRunner getInstance() {
        return instance;
    }

    public <R> void execute(Callable<R> task, Callback<R> callback) { //这个函数来自2022年科协暑培的代码
        workers.execute(() -> {
            try {
                final R res = task.call();
                if (res != null) {
                    uiThread.post(() -> callback.complete(Result.ofResult(res)));
                    Log.d("TaskRunner", "News fetch succeeded.");
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                uiThread.post(() -> callback.complete(Result.ofError(e)));
                Log.e("TaskRunner", "News fetch failed.");
            }
        });
    }
}
