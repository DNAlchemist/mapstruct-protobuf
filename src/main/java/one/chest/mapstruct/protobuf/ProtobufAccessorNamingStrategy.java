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

import org.mapstruct.ap.internal.util.Nouns;
import org.mapstruct.ap.spi.DefaultAccessorNamingStrategy;
import org.mapstruct.ap.spi.MapStructProcessingEnvironment;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static one.chest.mapstruct.protobuf.ProtobufGeneratedMethods.*;

public class ProtobufAccessorNamingStrategy extends DefaultAccessorNamingStrategy {

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
        }
        return super.isFluentSetter(method);
    }

    @Override
    public boolean isAdderMethod(ExecutableElement method) {
        if (isInternalMethodCommon(method)) {
            return false;
        }
        return ProtobufGeneratedMethods.isAdderMethod(method) && super.isAdderMethod(method);
    }

    @Override
    public boolean isPresenceCheckMethod(ExecutableElement method) {
        if (isInternalMethodCommon(method)) {
            return false;
        }
        return super.isPresenceCheckMethod(method);
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
        String propertyName = ProtobufGeneratedMethods.getPropertyName(method);
        return propertyName == null ? super.getPropertyName(method) : propertyName;
    }

}
