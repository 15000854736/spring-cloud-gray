package cn.springcloud.gray.dubbo.autoconfigure;

import cn.springcloud.gray.dubbo.GrayConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.cloud.gray.dubbo")
public class GrayDubboProperties {
    private boolean enabled = true;
    private String headerName = GrayConstants.DEFAULT_HEADER;
    private boolean forceTag;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public boolean isForceTag() {
        return forceTag;
    }

    public void setForceTag(boolean forceTag) {
        this.forceTag = forceTag;
    }
}
