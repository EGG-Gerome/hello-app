package cloud.tangyuan.helloconsumer;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public class MyBlockHandler {
    public static String handleBlock(String username, BlockException blockException){
        blockException.printStackTrace();
        return "%s, request is blocked.";
    }
}
