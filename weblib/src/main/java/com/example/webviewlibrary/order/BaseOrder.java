package com.example.webviewlibrary.order;

import com.example.webviewlibrary.interfaces.IOrder;

import java.util.HashMap;

public abstract class BaseOrder {
    private final HashMap<String, IOrder> orders;

    protected BaseOrder() {
        this.orders = new HashMap<String, IOrder>();
    }

    abstract int getOrderLvl();

    public HashMap<String,IOrder> getOrders() {
        return orders;
    }

    protected void registerOrder(IOrder iOrder) {
        orders.put(iOrder.getName(),iOrder);
    }



}
