package myrmi.network;

public class InvokeMessage {
    private int objectKey;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;

    public InvokeMessage(int objectKey, String methodName, Class<?>[] parameterTypes, Object[] args) {
        this.objectKey = objectKey;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public int getObjectKey() {
        return objectKey;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

}
