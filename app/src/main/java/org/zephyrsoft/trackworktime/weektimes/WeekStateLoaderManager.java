package org.zephyrsoft.trackworktime.weektimes;

import android.os.AsyncTask;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.zephyrsoft.trackworktime.model.Week;
import org.zephyrsoft.trackworktime.model.WeekState;

import java.util.concurrent.Executor;

/**
 * Manages loading of {@link WeekState}.
 */
public class WeekStateLoaderManager {

	private static final Executor threadPool = AsyncTask.THREAD_POOL_EXECUTOR;

	private final SparseArray<WeekStateLoader> weekStateLoaders = new SparseArray<>();
	private final WeekStateLoaderFactory weekStateLoaderFactory;

	public WeekStateLoaderManager(@NonNull WeekStateLoaderFactory weekStateLoaderFactory) {
		this.weekStateLoaderFactory = weekStateLoaderFactory;
	}

	/**
	 * Request {@link WeekState}, that will be calculated async.
	 * @param week week to calculate {@link WeekState} for
	 * @param requestId request identifier, to identify specific async loader
	 * @return data reference, that will be updated, once {@link WeekState} is ready
	 */
	public @NonNull LiveData<WeekState> requestWeekState(@NonNull Week week, int requestId) {
		MutableLiveData<WeekState> weekStateLiveData = new MutableLiveData<>();
		WeekStateLoader loader = createLoader(week, requestId, weekStateLiveData);
		registerLoader(loader, requestId);
		executeRequest(requestId);
		return weekStateLiveData;
	}

	private WeekStateLoader createLoader(Week week, int requestId,
			MutableLiveData<WeekState> weekStateData) {
		return weekStateLoaderFactory.create(week, weekState -> {
			weekStateData.postValue(weekState);
			cancelRequest(requestId);
		});
	}

	private void registerLoader(@NonNull WeekStateLoader weekStateLoader, int requestId) {
		checkRequestId(requestId);
		weekStateLoaders.put(requestId, weekStateLoader);
	}

	private void checkRequestId(int requestId) {
		if(weekStateLoaders.get(requestId) != null) {
			throw new RuntimeException("Duplicate request id: " + requestId);
		}
	}

	private void executeRequest(int requestId) {
		WeekStateLoader loader = getLoader(requestId);
		loader.executeOnExecutor(threadPool);
	}

	/**
	 * Cancel specific loading request
	 * @param requestId request identifier, to identify async loader, that will be stopped
	 */
	public void cancelRequest(int requestId) {
		WeekStateLoader loader = getLoader(requestId);
		if(loader != null) {
			loader.cancel(true);
			removeLoader(requestId);
		}
	}

	private WeekStateLoader getLoader(int requestId) {
		return weekStateLoaders.get(requestId);
	}

	private void removeLoader(int requestId) {
		weekStateLoaders.remove(requestId);
	}

}
