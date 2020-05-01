package JavaSets.sets.properties;

import JavaSets.Preconditions;
import JavaSets.sets.concrete.SSet;
import JavaSets.sets.abstraction.SetFunction;

import static java.lang.Integer.signum;

@FunctionalInterface
public interface Relation<T, U> {
    /**
     *
     * @param t (T)
     * @param u (T)
     * @return Whether two elements satisfy the relation
     */
    U areInRelation(T t, T u);

    /**
     * Allows to compare an element to a set
     * @param t (T)
     * @return the set of possible value to the relation
     */
    default SetFunction<T, U> partialApply(T t)
    {
        return l -> areInRelation(t, l);
    }

    @FunctionalInterface
    interface Equivalence<T> extends Relation<T, Boolean>{
        default boolean areInRelation(SSet<T> pair)
        {
            Preconditions.checkArgument(pair.cardinality() == 2);
            T t = pair.getElementOrThrow();
            return areInRelation(t, pair.minus(t).getElementOrThrow());
        }
    }

    enum COMP {
        LESS, EQUAL, GREATER;
        public static COMP of(int i)
        {
            switch (signum(i))
            {
                case -1 : return LESS;
                case 0 : return EQUAL;
                case 1 : return GREATER;
            }
            throw new IllegalArgumentException();
        }
    }

    @FunctionalInterface
    interface Order<T> extends Relation<T, COMP>
    {
        default COMP compare(T t, T u)
        {
            return areInRelation(t, u);
        }
    }
}
