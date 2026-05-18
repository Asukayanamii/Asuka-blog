package com.asuka.backend.context;

public class AdminContext {
    private static final ThreadLocal<Integer> THREAD_LOCAL = new ThreadLocal<>();

    public static void setCurrentId(Integer id) {
        THREAD_LOCAL.set(id);
    }

    public static Integer getCurrentId() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
