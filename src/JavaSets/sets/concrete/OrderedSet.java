package JavaSets.sets.concrete;

import JavaSets.sets.abstraction.AbstractOrderedSet;
import JavaSets.sets.properties.Relation;

/**
 * @author Alexandre Sallinen (303162)
 * @author Salim Najib (310003)
 */
public final class OrderedSet<T> extends SSet<T> implements AbstractOrderedSet<T> {

    private final Relation.Order<T> comparator;

    /**
     * The main constructor
     * @param m the underlying MathSet
     * @param comparator the order associated with this set
     */
    public OrderedSet(SSet<T> m, Relation.Order<T> comparator) {
        super(m);
        this.comparator = comparator;
    }

    @Override
    public Relation.Order<T> getComparator() {
        return comparator;
    }

}
