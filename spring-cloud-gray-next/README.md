# Spring Cloud Gray Next: JDK 25 + Dubbo 3

This is the JDK 25 / Spring Boot 4 migration track. It is isolated from the legacy
Spring Boot 2.3 build so applications can migrate service by service.

## Supported call path

`HTTP X-Gray-Tag` -> request scope -> Dubbo attachment -> Dubbo Tag Router -> tagged provider

The provider filter restores the tag for nested Dubbo calls. Dubbo publishes provider
parameters through Nacos, so the built-in tag router can select the matching instance.

## Build

```bash
cd spring-cloud-gray-next
mvn verify
```

JDK 25 and Maven 3.9+ are mandatory. CI also compiles and tests on JDK 25.

## Consumer

```xml
<dependency>
  <groupId>cn.springcloud.gray</groupId>
  <artifactId>spring-cloud-starter-gray-dubbo</artifactId>
  <version>4.0.0-SNAPSHOT</version>
</dependency>
```

```yaml
spring:
  cloud:
    gray:
      dubbo:
        enabled: true
        header-name: X-Gray-Tag
        # false: fall back to an untagged provider if the lane has no healthy instance
        force-tag: false

dubbo:
  registry:
    address: nacos://127.0.0.1:8848
```

Send `X-Gray-Tag: beta` on the ingress HTTP request. Non-HTTP entry points can use
`GrayContext.runWithTag("beta", action)`.

## Provider

Every gray provider must publish the same Dubbo tag:

```yaml
dubbo:
  registry:
    address: nacos://127.0.0.1:8848
  provider:
    tag: beta
```

Production instances omit `dubbo.provider.tag`. The starter must also be installed on a
provider if that provider makes nested Dubbo calls.

## Compatibility notes

- Baseline: JDK 25, Spring Boot 4.0.8, Apache Dubbo 3.3.6.
- The implementation uses Dubbo's public Filter SPI and built-in Tag Router instead of
  the removed Netflix Ribbon integration.
- The legacy server/UI are not yet migrated; this increment supplies the runtime gray
  routing path. Dynamic rule management is the next migration phase.
