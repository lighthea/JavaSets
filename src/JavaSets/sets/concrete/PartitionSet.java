package JavaSets.sets.concrete;

import JavaSets.sets.abstraction.AbstractSSet;
import JavaSets.sets.abstraction.AbstractPartitionSet;
import JavaSets.sets.properties.Relation;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Alexandre Sallinen (303162)
 * @author Salim Najib (310003)
 */
public class PartitionSet<T> extends SSet<T> implements AbstractPartitionSet<T> {

    private final IndexedSet<AbstractSSet<T>, T> components;

    /**
     * Construct a partition where each equivalence class will be determined by its belonging Collection
     * @param data the underlying data
     */
    public PartitionSet(Collection<AbstractSSet<T>> data) {
        super(AbstractSSet.unionOf(data).getData());
        components = new IndexedSet<>(data, elem -> data.stream().filter(subset -> subset.contains(elem)).findFirst().orElseThrow());
    }

    /**
     * A partition Set where each equivalence class is determined by the value of the indexer function
     * @param t the underlying data
     */
    public PartitionSet(IndexedSet<AbstractSSet<T>, T> t) {
        super(AbstractSSet.unionOf(t.getData()));
        components = t;
    }

    /**
     * Main constructor, building the equivalence classes from a relation
     * @param data (AbstractMathSet<T>) the underlying data
     * @param areInRelation the equivalence relation used to partition the set
     */
    public PartitionSet(AbstractSSet<T> data, Relation.Equivalence<T> areInRelation) {
        this(data.image( (T elem1) -> areInRelation.partialApply(elem1).preImageOf(true).solveIn(data)).getData());
    }

    /**
     * A single equivalence class Partition Set
     * @param t the MathSet to copy
     */
    public PartitionSet(AbstractSSet<T> t) {
        this(Collections.singletonList(t));
    }

    /**
     * @return The set of all equivalence classes
     */
    @Override
    public AbstractSSet<AbstractSSet<T>> components() {
        return components;
    }
}
