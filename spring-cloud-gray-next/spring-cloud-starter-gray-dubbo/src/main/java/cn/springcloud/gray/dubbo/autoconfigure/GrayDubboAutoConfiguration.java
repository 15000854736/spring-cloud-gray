package cn.springcloud.gray.dubbo.autoconfigure;

import cn.springcloud.gray.dubbo.GrayRuntimeConfiguration;
import jakarta.servlet.Filter;
import org.apache.dubbo.rpc.Invoker;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(Invoker.class)
@EnableConfigurationProperties(GrayDubboProperties.class)
@ConditionalOnProperty(prefix = "spring.cloud.gray.dubbo", name = "enabled", havingValue = "true", matchIfMissing = true)
public class GrayDubboAutoConfiguration {
    public GrayDubboAutoConfiguration(GrayDubboProperties properties) {
        GrayRuntimeConfiguration.setForceTag(properties.isForceTag());
    }

    @Bean
    @ConditionalOnClass(Filter.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnMissingBean(name = "grayIngressFilter")
    Filter grayIngressFilter(GrayDubboProperties properties) {
        return new GrayIngressFilter(properties.getHeaderName());
    }
}
