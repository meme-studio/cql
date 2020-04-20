package dev.memestudio.toolbox.cql.core.resolver;

import lombok.SneakyThrows;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class ResolverTests {

    @SneakyThrows
    @Test
    public void testSimpleSelect() {
        //Statement parse1 = CCJSqlParserUtil.parse("SELECT count(aa), aa.heihei, aa.heihei, CASE WHEN a=1 THEN 9 ELSE 1 END FROM a1 aa JOIN bb ON a1.aa = bb.df JOIN (SELECT * FROM dd) cc ON bb.s = cc.fda WHERE a1.bb = 9 AND (a1.cc = 10 AND a1.cc <> 1)");
        Statement parse1 = CCJSqlParserUtil.parse("SELECT *, CASE WHEN aa= aa THEN 1 ELSE 0 END, aa FROM a1 WHERE a1.bb = 9 OR a1.cc <> 1");
        UnaryOperator<ResolvingContext> parse2 = Resolvers.resolve(parse1);
        ResolvingContext resolvingContext = new ResolvingContext();
        Map<String, Collection<Map<String, Object>>> schemas = new HashMap<>();
        ArrayList<Map<String, Object>> value = new ArrayList<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("bb", 9L);
        map1.put("cc", 10L);
        value.add(map1);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("bb", 3L);
        map2.put("cc", 3L);
        value.add(map2);
        schemas.put("a1", value);
        resolvingContext.setSchemas(schemas);
        ResolvingContext apply = parse2.apply(resolvingContext);
        System.out.println(apply.getResult().toJavaList());
    }

}
