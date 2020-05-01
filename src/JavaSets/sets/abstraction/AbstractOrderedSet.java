package JavaSets.sets.abstraction;

import JavaSets.Preconditions;
import JavaSets.sets.properties.Relation;

import java.util.Set;

/**
 * @author Alexandre Sallinen (303162)
 * @author Salim Najib (310003)
 */
public interface AbstractOrderedSet<T> extends AbstractSSet<T> {

    Relation.Order<T> getComparator();

    default Relation.COMP compare(T t, T u)
    {
        Preconditions.checkArgument(contains(t) && contains(u));
        return getComparator().compare(t, u);
    }

    /**
     *
     * @return the minimal elements according to the order
     */
    default AbstractSSet<T> min()
    {
        return suchThat(p -> getComparator().partialApply(p).apply(this).getData().equals(Set.of(Relation.COMP.LESS)));
    }
    /**
     *
     * @return the maximal elements according to the order
     */
    default AbstractSSet<T> max()
    {
        return suchThat(p -> getComparator().partialApply(p).apply(this).getData().equals(Set.of(Relation.COMP.LESS)));
    }

    /**
     * All the elements greater than t according to the order
     * @param t the element to compare
     * @return all the elements greater than t in this set
     */
    default AbstractSSet<T> moreThan(T t)
    {
        return suchThat(p -> getComparator().partialApply(t).apply(p) == (Relation.COMP.LESS));
    }


    /**
     * All the elements less than t according to the order
     * @param t the element to compare
     * @return all the elements less than t in this set
     */
    default AbstractSSet<T> lessThan(T t)
    {
        return suchThat(p -> getComparator().partialApply(t).apply(p) == (Relation.COMP.GREATER));
    }

    /**
     * All the elements equal to t according to the order
     * @param t the element to compare
     * @return all the elements equal to t in this set
     */
    default AbstractSSet<T> equalsTo(T t)
    {
        return suchThat(p -> getComparator().partialApply(t).apply(p) == (Relation.COMP.EQUAL));
    }
}
