package es.icarto.gvsig.commons.queries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.TreeSet;

import org.junit.Test;

public class TestField {

    @Test
    public void testCompareTo() {
	Field f1 = new Field("a", "b");
	Field f2 = new Field("a", "a");
	Field f3 = new Field("a", "");
	Field f4 = new Field("a", " ");

	TreeSet<Field> treeSet = new TreeSet<Field>();
	treeSet.add(f1);
	treeSet.add(f2);
	treeSet.add(f3);
	treeSet.add(f4);

	assertEquals(f3, treeSet.pollFirst());
	assertEquals(f4, treeSet.pollFirst());
	assertEquals(f2, treeSet.pollFirst());
	assertEquals(f1, treeSet.pollFirst());
	assertNull(treeSet.pollFirst());

    }
}
