/*
 * Copyright 2015-2016 Canoo Engineering AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.rico.internal.projector.converters;

import dev.rico.internal.projector.ForRemoval;
import dev.rico.internal.projector.mixed.DocumentData;
import dev.rico.internal.remoting.converters.AbstractConverterFactory;
import dev.rico.internal.remoting.converters.AbstractStringConverter;
import dev.rico.remoting.converter.Converter;
import dev.rico.remoting.converter.ValueConverterException;

import java.util.Collections;
import java.util.List;

@ForRemoval
public class DocumentDataConverterFactory extends AbstractConverterFactory {

    @SuppressWarnings("rawtypes")
    private final static Converter CONVERTER = new DocumentDataConverter();

    @Override
    public boolean supportsType(Class<?> cls) {
        return DocumentData.class.isAssignableFrom(cls);
    }

    @Override
    public List<Class> getSupportedTypes() {
        return Collections.singletonList(DocumentData.class);
    }

    @Override
    public int getTypeIdentifier() {
        return 1100;
    }

    @Override
    public Converter getConverterForType(Class<?> cls) {
        return CONVERTER;
    }

    private static class DocumentDataConverter extends AbstractStringConverter<DocumentData> {

        @Override
        public DocumentData convertFromRemoting(String value) throws ValueConverterException {
            if (value == null) {
                return null;
            }
            try {
                return DocumentData.from(value);
            } catch (Exception e) {
                throw new ValueConverterException("Can not convert to DocumentData", e);
            }
        }

        @Override
        public String convertToRemoting(DocumentData value) throws ValueConverterException {
            if (value == null) {
                return null;
            }
            try {
                return value.asString();
            } catch (Exception e) {
                throw new ValueConverterException("Can not convert from DocumentData", e);
            }
        }
    }

}
