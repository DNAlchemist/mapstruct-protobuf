/*
 * Mapstruct SPI
 * Copyright (C) 2020 Ruslan Mikhalev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package one.chest.mapstruct.protobuf;

import org.mapstruct.ap.spi.util.IntrospectorUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.List;

class ProtobufGeneratedMethods {

    public static final String PROTOBUF_MESSAGE_LITE_OR_BUILDER = "com.google.protobuf.MessageLiteOrBuilder";

    private static final List<String> PROTOBUF_GENERATED_METHOD_SUFFIX = Arrays.asList(
            "Count",
            "Bytes",
            "Value",
            "ValueList",
            "Map"
    );
    private static final List<String> PROTOBUF_GENERATED_METHOD_PREFIX = Arrays.asList(
            "remove",
            "clear",
            "mutable",
            "getMutable",
            "merge"
    );

    private static final List<String> ADDER_PREFIXES = Arrays.asList(
            "putAll",
            "put",
            "addAll",
            "add"
    );

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
            "getField",
            "getFieldRaw",
            "getInitializationErrorString",
            "getMemoizedSerializedSize",
            "getOneofFieldDescriptor",
            "getParserForType",
            "getRepeatedField",
            "getRepeatedFieldCount",
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

    public static boolean isAdderMethod(ExecutableElement method) {
        String methodName = String.valueOf(method.getSimpleName());
        for (String adderPrefix : ADDER_PREFIXES) {
            if (methodName.endsWith(adderPrefix)) {
                String propertyMethod = IntrospectorUtils.decapitalize(methodName.substring(adderPrefix.length()));
                if (isPropertyMethodExists(method, propertyMethod)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getPropertyName(ExecutableElement method) {
        String methodName = method.getSimpleName().toString();
        if (isGetList(method) || isSetList(method)) {
            Element receiver = method.getEnclosingElement();
            if (receiver != null && (receiver.getKind() == ElementKind.CLASS || receiver.getKind() == ElementKind.INTERFACE)) {
                if (isProtobufGeneratedMessage((TypeElement) receiver)) {
                    //FIXME refactor this magic expression
                    return IntrospectorUtils.decapitalize(methodName.substring(3, methodName.length() - 4));
                }
            }
        }
        for (String adderPrefix : ADDER_PREFIXES) {
            if (methodName.startsWith(adderPrefix)) {
                Element receiver = method.getEnclosingElement();
                if (receiver != null && (receiver.getKind() == ElementKind.CLASS || receiver.getKind() == ElementKind.INTERFACE)) {
                    if (isProtobufGeneratedMessage((TypeElement) receiver)) {
                        return IntrospectorUtils.decapitalize(methodName.substring(adderPrefix.length()));
                    }
                }
            }
        }

        return null;
    }

    public static boolean isInternalMethod(ExecutableElement method) {
        String methodName = String.valueOf(method.getSimpleName());
        for (String generatedMethod : PROTOBUF_GENERATED_METHOD_SUFFIX) {
            if (methodName.endsWith(generatedMethod)) {
                String propertyMethod = methodName.substring(0, methodName.length() - generatedMethod.length());
                if (isPropertyMethodExists(method, propertyMethod)) {
                    return true;
                }
            }
        }

        for (String generatedMethod : PROTOBUF_GENERATED_METHOD_PREFIX) {
            if (methodName.startsWith(generatedMethod)) {
                String propertyMethod = "get" + methodName.substring(generatedMethod.length());
                if (isPropertyMethodExists(method, propertyMethod)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isPropertyMethodExists(ExecutableElement method, String propertyMethodName) {
        for (Element el : method.getEnclosingElement().getEnclosedElements()) {
            if (el.getSimpleName().toString().equals(propertyMethodName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBuilder(ExecutableElement method) {
        String methodName = String.valueOf(method.getSimpleName());
        for (String generatedMethod : PROTOBUF_GENERATED_BUILDER_SUFFIX) {
            if (methodName.endsWith(generatedMethod)) {
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

    public static boolean isProtobufGeneratedMessage(TypeElement type) {
        List<? extends TypeMirror> interfaces = type.getInterfaces();

        if (interfaces != null) {
            for (TypeMirror i : interfaces) {
                if (i.toString().startsWith(PROTOBUF_MESSAGE_LITE_OR_BUILDER)) {
                    return true;
                } else if (i instanceof DeclaredType) {
                    Element element = ((DeclaredType) i).asElement();
                    if (isProtobufGeneratedMessage((TypeElement) element)) {
                        return true;
                    }
                }
            }
        }

        TypeMirror typeMirror = type.getSuperclass();
        if (typeMirror instanceof DeclaredType) {
            Element element = ((DeclaredType) typeMirror).asElement();
            return isProtobufGeneratedMessage((TypeElement) element);
        }
        return false;
    }

}
