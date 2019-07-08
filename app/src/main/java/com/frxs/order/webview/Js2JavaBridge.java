package com.frxs.order.webview;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Js2JavaBridge {

	private static Js2JavaBridge js2JavaBridge = null;

	private HashMap<String, Object> jsobjectHashMap = new HashMap<String, Object>();

	private Js2JavaBridge() {

	}

	public static synchronized Js2JavaBridge getInstance() {
		if (js2JavaBridge == null) {
			js2JavaBridge = new Js2JavaBridge();
		}
		return js2JavaBridge;
	}

	public void addjsObject(String key, Object object) {
		jsobjectHashMap.put(key, object);
	}

	public void run(String objectName, String menthod, JsonElement param) {

		Object invokeobject = jsobjectHashMap.get(objectName);
		invokeMethod(invokeobject, menthod, param);

	}

	private void invokeMethod(Object invokeobject, String aMethodName, JsonElement param) {

		// Log.i(TAG, invokeobject.toString() );
		// Log.i(TAG, methodName );
		// Log.i(TAG, catalogAndContentRes.toString() );
		if (aMethodName != null && invokeobject != null) {
			try {
				if (param == null) {
					Method method = invokeobject.getClass().getDeclaredMethod(aMethodName);
					method.invoke(invokeobject);
				} else {
					Method method = invokeobject.getClass().getDeclaredMethod(aMethodName, JsonObject.class);
					method.invoke(invokeobject, param.getAsJsonObject());

				}

			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
