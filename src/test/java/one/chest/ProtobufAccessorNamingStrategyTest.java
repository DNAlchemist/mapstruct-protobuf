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
package one.chest;

import one.chest.mapstruct.protobuf.ProtobufAccessorNamingStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProtobufAccessorNamingStrategyTest {
    @Test
    void testSomeLibraryMethod() {
        ProtobufAccessorNamingStrategy classUnderTest = new ProtobufAccessorNamingStrategy();
        assertTrue(true, "Coming soon");
    }
}
