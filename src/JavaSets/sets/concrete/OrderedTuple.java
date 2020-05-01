package JavaSets.sets.concrete;

import JavaSets.sets.abstraction.AbstractOrderedTuple;
import JavaSets.sets.abstraction.SetFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Alexandre Sallinen (303162)
 * @author Salim Najib (310003)
 */
public class OrderedTuple<T> extends IndexedSet<T, Integer> implements AbstractOrderedTuple<T> {

    /**
     * Classic constructor for an IndexedSet
     * @param t the underlying data
     * @param indexer the function allowing the order
     */
    public OrderedTuple(Collection<T> t, SetFunction<Integer, T> indexer) {
        super(t, indexer);
    }

    /**
     * The constructor from an array, assign each elements to its position in the array
     * @param t the array to convert
     */
    @SafeVarargs
    public OrderedTuple(T... t) {
        super(List.of(t), i -> t[i]);
    }

    /**
     * Constructor allowing to construct an OrderedTuple from any Iterable
     * @param t the iterable to convert
     */
    public OrderedTuple(Iterable<T> t) {
        this(iterableToList(t));
    }

    private static<T> List<T> iterableToList(final Iterable<T> i)
    {
        final List<T> target = new ArrayList<>();
        i.forEach(target::add);
        return target;
    }

    public OrderedTuple(List<T> t) {
        super(t, t::get);
    }

    @Override
    public <U> OrderedTuple<U> image(SetFunction<T, U> f) {
        return new OrderedTuple<>(toList().stream().map(f).collect(Collectors.toList()));
    }
}
