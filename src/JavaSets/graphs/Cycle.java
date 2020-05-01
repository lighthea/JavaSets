package JavaSets.graphs;

import JavaSets.sets.abstraction.AbstractSSet;
import JavaSets.sets.abstraction.AbstractOrderedTuple;
import JavaSets.sets.concrete.SSet;
import JavaSets.sets.concrete.OrderedTuple;

import java.util.List;
import java.util.Optional;

public abstract class Cycle<T> extends OrderedTuple<T> implements Graph<T, AbstractOrderedTuple<T>> {

    public Cycle(List<T> collect) {
        super(collect);
    }

    public Cycle(AbstractOrderedTuple<T> collect) {
        super(collect);
    }

    /**
     * Gets the set of points linked to given point
     *
     * @param point (T) given point
     * @return (Set < T >) said set
     */
    @Override
    public Optional<AbstractOrderedTuple<T>> getNeighbours(T point) {
        return Optional.of(new OrderedTuple<>(prev(point), next(point)));
    }

    /**
     * Creates a graph ON given set of vertices
     *
     * @param points (Set<T>)
     * @return (Graph < T, U >) some implementation of Graph<T,U>
     */
    @Override
    public Graph<T, ? extends AbstractSSet<T>> on(AbstractSSet<T> points) {
        return new ConcreteGraph<T>(this, edgeSet()).on(points);
    }


    /**
     * A component is a maximally connected subset of a graph
     *
     * @param point the point on which we want the compponent
     * @return the component onn which this point lies
     */
    @Override
    public Graph<T, AbstractOrderedTuple<T>> connectedComponent(T point) {
        return this;
    }

    /**
     * @return the Set of connected components of this graph
     */
    @Override
    public AbstractSSet<Graph<T, AbstractOrderedTuple<T>>> connectedComponents() {
        return SSet.of(this);
    }

    /**
     * @return (MathSet < Link < T > >) getter for immutable set of edges
     */
    @Override
    public AbstractSSet<Link<T>> edgeSet() {
        return image(p -> p.equals(tail()) ? new Link<>(p, head()) : new Link<>(p, next(p)));
    }

    /**
     * @return (MathSet < T >) getter for immutable set of vertices
     */
    @Override
    public AbstractOrderedTuple<T> vertexSet() {
        return this;
    }
}
