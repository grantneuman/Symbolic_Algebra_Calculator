package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteStress extends TestDoubleLinkedList {
    @Test(timeout=15 * SECOND)
    public void testForAddingDeletingFromLastEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20000; i++) {
            list.add(i);
        }

        for (int i = 0; i < 20000; i++){
            list.add(1);
            list.delete(20000);
        }
    }
    
    @Test(timeout=15 * SECOND)
    public void testDeletingBFEndEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int c = 7654321;
        for (int i = 0; i < c; i++) {
            list.add(i);
        }
        
        for (int i = 0; i < c - 1; i++){
            list.delete(list.size() - 2);
        }
        assertEquals(list.size(), 1);
        this.assertListMatches(new Integer[] {7654320}, list);
    }
}