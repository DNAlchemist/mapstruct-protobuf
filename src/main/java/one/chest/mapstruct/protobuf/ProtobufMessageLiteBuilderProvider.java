package one.chest.mapstruct.protobuf;

import org.mapstruct.ap.spi.DefaultBuilderProvider;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

public class ProtobufMessageLiteBuilderProvider extends DefaultBuilderProvider {

    @Override
    protected boolean isBuildMethod(ExecutableElement buildMethod, TypeElement typeElement) {
        if ("build".equals(buildMethod.getSimpleName().toString())
                && buildMethod.getReturnType() instanceof TypeVariable
        ) {
            if (isMessageLiteReturnType(buildMethod)) {
                return true;
            }
        }
        return super.isBuildMethod(buildMethod, typeElement);
    }

    private boolean isMessageLiteReturnType(ExecutableElement buildMethod) {
        TypeVariable tv = (TypeVariable) buildMethod.getReturnType();
        TypeMirror upperBound = tv.getUpperBound() == null ? tv : tv.getUpperBound();
        return upperBound.toString().startsWith("com.google.protobuf.GeneratedMessageLite");
    }
}
