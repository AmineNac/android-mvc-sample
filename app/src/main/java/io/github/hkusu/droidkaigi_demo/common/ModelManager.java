package io.github.hkusu.droidkaigi_demo.common;

import java.util.HashMap;
import java.util.Map;

// 各モデルの参照を保持するクラス

public class ModelManager {

    public static enum Tag {
        QIITA_ITEM,
    }

    private static Map<Tag, Object> showcase = new HashMap<>();

    private ModelManager() {
    }

    public static void register(Tag tag, Object object) {
        showcase.put(tag, object);
    }

    public static Object get(Tag tag) {
        return showcase.get(tag);
    }
}
