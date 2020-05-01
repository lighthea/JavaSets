package JavaSets.sets.abstraction;

/**
 * @author Alexandre Sallinen (303162)
 * @author Salim Najib (310003)
 */
public interface AbstractIndexedSet<T, I> extends AbstractSSet<T> {

    SetFunction<I, T> getIndexer();
    default T at(I i) {
        return getIndexer().apply(i);
    }

}
