package JavaSets.sets.concrete;

import JavaSets.sets.abstraction.AbstractIndexedSet;
import JavaSets.sets.abstraction.AbstractSSet;
import JavaSets.sets.abstraction.SetFunction;

import java.util.Collection;
import java.util.Map;

/**
 * @author Alexandre Sallinen (303162)
 * @author Salim Najib (310003)
 */
public class IndexedSet<T, I> extends SSet<T> implements AbstractIndexedSet<T, I> {

    private final SetFunction<I, T> indexer;

    public IndexedSet(Collection<T> t, SetFunction<I, T> indexer) {
        super(t);
        this.indexer = indexer;
    }

    public IndexedSet(Map<I, T> t) {
        super(t.values());
        this.indexer = t::get;
    }

    public IndexedSet(AbstractSSet<T> t, SetFunction<I, T> indexer) {
        super(t);
        this.indexer = indexer;
    }

    @Override
    public SetFunction<I, T> getIndexer() {
        return indexer;
    }

    @Override
    public <U> IndexedSet<U, I> image(SetFunction<T, U> f) {
        return new IndexedSet<>(f.apply(this), (i -> f.apply(at(i))));
    }

}
