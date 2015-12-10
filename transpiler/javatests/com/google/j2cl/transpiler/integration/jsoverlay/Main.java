package com.google.j2cl.transpiler.integration.jsoverlay;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsType;

public class Main {
  @JsType(isNative = true, namespace = "test.foo")
  static class NativeJsTypeWithOverlay {
    @JsOverlay public static final int COMPILE_TIME_CONSTANT = 1;
    @JsOverlay public static Object staticField = new Object();
    public native int m();

    @JsOverlay
    public final int callM() {
      return m();
    }

    @JsOverlay
    public static final int fun(int a, int b) {
      return a > b ? a + b : a * b;
    }
  }

  public static void testNativeJsWithOverlay() {
    NativeJsTypeWithOverlay object = new NativeJsTypeWithOverlay();
    assert 6 == object.callM();
    assert 20 == NativeJsTypeWithOverlay.fun(4, 5);
    assert 1 == NativeJsTypeWithOverlay.COMPILE_TIME_CONSTANT;
    assert NativeJsTypeWithOverlay.staticField != null;
    NativeJsTypeWithOverlay.staticField = null;
    assert NativeJsTypeWithOverlay.staticField == null;
  }

  public static void main(String... args) {
    testNativeJsWithOverlay();
  }
}
