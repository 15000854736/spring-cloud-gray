package cn.springcloud.gray.dubbo;

import java.util.Optional;

/** Request-scoped gray tag shared by HTTP ingress and Dubbo filters. */
public final class GrayContext {
    private static final ScopedValue<String> TAG = ScopedValue.newInstance();
    private static final ThreadLocal<String> COMPATIBILITY_TAG = new ThreadLocal<>();

    private GrayContext() {
    }

    public static Optional<String> currentTag() {
        if (TAG.isBound()) {
            return Optional.of(TAG.get());
        }
        return Optional.ofNullable(COMPATIBILITY_TAG.get());
    }

    public static void runWithTag(String tag, Runnable action) {
        String normalized = normalize(tag);
        if (normalized == null) {
            action.run();
            return;
        }
        ScopedValue.where(TAG, normalized).run(action);
    }

    public static <T> T callWithTag(String tag, java.util.concurrent.Callable<T> action) throws Exception {
        String normalized = normalize(tag);
        if (normalized == null) {
            return action.call();
        }
        return ScopedValue.where(TAG, normalized).call(action);
    }

    static Scope openCompatibilityScope(String tag) {
        String previous = COMPATIBILITY_TAG.get();
        String normalized = normalize(tag);
        if (normalized == null) {
            COMPATIBILITY_TAG.remove();
        } else {
            COMPATIBILITY_TAG.set(normalized);
        }
        return () -> {
            if (previous == null) {
                COMPATIBILITY_TAG.remove();
            } else {
                COMPATIBILITY_TAG.set(previous);
            }
        };
    }

    private static String normalize(String tag) {
        if (tag == null || tag.isBlank()) {
            return null;
        }
        return tag.trim();
    }

    interface Scope extends AutoCloseable {
        @Override
        void close();
    }
}
