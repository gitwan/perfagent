package com.gitwan.perf;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class PerfTransformer implements ClassFileTransformer {
    private String clzName;
    private String mtdName;
    private String[] mtdParamsType;

    public PerfTransformer(String clzName, String mtdName, String[] mtdParamsType) {
        this.clzName = clzName;
        this.mtdName = mtdName;
        this.mtdParamsType = mtdParamsType;
    }

    public PerfTransformer(String clzName, String mtdName) {
        this(clzName, mtdName, null);
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replace("/", ".");
        if (className.equalsIgnoreCase(clzName)) {
            try {
                ClassPool classPool = ClassPool.getDefault();
                CtClass ctClass = classPool.get(clzName);
                CtMethod ctMethod;
                if (mtdParamsType != null) {
                    ctMethod = ctClass.getDeclaredMethod("main");
                    //todo
                } else {
                    ctMethod = ctClass.getDeclaredMethod(mtdName);
                }
                ctMethod.insertBefore("System.out.println(System.currentTimeMillis());");
                ctMethod.insertAfter("System.out.println(System.currentTimeMillis());");

                return ctClass.toBytecode();
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
