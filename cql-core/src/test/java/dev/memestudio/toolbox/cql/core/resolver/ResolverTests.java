package dev.memestudio.toolbox.cql.core.resolver;

import dev.memestudio.toolbox.cql.core.CollectionQL;
import dev.memestudio.toolbox.cql.core.schema.Datasource;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.junit.Test;

import static io.vavr.API.List;
import static io.vavr.API.Map;


@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
public class ResolverTests {

    @Test
    public void testSimpleSelect() {
        //"SELECT count(aa), aa.heihei, aa.heihei, CASE WHEN a=1 THEN 9 ELSE 1 END FROM a1 aa JOIN bb ON a1.aa = bb.df JOIN (SELECT * FROM dd) cc ON bb.s = cc.fda WHERE a1.bb = 9 AND (a1.cc = 10 AND a1.cc <> 1)"
        List<Map<String, Object>> result =
                CollectionQL.statement("SELECT * FROM a1 WHERE (a1.cc, a1.bb) > (4, 2)")
                            .tables(
                                    Datasource.from(
                                            Map("a1", List(
                                                    Map(
                                                            "bb", 3L,
                                                            "cc", 10L
                                                    ),
                                                    Map(
                                                            "bb", 3L,
                                                            "cc", 3L
                                                    )
                                            )))
                            )
                            .asList();
        System.out.println(result);
    }

    @Test
    public void testSubSelect() {
        //"SELECT count(aa), aa.heihei, aa.heihei, CASE WHEN a=1 THEN 9 ELSE 1 END FROM a1 aa JOIN bb ON a1.aa = bb.df JOIN (SELECT * FROM dd) cc ON bb.s = cc.fda WHERE a1.bb = 9 AND (a1.cc = 10 AND a1.cc <> 1)"
        List<Map<String, Object>> result =
                CollectionQL.statement("SELECT * FROM (SELECT a1.bb FROM a1) aaa WHERE bb = 3")
                            .tables(
                                    Datasource.from(
                                            Map("a1", List(
                                                    Map(
                                                            "bb", 9L,
                                                            "cc", 10L
                                                    ),
                                                    Map(
                                                            "bb", 3L,
                                                            "cc", 3L
                                                    )
                                            ))
                                    )
                            )
                            .asList();
        System.out.println(result);
    }

    @Test
    public void testJoinSelect() {
        //"SELECT count(aa), aa.heihei, aa.heihei, CASE WHEN a=1 THEN 9 ELSE 1 END FROM a1 aa JOIN bb ON a1.aa = bb.df JOIN (SELECT * FROM dd) cc ON bb.s = cc.fda WHERE a1.bb = 9 AND (a1.cc = 10 AND a1.cc <> 1)"
        List<Map<String, Object>> result =
                CollectionQL.statement("SELECT * FROM a1 LEFT JOIN b1 ON a1.bb = b1.bb RIGHT JOIN c1 ON a1.bb = bbb")
                            .tables(
                                    Datasource.from(
                                            Map(
                                                    "a1", List(
                                                            Map(
                                                                    "bb", 9L,
                                                                    "cc", 10L
                                                            ),
                                                            Map(
                                                                    "bb", 3L,
                                                                    "cc", 3L
                                                            )
                                                    ),
                                                    "b1", List(
                                                            Map(
                                                                    "bb", 9L,
                                                                    "cc", 100L
                                                            ),
                                                            Map(
                                                                    "bb", 31L,
                                                                    "cc", 31L
                                                            )
                                                    ),
                                                    "c1", List(
                                                            Map(
                                                                    "bbb", 9L,
                                                                    "cc", 101L
                                                            ),
                                                            Map(
                                                                    "bb", 3L,
                                                                    "cc", 3L
                                                            )
                                                    )
                                            ))
                            )
                            .asList();
        System.out.println(result);
    }

    @Test
    public void testOrderBy() {
        //"SELECT count(aa), aa.heihei, aa.heihei, CASE WHEN a=1 THEN 9 ELSE 1 END FROM a1 aa JOIN bb ON a1.aa = bb.df JOIN (SELECT * FROM dd) cc ON bb.s = cc.fda WHERE a1.bb = 9 AND (a1.cc = 10 AND a1.cc <> 1)"
        List<Map<String, Object>> result =
                CollectionQL.statement("SELECT * FROM a1 ORDER BY aa ")
                            .tables(
                                    Datasource.from(
                                            Map(
                                                    "a1", List(
                                                            Map(
                                                                    "bb", 9L,
                                                                    "cc", 10L,
                                                                    "dd", 1L
                                                            ),
                                                            Map(
                                                                    "bb", 3L,
                                                                    "cc", 3L,
                                                                    "dd", 5L
                                                            ),
                                                            Map(
                                                                    "bb", 4L,
                                                                    "cc", 3L,
                                                                    "dd", 99L
                                                            )
                                                    )
                                            ))
                            )
                            .asList();
        System.out.println(result);
    }

}
