package com.cdbbbsp.www.Dagger;

import com.cdbbbsp.www.Activity.Main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by 李晓军 on 2017/7/12.
 */
@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity activity);
}
