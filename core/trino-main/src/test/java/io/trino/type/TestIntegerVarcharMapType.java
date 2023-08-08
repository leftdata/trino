/*
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
package io.trino.type;

import com.google.common.collect.ImmutableMap;
import io.trino.spi.block.Block;
import io.trino.spi.block.BlockBuilder;
import io.trino.spi.type.Type;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.trino.spi.type.IntegerType.INTEGER;
import static io.trino.spi.type.VarcharType.VARCHAR;
import static io.trino.util.StructuralTestUtil.mapBlockOf;
import static io.trino.util.StructuralTestUtil.mapType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestIntegerVarcharMapType
        extends AbstractTestType
{
    public TestIntegerVarcharMapType()
    {
        super(mapType(INTEGER, VARCHAR), Map.class, createTestBlock(mapType(INTEGER, VARCHAR)));
    }

    public static Block createTestBlock(Type mapType)
    {
        BlockBuilder blockBuilder = mapType.createBlockBuilder(null, 2);
        mapType.writeObject(blockBuilder, mapBlockOf(INTEGER, VARCHAR, ImmutableMap.of(1, "hi")));
        mapType.writeObject(blockBuilder, mapBlockOf(INTEGER, VARCHAR, ImmutableMap.of(1, "2", 2, "hello")));
        return blockBuilder.build();
    }

    @Override
    protected Object getGreaterValue(Object value)
    {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testRange()
    {
        assertThat(type.getRange())
                .isEmpty();
    }

    @Test
    public void testPreviousValue()
    {
        Object sampleValue = getSampleValue();
        if (!type.isOrderable()) {
            assertThatThrownBy(() -> type.getPreviousValue(sampleValue))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Type is not orderable: " + type);
            return;
        }
        assertThat(type.getPreviousValue(sampleValue))
                .isEmpty();
    }

    @Test
    public void testNextValue()
    {
        Object sampleValue = getSampleValue();
        if (!type.isOrderable()) {
            assertThatThrownBy(() -> type.getNextValue(sampleValue))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Type is not orderable: " + type);
            return;
        }
        assertThat(type.getNextValue(sampleValue))
                .isEmpty();
    }
}
