package one.chest.mapstruct.protobuf;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.List;

class ProtobufGeneratedMethods {

    private static final List<String> PROTOBUF_GENERATED_METHOD_SUFFIX = Arrays.asList("Value", "Count", "Bytes", "Map", "ValueList");
    private static final List<String> PROTOBUF_GENERATED_METHOD_PREFIX = Arrays.asList("remove", "clear", "mutable", "merge", "putAll", "getMutable");
    private static final List<String> PROTOBUF_GENERATED_BUILDER_SUFFIX = Arrays.asList("OrBuilder", "BuilderList");

    public static final String PROTOBUF_STRING_LIST_TYPE = "com.google.protobuf.ProtocolStringList";

    public static final List<String> COMMON = Arrays.asList(
            "clear",
            "clearField",
            "clearOneof",
            "getAllFields",
            "getAllFieldsMutable",
            "getAllFieldsRaw",
            "getDefaultInstance",
            "getDefaultInstanceForType",
            "getDescriptor",
            "getDescriptorForType",
            "getDescriptorForType",
            "getField",
            "getFieldRaw",
            "getInitializationErrorString",
            "getMemoizedSerializedSize",
            "getMemoizedSerializedSize",
            "getOneofFieldDescriptor",
            "getOneofFieldDescriptor",
            "getParserForType",
            "getRepeatedField",
            "getRepeatedFieldCount",
            "getSerializedSize",
            "getSerializedSize",
            "getSerializingExceptionMessage",
            "getUnknownFields",
            "isInitialized",
            "mergeFrom",
            "mergeUnknownFields",
            "newBuilder",
            "newBuilderForType",
            "parseDelimitedFrom",
            "parseFrom",
            "setRepeatedField",
            "setUnknownFields"
    );

    public static boolean isInternalMethodCommon(ExecutableElement method) {
        return COMMON.contains(String.valueOf(method.getSimpleName()));
    }

    public static boolean isInternalMethod(ExecutableElement method) {
        String methodName = String.valueOf(method.getSimpleName());
        for (String generatedMethod : PROTOBUF_GENERATED_METHOD_SUFFIX) {
            if (methodName.endsWith(generatedMethod)) {
                String propertyMethod = methodName.substring(0, methodName.length() - generatedMethod.length());
                boolean propertyMethodExists = method.getEnclosingElement().getEnclosedElements().stream().anyMatch(e -> e.getSimpleName().toString().equals(propertyMethod));
                if (propertyMethodExists) {
                    return true;
                }
            }
        }

        for (String generatedMethod : PROTOBUF_GENERATED_METHOD_PREFIX) {
            if (methodName.startsWith(generatedMethod)) {
                String propertyMethod = "get" + methodName.substring(generatedMethod.length());
                boolean propertyMethodExists = method.getEnclosingElement().getEnclosedElements().stream().anyMatch(e -> e.getSimpleName().toString().equals(propertyMethod));
                if (propertyMethodExists) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isBuilder(ExecutableElement method) {
        String methodName = String.valueOf(method.getSimpleName());
        for (String generatedMethod : PROTOBUF_GENERATED_BUILDER_SUFFIX) {
            if (methodName.startsWith(generatedMethod)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGetList(ExecutableElement element) {
        return element.getSimpleName().toString().startsWith("get") && isListType(element.getReturnType());
    }

    public static boolean isSetList(ExecutableElement element) {
        if (element.getSimpleName().toString().startsWith("set") && element.getParameters().size() == 1) {
            TypeMirror param = element.getParameters().get(0).asType();
            return isListType(param);
        }
        return false;
    }

    public static boolean isListType(TypeMirror t) {
        return t.toString().startsWith(List.class.getCanonicalName()) || t.toString().startsWith(PROTOBUF_STRING_LIST_TYPE);
    }

}
