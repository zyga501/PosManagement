package com.posmanagement.autorun;

import com.posmanagement.policy.SalemanFeePolicy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class NucleonEventProcessor extends HttpServlet {
    public void init() throws ServletException {
        FutureTask<String> task = new FutureTask<String>(new Callable<String>(){
            @Override
            public String call() throws Exception {
                start();
                return "Collection Completed";
            }
        });
        new Thread(task).start();
    }

    private void start() {
        Calendar date = Calendar.getInstance();
        date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), 0, 0, 0);
        long daySpan = 24 * 60 * 60 * 1000;
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                try {
                    new SalemanFeePolicy().updateSalemanFee();
                }
                catch (Exception exception) {

                }
            }
        }, date.getTime(), daySpan);
    };
}
