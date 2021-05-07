package com.example.webviewlibrary.order;

import android.content.Context;

import com.example.webviewlibrary.constant.WebConstant;
import com.example.webviewlibrary.interfaces.IOrder;
import com.example.webviewlibrary.interfaces.ResultCallback;

import java.util.Map;

public class OrderManager {

    private static OrderManager instance;

    private UIOrders uiOrders;

    private OrderManager() {
        uiOrders = new UIOrders();
    }

    public static OrderManager getInstance() {
        if(instance == null) {
            synchronized (OrderManager.class) {
                instance = new OrderManager();
            }
        }
        return instance;
    }

    /**
     *
     * 动态注册order
     *
     * */

    public void registerOrders(int orderLvl, IOrder order){
        switch (orderLvl) {
            case WebConstant.LEVEL_UI:
                uiOrders.registerOrder(order);
                break;
            case WebConstant.LEVEL_BASE:
                break;
            case WebConstant.LEVEL_ACCOUNT:
                break;
        }
    }

    /**
     *
     * execute ui orders
     *
     * */

    public void findAndExecUiOrder(Context context, int lvl, String orderName, Map params, ResultCallback callback) {
        if(uiOrders.getOrders().get(orderName) != null) {
            uiOrders.getOrders().get(orderName).exec(context, params, callback);
        }
    }

    public boolean isUiOrder(int lvl,String orderName) {
        return uiOrders.getOrders().get(orderName) != null;
    }


}




















