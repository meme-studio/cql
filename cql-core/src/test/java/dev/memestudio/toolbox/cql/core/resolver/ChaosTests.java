package dev.memestudio.toolbox.cql.core.resolver;

import dev.memestudio.toolbox.cql.core.util.ObjectTransformer;
import io.vavr.collection.Stream;
import lombok.SneakyThrows;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.junit.Test;

import java.util.*;

@SuppressWarnings("SqlResolve")
public class ChaosTests {


    @SneakyThrows
    @Test
    public void testCCJSqlParserUtil() {
        Statement parsed = CCJSqlParserUtil.parse("SELECT * FROM a LEFT JOIN b ON ii = cc");
        System.out.println(parsed);
    }

    @Test
    public void testLeftJoin() {
        Stream<Map<String, Object>> left = Stream.ofAll(new ArrayList<Map<String, Object>>() {{
            add(new HashMap<String, Object>() {{
                put("a.bb", 1);
                put("a.cc", 10L);
            }});
            add(new HashMap<String, Object>() {{
                put("a.bb", 2);
                put("a.cc", 3L);
            }});
        }});

        Stream<Map<String, Object>> right = Stream.ofAll(new ArrayList<Map<String, Object>>() {{
            add(new HashMap<String, Object>() {{
                put("b.bb", 2);
                put("b.cc", 10L);
            }});
            add(new HashMap<String, Object>() {{
                put("b.bb", 4);
                put("b.cc", 3L);
            }});
        }});

        List<Map<String, Object>> result = left.flatMap(leftRow -> right.map(rightRow -> ObjectTransformer.combineRows(leftRow, rightRow))
                                                                        .filter(row -> true)
                                                                        .orElse(Stream.of(leftRow)))
                                               .toJavaList();
        System.out.println(result);
    }


}
