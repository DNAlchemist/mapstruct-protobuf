package one.chest.mapstruct.protobuf;

import org.mapstruct.ap.internal.util.Nouns;
import org.mapstruct.ap.spi.DefaultAccessorNamingStrategy;
import org.mapstruct.ap.spi.MapStructProcessingEnvironment;
import org.mapstruct.ap.spi.util.IntrospectorUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static one.chest.mapstruct.protobuf.ProtobufGeneratedMethods.*;

public class ProtobufAccessorNamingStrategy extends DefaultAccessorNamingStrategy {

    public static final String PROTOBUF_MESSAGE_LITE_OR_BUILDER = "com.google.protobuf.MessageLiteOrBuilder";
    private TypeMirror protobufMessageLiteOrBuilderType;

    @Override
    public void init(MapStructProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        TypeElement typeElement = elementUtils.getTypeElement(PROTOBUF_MESSAGE_LITE_OR_BUILDER);
        if (typeElement != null) {
            protobufMessageLiteOrBuilderType = typeElement.asType();
        }
    }

    @Override
    public boolean isGetterMethod(ExecutableElement method) {
        if (isBuilder(method) || isInternalMethod(method) || isInternalMethodCommon(method)) {
            return false;
        }
        return super.isGetterMethod(method);
    }

    @Override
    public boolean isSetterMethod(ExecutableElement method) {
        if (isInternalMethod(method) || isInternalMethodCommon(method)) {
            return false;
        }
        return super.isSetterMethod(method);
    }

    @Override
    protected boolean isFluentSetter(ExecutableElement method) {
        if (isInternalMethodCommon(method)) {
            return false;
        } else {
            return super.isFluentSetter(method);
        }
    }

    @Override
    public boolean isAdderMethod(ExecutableElement method) {
        if (isInternalMethodCommon(method)) {
            return false;
        } else {
            return super.isAdderMethod(method);
        }
    }

    @Override
    public boolean isPresenceCheckMethod(ExecutableElement method) {
        if (isInternalMethodCommon(method)) {
            return false;
        } else {
            return super.isPresenceCheckMethod(method);
        }
    }

    @Override
    public String getElementName(ExecutableElement adderMethod) {
        String methodName = super.getElementName(adderMethod);
        Element receiver = adderMethod.getEnclosingElement();
        if (receiver != null && protobufMessageLiteOrBuilderType != null && typeUtils.isAssignable(receiver.asType(), protobufMessageLiteOrBuilderType)) {
            methodName = Nouns.singularize(methodName);
        }

        return methodName;
    }

    @Override
    public String getPropertyName(ExecutableElement method) {
        String methodName = method.getSimpleName().toString();
        if (isGetList(method) || isSetList(method)) {
            Element receiver = method.getEnclosingElement();
            if (receiver != null && (receiver.getKind() == ElementKind.CLASS || receiver.getKind() == ElementKind.INTERFACE)) {
                if (isProtobufGeneratedMessage((TypeElement) receiver)) {
                    return IntrospectorUtils.decapitalize(methodName.substring(3, methodName.length() - 4));
                }
            }
        }
        return super.getPropertyName(method);

    }

    private boolean isProtobufGeneratedMessage(TypeElement type) {
        List<? extends TypeMirror> interfaces = type.getInterfaces();

        if (interfaces != null) {
            for (TypeMirror implementedInterface : interfaces) {
                if (implementedInterface.toString().startsWith(PROTOBUF_MESSAGE_LITE_OR_BUILDER)) {
                    return true;
                } else if (implementedInterface instanceof DeclaredType) {
                    DeclaredType declared = (DeclaredType) implementedInterface;
                    Element supertypeElement = declared.asElement();
                    if (isProtobufGeneratedMessage((TypeElement) supertypeElement)) {
                        return true;
                    }
                }
            }
        }

        TypeMirror superType = type.getSuperclass();
        if (superType instanceof DeclaredType) {
            DeclaredType declared = (DeclaredType) superType;
            Element supertypeElement = declared.asElement();
            return isProtobufGeneratedMessage((TypeElement) supertypeElement);
        }
        return false;
    }

}
