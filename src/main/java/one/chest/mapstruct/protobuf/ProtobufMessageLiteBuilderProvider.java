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
                && isMessageLiteReturnType(buildMethod)
        ) {
                return true;
        }
        return super.isBuildMethod(buildMethod, typeElement);
    }

    private boolean isMessageLiteReturnType(ExecutableElement buildMethod) {
        TypeVariable tv = (TypeVariable) buildMethod.getReturnType();
        TypeMirror upperBound = tv.getUpperBound() == null ? tv : tv.getUpperBound();
        return upperBound.toString().startsWith("com.google.protobuf.GeneratedMessageLite");
    }
}
