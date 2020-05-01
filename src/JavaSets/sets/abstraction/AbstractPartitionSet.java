package JavaSets.sets.abstraction;

import JavaSets.Preconditions;

/**
 * @author Alexandre Sallinen (303162)
 * @author Salim Najib (310003)
 */
public interface AbstractPartitionSet<T> extends AbstractSSet<T> {
     /**
     *
     * @param t an element of this set
     * @return The equivalence class in which t lies
     */
     default AbstractSSet<T> component(T t) {
        Preconditions.checkArgument(contains(t));
        return components().suchThat(p -> p.contains(t)).getElementOrThrow();
    }
    /**
     *
     * @return The set of all equivalence classes
     */
    AbstractSSet<AbstractSSet<T>> components();

    /**
     * An element in a given component
     * @param component the component in which to get a representant
     * @return An element in this equivalence class
     */
    default T representing(AbstractSSet<T> component)
    {
        Preconditions.checkArgument(components().contains(component));
        return component.getElementOrThrow();
    }

    /**
     *
     * @return A set of representant for each equivalence class
     */
    default AbstractSSet<T> representants()
    {
        return components().image(this::representing);
    }

    /**
     *
     * @return the number of Equivalence classes
     */
    default int numberOfComponents(){return components().cardinality();}

}
