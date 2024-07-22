package ru.danil42russia.agent;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class GoSdkUtilClassTransformer implements ClassFileTransformer {
    private static final String GO_SDK_UTIL_CLASS = "com/goide/sdk/GoSdkUtil";
    private static final String IS_UNREACHABLE_INTERNAL_PACKAGE_METHOD = "isUnreachableInternalPackage";
    private static final String NEW_UNREACHABLE_INTERNAL_PACKAGE_METHOD_BODY = "{ return false; }";

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classFileBuffer) {
        if (!GO_SDK_UTIL_CLASS.equals(className)) {
            return classFileBuffer;
        }
        Logger.debug("Class '" + GO_SDK_UTIL_CLASS + "' found");


        CtClass goSdkUtilClass;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(classFileBuffer)) {
            ClassPool classPool = ClassPool.getDefault();
            goSdkUtilClass = classPool.makeClass(byteArrayInputStream);
        } catch (IOException | RuntimeException e) {
            Logger.error(e);
            return classFileBuffer;
        }
        Logger.debug("Class '" + GO_SDK_UTIL_CLASS + "' was successfully created from the bytecode");


        CtMethod isUnreachableInternalPackageMethod;
        try {
            isUnreachableInternalPackageMethod = goSdkUtilClass.getDeclaredMethod(IS_UNREACHABLE_INTERNAL_PACKAGE_METHOD);
        } catch (NotFoundException e) {
            Logger.error(e);
            return classFileBuffer;
        }
        Logger.debug("Method '" + IS_UNREACHABLE_INTERNAL_PACKAGE_METHOD + "' found in bytecode");


        try {
            isUnreachableInternalPackageMethod.setBody(NEW_UNREACHABLE_INTERNAL_PACKAGE_METHOD_BODY);
        } catch (CannotCompileException e) {
            Logger.error(e);
            return classFileBuffer;
        }
        Logger.debug("Body for method '" + IS_UNREACHABLE_INTERNAL_PACKAGE_METHOD + "' has been successfully changed");


        byte[] newByteCode;
        try {
            newByteCode = goSdkUtilClass.toBytecode();
        } catch (IOException | CannotCompileException e) {
            Logger.error(e);
            return classFileBuffer;
        }
        Logger.debug("Bytecode for class '" + GO_SDK_UTIL_CLASS + "' has been successfully generated");

        return newByteCode;
    }
}
