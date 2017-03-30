package graph.sockets.com.sensorsocket.utils;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;


/**
 * Created by sumandas on 29/01/2017.
 */

public class RxUtils {

    public static <T> Observable<T> buildIO(Observable<T> observable) {
        return observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public static <T> Observable<T> buildComputation(Observable<T> observable) {
        return observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }


    public static <T> Observable<T> buildTest(Observable<T> observable) {
        return observable
                .observeOn(Schedulers.immediate())
                .subscribeOn(Schedulers.immediate());
    }


    public static void unSubscribe(CompositeSubscription subscription) {
        if (subscription == null || subscription.isUnsubscribed()) {
            return;
        }
        subscription.unsubscribe();
    }


    @NonNull
    public static <T> Observable<T> toObservable(@NonNull final ObservableField<T> field) {
        return Observable.create(subscriber -> {
            subscriber.onNext(field.get());
            final android.databinding.Observable.OnPropertyChangedCallback callback = new android.databinding.Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(android.databinding.Observable observable, int i) {
                    subscriber.onNext(field.get());
                }
            };
            field.addOnPropertyChangedCallback(callback);
            subscriber.add(Subscriptions.create(() -> field.removeOnPropertyChangedCallback(callback)));
        });
    }

}
