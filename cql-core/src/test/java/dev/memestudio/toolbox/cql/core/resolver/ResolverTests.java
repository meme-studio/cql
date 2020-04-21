package dev.memestudio.toolbox.cql.core.resolver;

import dev.memestudio.toolbox.cql.core.CollectionQL;
import org.junit.Test;

import java.util.*;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
public class ResolverTests {

    @Test
    public void testSimpleSelect() {
        //"SELECT count(aa), aa.heihei, aa.heihei, CASE WHEN a=1 THEN 9 ELSE 1 END FROM a1 aa JOIN bb ON a1.aa = bb.df JOIN (SELECT * FROM dd) cc ON bb.s = cc.fda WHERE a1.bb = 9 AND (a1.cc = 10 AND a1.cc <> 1)"
        List<Map<String, Object>> result =
                CollectionQL.statement("SELECT *, aa FROM a1 WHERE a1.bb = 9 OR a1.cc = 1")
                            .tables(
                                    new HashMap<String, Collection<Map<String, Object>>>() {{
                                        put("a1", new ArrayList<Map<String, Object>>() {{
                                            add(new HashMap<String, Object>() {{
                                                put("bb", 9L);
                                                put("cc", 10L);
                                            }});
                                            add(new HashMap<String, Object>() {{
                                                put("bb", 3L);
                                                put("cc", 3L);
                                            }});
                                        }});
                                    }}
                            )
                            .asList();
        System.out.println(result);
    }

    @Test
    public void testJoinSelect() {
        //"SELECT count(aa), aa.heihei, aa.heihei, CASE WHEN a=1 THEN 9 ELSE 1 END FROM a1 aa JOIN bb ON a1.aa = bb.df JOIN (SELECT * FROM dd) cc ON bb.s = cc.fda WHERE a1.bb = 9 AND (a1.cc = 10 AND a1.cc <> 1)"
        List<Map<String, Object>> result =
                CollectionQL.statement("SELECT * FROM a1 LEFT JOIN b1 ON a1.bb = b1.bb RIGHT JOIN c1 ON b1.bb = c1.bb")
                            .tables(
                                    new HashMap<String, Collection<Map<String, Object>>>() {{
                                        put("a1", new ArrayList<Map<String, Object>>() {{
                                            add(new HashMap<String, Object>() {{
                                                put("bb", 1L);
                                                put("cc", 2L);
                                            }});
                                            add(new HashMap<String, Object>() {{
                                                put("bb", 2L);
                                                put("cc", 3L);
                                            }});
                                        }});
                                        put("b1", new ArrayList<Map<String, Object>>() {{
                                            add(new HashMap<String, Object>() {{
                                                put("bb", 2L);
                                                put("cc", 99L);
                                            }});
                                            add(new HashMap<String, Object>() {{
                                                put("bb", 3L);
                                                put("cc", 323L);
                                            }});
                                        }});
                                        put("c1", new ArrayList<Map<String, Object>>() {{
                                            add(new HashMap<String, Object>() {{
                                                put("bb", 2L);
                                                put("cc", 90000L);
                                            }});
                                            add(new HashMap<String, Object>() {{
                                                put("bb", 3L);
                                                put("cc", 323L);
                                            }});
                                        }});
                                    }}
                            )
                            .asList();
        System.out.println(result);
    }

}
