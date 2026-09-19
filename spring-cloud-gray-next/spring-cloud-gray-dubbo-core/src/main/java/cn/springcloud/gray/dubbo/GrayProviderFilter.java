package cn.springcloud.gray.dubbo;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

/** Restores an inbound Dubbo tag so nested Dubbo calls stay on the same gray lane. */
@Activate(group = CommonConstants.PROVIDER, order = -20_000)
public final class GrayProviderFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String tag = invocation.getAttachment(GrayConstants.DUBBO_TAG_KEY);
        try (GrayContext.Scope ignored = GrayContext.openCompatibilityScope(tag)) {
            return invoker.invoke(invocation);
        }
    }
}
