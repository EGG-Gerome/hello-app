package cloud.tangyuan.helloconsumer;

import cloud.tangyuan.hellocommon.CallbackListener;

public class MyCallbackListener implements CallbackListener {
    @Override
    public void report(String msg) {
        System.out.println(msg);
    }
}
