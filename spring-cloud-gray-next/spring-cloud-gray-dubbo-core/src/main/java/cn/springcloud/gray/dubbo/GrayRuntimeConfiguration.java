package cn.springcloud.gray.dubbo;

/** Small bridge because Dubbo SPI instances are not created by Spring. */
public final class GrayRuntimeConfiguration {
    private static volatile boolean forceTag;

    private GrayRuntimeConfiguration() {
    }

    public static boolean isForceTag() {
        return forceTag;
    }

    public static void setForceTag(boolean forceTag) {
        GrayRuntimeConfiguration.forceTag = forceTag;
    }
}
