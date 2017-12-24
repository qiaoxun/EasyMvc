package com.test.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class MethodParameterUtil {
	public static String[] getMethodParameters(final Method method, Class<?> clazz) {
		String className = ClassUtils.getClassFileName(clazz);

		System.out.println(className);
		
		final Class<?>[] parameterTypes = method.getParameterTypes();
		if (parameterTypes == null || parameterTypes.length == 0) {
			return null;
		}

		final Type[] types = new Type[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			types[i] = Type.getType(parameterTypes[i]);
		}
		final String[] parameterNames = new String[parameterTypes.length];

		InputStream is = clazz.getResourceAsStream(className);
		try {
			ClassReader classReader = new ClassReader(is);
			classReader.accept(new ClassVisitor(Opcodes.ASM5) {
				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					Type[] argumentTypes = Type.getArgumentTypes(desc);

					if (!method.getName().equals(name) || !Arrays.equals(argumentTypes, types)) {
						return null;
					}

					return new MethodVisitor(Opcodes.ASM5) {
						@Override
						public void visitLocalVariable(String name, String desc, String signature, Label start,
								Label end, int index) {
							// 静态方法第一个参数就是方法的参数，如果是实例方法，第一个参数是this
							if (Modifier.isStatic(method.getModifiers())) {
								if (index <= parameterNames.length - 1) {
									parameterNames[index] = name;
								}
							} else if (index > 0) {
								if (index <= parameterNames.length - 2) {
									parameterNames[index - 1] = name;
								}
							}
						}
					};
				}
			}, 0);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return parameterNames;
	}
}
