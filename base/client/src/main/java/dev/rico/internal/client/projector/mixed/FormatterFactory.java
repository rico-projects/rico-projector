/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2019 The original authors.
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
package dev.rico.internal.client.projector.mixed;

import dev.rico.internal.core.RicoConstants;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatterFactory {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern()).withLocale(Locale.ENGLISH).withZone(ZoneId.of(RicoConstants.TIMEZONE_UTC));

    public static String datePattern() {
        return "d. MMM yyyy";
    }

    public static DateTimeFormatter dateFormatter() {
        return dateFormatter;
    }

    public static DateTimeFormatter customFormat(final String formatString) {
        return DateTimeFormatter.ofPattern(formatString).withLocale(Locale.ENGLISH).withZone(ZoneId.of(RicoConstants.TIMEZONE_UTC));
    }
}
