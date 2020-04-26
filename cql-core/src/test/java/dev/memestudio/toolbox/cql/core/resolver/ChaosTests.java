package dev.memestudio.toolbox.cql.core.resolver;

import io.vavr.API;
import io.vavr.collection.Stream;
import lombok.SneakyThrows;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.junit.Test;

@SuppressWarnings("SqlResolve")
public class ChaosTests {


    @SneakyThrows
    @Test
    public void testCCJSqlParserUtil() {
        //Statement parsed = CCJSqlParserUtil.parse("SELECT count(*), sum(DISTINCT b), ifnull(a, 11), * FROM a LEFT JOIN b ON ii = cc GROUP BY abs(a % 10) HAVING sum(b)");
        Statement parsed = CCJSqlParserUtil.parse("SELECT CASE a WHEN 1 THEN 'fa' ELSE 'da' END FROM a WHERE (a, c) > '2019-01-01'");
        System.out.println(parsed);
    }

    @Test
    public void testStreamCopy() {
        Stream<Integer> stream1 = Stream.ofAll(1, 2, 3);
        Stream<Integer> stream2 = Stream.ofAll(1, 2, 3).toStream();
        System.out.println(stream2.toJavaList());
        System.out.println(stream1.toJavaList());

    }

    @Test
    public void testVavrMap() {
        io.vavr.collection.Map<Object, Object> put = API.Map().put(1, 1);
        System.out.println(put);

    }


}
