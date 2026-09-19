package cn.springcloud.gray.dubbo;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

/** Adds the gray tag used by Dubbo's built-in tag router to every outbound RPC. */
@Activate(group = CommonConstants.CONSUMER, order = -20_000)
public final class GrayConsumerFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        GrayContext.currentTag().ifPresent(tag -> {
            invocation.setAttachment(GrayConstants.DUBBO_TAG_KEY, tag);
            invocation.setAttachment(GrayConstants.FORCE_TAG_KEY,
                    Boolean.toString(GrayRuntimeConfiguration.isForceTag()));
        });
        return invoker.invoke(invocation);
    }
}
