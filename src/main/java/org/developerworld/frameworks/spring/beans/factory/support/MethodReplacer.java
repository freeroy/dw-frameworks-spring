package org.developerworld.frameworks.spring.beans.factory.support;

import java.lang.reflect.Method;

/**
 * 方法重写器
 * 
 * @author Roy Huang
 * @version 20111006
 * 
 */
public class MethodReplacer implements
		org.springframework.beans.factory.support.MethodReplacer {

	private Class<?> replacerStaticClass;
	private Object replacerObject;
	private String replacerMethod;
	private Object[] replacerArgs;
	private Class<?>[] replacerArgTypes;

	public void setReplacerObject(Object replacerObject) {
		this.replacerObject = replacerObject;
	}

	public void setReplacerMethod(String replacerMethod) {
		this.replacerMethod = replacerMethod;
	}

	public void setReplacerStaticClass(Class<?> replacerStaticClass) {
		this.replacerStaticClass = replacerStaticClass;
	}

	public void setReplacerArgs(Object[] replacerArgs) {
		this.replacerArgs = replacerArgs;
		this.replacerArgTypes = getArgsTypes(replacerArgs);
	}

	/**
	 * 获取参数的类型
	 * 
	 * @param args
	 * @return
	 */
	private Class<?>[] getArgsTypes(Object[] args) {
		Class<?>[] rst = null;
		if (args != null) {
			rst = new Class[args.length];
			for (int i = 0; i < args.length; i++)
				rst[i] = args[i].getClass();
		}
		return rst;
	}

	/**
	 * 接口要求实现的方法
	 */
	public Object reimplement(Object object, Method method, Object[] args)
			throws Throwable {
		Object _object = getInvokeObject(object);
		Method _method = getInvokeMethod(_object, method, args);
		Object[] _args = getInvokeArgs(args);
		// 执行方法，并返回值
		return _method.invoke(_object, _args);
	}

	/**
	 * 获取运行参数
	 * 
	 * @param defaultArgs
	 * @return
	 */
	private Object[] getInvokeArgs(Object[] defaultArgs) {
		Object[] rst = defaultArgs;
		if (replacerArgs != null)
			rst = replacerArgs;
		return rst;
	}

	/**
	 * 获取运行方法
	 * 
	 * @param object
	 * @param defaultMethod
	 * @param defaultArgs
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	private Method getInvokeMethod(Object object, Method defaultMethod,
			Object[] defaultArgs) throws SecurityException,
			NoSuchMethodException {
		Method rst = defaultMethod;
		//获取方法名
		String methodName=replacerMethod;
		if(methodName==null)
			methodName=rst.getName();
		// 获取类
		Class<?> clazz = replacerStaticClass;
		if (object != null)
			clazz = object.getClass();
		// 获取方法参数类型
		Class<?>[] argTypes = replacerArgTypes;
		if (argTypes == null)
			argTypes = getArgsTypes(defaultArgs);
		rst = clazz.getMethod(methodName, argTypes);
		return rst;
	}

	/**
	 * 获取运行对象
	 * 
	 * @param defaultObject
	 * @return
	 */
	private Object getInvokeObject(Object defaultObject) {
		Object rst = defaultObject;
		// 静态对象
		if (replacerStaticClass != null)
			rst = null;
		else if (replacerObject != null)
			rst = replacerObject;
		return rst;
	}

}
