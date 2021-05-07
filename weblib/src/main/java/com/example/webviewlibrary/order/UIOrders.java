package com.example.webviewlibrary.order;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.webviewlibrary.constant.OrderConstant;
import com.example.webviewlibrary.constant.WebConstant;
import com.example.webviewlibrary.interfaces.IOrder;
import com.example.webviewlibrary.interfaces.ResultCallback;
import com.example.webviewlibrary.util.WebUtils;

import java.util.List;
import java.util.Map;

public class UIOrders extends BaseOrder {

    public UIOrders() {
        super();
        loadOrders();
    }

    private void loadOrders() {
        registerOrder(toastOrder);
        registerOrder(dialogOrder);
    }

    @Override
    int getOrderLvl() {
        return WebConstant.LEVEL_UI;
    }

    /**
     *
     * show a toast
     *
     * */
    private final IOrder toastOrder = new IOrder() {
        @Override
        public String getName() {
            return OrderConstant.ORDER_SHOW_TOAST;
        }

        @Override
        public void exec(Context context, Map params, ResultCallback callback) {
            if(WebUtils.isNotNull(params)) {
                Toast.makeText(context,String.valueOf(params.get("message")),Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     *
     * pop dialog
     *
     * */

    private final IOrder dialogOrder = new IOrder() {
        @Override
        public String getName() {
            return OrderConstant.ORDER_SHOW_DIALOG;
        }

        @Override
        public void exec(Context context, Map params, ResultCallback callback) {
            if(WebUtils.isNotNull(params)) {
                String title = (String) params.get("title");
                String content = (String) params.get("content");
                int canceledOutside = 1;
                if (params.get("canceledOutside") != null) {
                    canceledOutside = Integer.parseInt(params.get("canceledOutside").toString());
                }
                List<Map<String, String>> buttons = (List<Map<String, String>>) params.get("buttons");
                final String callbackName = (String) params.get(WebConstant.WEB2NATIVE_CALLBACk);
                if (!TextUtils.isEmpty(content)) {
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle(title)
                            .setMessage(content)
                            .create();
                    dialog.setCanceledOnTouchOutside(canceledOutside == 1 ? true : false);
                    if (WebUtils.isNotNull(buttons)) {
                        for (int i = 0; i < buttons.size(); i++) {
                            final Map<String, String> button = buttons.get(i);
                            int buttonWhich = getDialogButtonWhich(i);

                            if (buttonWhich == 0) return;

                            dialog.setButton(buttonWhich, button.get("title"), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    button.put(WebConstant.NATIVE2WEB_CALLBACK, callbackName);
                                    callback.onResult(WebConstant.SUCCESS, getName(), button);
                                }
                            });
                        }
                    }
                    dialog.show();
                }
            }

        }

        private int getDialogButtonWhich(int index) {
            switch (index) {
                case 0:
                    return DialogInterface.BUTTON_POSITIVE;
                case 1:
                    return DialogInterface.BUTTON_NEGATIVE;
                case 2:
                    return DialogInterface.BUTTON_NEUTRAL;
            }
            return 0;
        }
    };

}


















