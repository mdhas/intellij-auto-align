import aligner.predicates.*;
import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PredicatesTests {

    @Test
    public void startsWithPredicate() {
        Predicate<String> predicate = new StartsWithPredicate("hello");
        assertTrue(predicate.test("hello world"));
        assertTrue(predicate.test("     hello world"));
        assertTrue(predicate.test("\t\t\thello world"));
        assertTrue(predicate.test("\nhello world"));
        assertFalse(predicate.test("qewr34"));
    }

    @Test
    public void constainsPredicate() {
        Predicate<String> predicate;

        predicate = new ContainsPredicate("hello");
        assertTrue(predicate.test("this is some text. hello"));
        assertFalse(predicate.test("this is some text"));
    }

    @Test
    public void predicateOR() {
        Predicate<String> predicate;

        predicate = Predicates.or(new StartsWithPredicate("hello"), new StartsWithPredicate("h45gf"));
        assertTrue(predicate.test("hello"));

        predicate = Predicates.or(new StartsWithPredicate("2134rt"),new StartsWithPredicate("h45gf"));
        assertFalse(predicate.test("hello"));
    }

    @Test
    public void predicateAND() {
        Predicate<String> predicate;

        predicate = Predicates.and(new StartsWithPredicate("hello"), new StartsWithPredicate("h"), new StartsWithPredicate("hell"));
        assertTrue(predicate.test("hello"));


        predicate = Predicates.and(new StartsWithPredicate("hello"), new StartsWithPredicate("h"), new StartsWithPredicate("ell"));
        assertFalse(predicate.test("hello"));
    }

    @Test
    public void predicateNOT() {
        Predicate<String> predicate;

        predicate = Predicates.not(new StartsWithPredicate("ll032"));
        assertTrue(predicate.test("hello"));


        predicate = Predicates.not(new StartsWithPredicate("hell"));
        assertFalse(predicate.test("hello"));
    }
}
