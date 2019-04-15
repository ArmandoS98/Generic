package com.santos.generic.Utils.Async;

import android.os.AsyncTask;
import android.util.Log;

import com.santos.generic.Utils.Persistence.TareaDao;
import com.santos.generic.Utils.TasksG;

public class UpdateAsyncTask extends AsyncTask<TasksG, Void, Void> {
    private static final String TAG = "InsertAsyncTask";
    private TareaDao mTareaDao;

    public UpdateAsyncTask(TareaDao dao) {
        mTareaDao = dao;
    }

    @Override
    protected Void doInBackground(TasksG... tasksGS) {
        Log.d(TAG, "doInBackground: thread: " + Thread.currentThread().getName());
        mTareaDao.actualizar(tasksGS);
        return null;
    }
}
