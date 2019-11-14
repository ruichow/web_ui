package com.hand.utils;


import org.openqa.selenium.Capabilities;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.remote.RemoteTouchScreen;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

/**
 * @author：关河九州
 * @date：2019/11/14 16:36
 * @version：1.0
 */
public class MyRemoteWebDriver extends RemoteWebDriver implements HasTouchScreen {
    public TouchScreen touchScreen;

    public MyRemoteWebDriver(URL remoteAddress, Capabilities capabilities){
        super(remoteAddress, capabilities);
        this.touchScreen = new RemoteTouchScreen(this.getExecuteMethod());
    }

    public TouchScreen getTouch() {
        return this.touchScreen;
    }
}
