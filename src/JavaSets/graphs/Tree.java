package JavaSets.graphs;

import JavaSets.Preconditions;
import JavaSets.sets.abstraction.AbstractSSet;
import JavaSets.sets.abstraction.AbstractOrderedTuple;
import JavaSets.sets.abstraction.SetFunction;
import JavaSets.sets.concrete.SSet;
import JavaSets.sets.concrete.OrderedTuple;
import JavaSets.sets.concrete.PartitionSet;
import JavaSets.sets.concrete.PointedSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Alexandre Sallinen (303162)
 * @author Salim Najib (310003)
 */
public final class Tree<V> extends PointedSet<GraphNode<V>> implements Graph<GraphNode<V>, Tree<V>> {

    private final AbstractSSet<GraphNode<V>> nodes;
    private final int maxDepth;

    /**
     * Constructor of Tree using parameter Nodes' inner hierarchy to construct the directed graph
     * Supposes a connected tree AKA not a forest
     *
     * @param t (Collection<Node<T>>)
     * @throws IllegalArgumentException if given leaves aren't all connected to the same root
     */
    public Tree(Collection<AbstractSSet<GraphNode<V>>> t) {
        this(AbstractSSet.unionOf(t));
    }

    /**
     * Main Constructor of Tree using parameter Nodes' inner hierarchy to construct the directed graph
     * Supposes a connected tree AKA not a forest
     *
     * @param nodes (Collection<Node<T>>)
     * @throws IllegalArgumentException if given leaves aren't all connected to the same root
     */
    public Tree(AbstractSSet<GraphNode<V>> nodes) {
        this(nodes, true, nodes.minOf(GraphNode::getDepth), nodes.maxOf(GraphNode::getDepth).getDepth());
    }

    public Tree(AbstractSSet<GraphNode<V>> nodes, boolean securityChecksActivated) {
        this(nodes, securityChecksActivated, nodes.minOf(GraphNode::getDepth), nodes.maxOf(GraphNode::getDepth).getDepth());
    }

    public Tree()
    {
        super(emptySet(), null);
        this.maxDepth = -1;
        this.nodes = emptySet();
    }

    private Tree(AbstractSSet<GraphNode<V>> nodes, boolean securityChecksActivated, GraphNode<V> root, int maxDepth) {
        super(nodes, root);
        if (securityChecksActivated) {
            Preconditions.checkArgument(nodes.image(
                    node -> {
                        final Path<GraphNode<V>> pathN = node.hierarchy();
                        return pathN.at(pathN.cardinality() - 1);
                    })
                    .cardinality() == 1);
        }

        this.maxDepth = maxDepth;
        this.nodes = nodes;
    }

    public static <V> Tree<V> emptyTree()
    {
        return new Tree<>();
    }

    public Tree<V> add (final Path<GraphNode<V>> p)
    {
        return new Tree<>(union(p));
    }

    @Override
    public Optional<Tree<V>> getNeighbours(GraphNode<V> point) {
        return getChildren(point.getParent().orElseThrow());
    }

    /**
     *
     * @param point (T)
     * @return The points for which point is their parent
     */
    public Optional<Tree<V>> getChildren(GraphNode<V> point) {
        final AbstractSSet<GraphNode<V>> children = getNodesAtDepth(point.getDepth() + 1).suchThat(point::isParentOf);
        return children.isEmpty() ? Optional.empty() : Optional.of(new Tree<>(children, false, point, maxDepth));
    }

    @Override
    public OrderedTuple<GraphNode<V>> flow(SetFunction<Tree<V>, GraphNode<V>> chooser, GraphNode<V> point) {
        final List<GraphNode<V>> flowList = flowRecur(chooser, chooser.apply(getChildren(point).orElse(null)), new ArrayList<>());
        flowList.add(point);
        Collections.reverse(flowList);
        return new OrderedTuple<>(flowList);
    }

    private List<GraphNode<V>> flowRecur(Function<Tree<V>, GraphNode<V>> chooser, GraphNode<V> point, List<GraphNode<V>> workList) {
        if (getChildren(point).isEmpty()) {
            workList.add(point);
            return workList;
        }
        flowRecur(chooser, chooser.apply(getChildren(point).get()), workList);
        workList.add(point);
        return workList;
    }

    /**
     *
     * @param point (T)
     * @return the tree that has for root point and points the same as the current tree
     */
    public Tree<V> subtreeAtPoint(GraphNode<V> point) {
        Preconditions.checkArgument(contains(point));
        return new Tree<>(nodes.suchThat(node -> node.getDepth() >= point.getDepth() && GraphNode.areRelated(node, point)),
                false, point, maxDepth);
    }
    /**
     * Finds the shortest path between two nodes
     *
     * @param node1 (GraphNode<V>) from where the path should start
     * @param node2 (GraphNode<V>) where it should end
     * @return (Path<GraphNode<V>>) the shortest path in the graph from point a to b
     * @throws IllegalArgumentException if either of the two nodes is not in the tree
     */
    public Optional<AbstractOrderedTuple<GraphNode<V>>> findPathBetween(GraphNode<V> node1, GraphNode<V> node2) {
        Preconditions.checkArgument(contains(node1) && contains(node2));

        final Path<GraphNode<V>> nodeOneHierarchy = node1.hierarchy();
        final Path<GraphNode<V>> nodeTwoHierarchy = node2.hierarchy();
        final AbstractSSet<GraphNode<V>> aut = nodeOneHierarchy.intersection(nodeTwoHierarchy);

        if (aut.contains(node1) || aut.contains(node2))
        {
            return nodeTwoHierarchy.findPathBetween(node1, node2);
        } else {

            final GraphNode<V> anchor = aut.maxOf(GraphNode::getDepth);
            return  Optional.of(new Path<>(nodeOneHierarchy.findPathBetween(node1, anchor).orElseThrow())
                    .add(new Path<>(nodeTwoHierarchy.findPathBetween(anchor, node2).orElseThrow()).reverse()));
        }
    }

    @Override
    public Graph<GraphNode<V>, ? extends AbstractSSet<GraphNode<V>>> on(AbstractSSet<GraphNode<V>> points) {
        return new ConcreteGraph<>(new PartitionSet<>(intersection(points),
                (a, b) -> points.containsSet(new OrderedTuple<>(findPathBetween(a,b).orElseThrow()))),
                edgeSet().suchThat(points::containsSet));
    }

    @Override
    public Graph<GraphNode<V>, Tree<V>> connectedComponent(GraphNode<V> point) {
        return this;
    }

    @Override
    public AbstractSSet<Graph<GraphNode<V>, Tree<V>>> connectedComponents() {
        return new SSet<>(Collections.singleton(this));
    }

    @Override
    public AbstractSSet<Link<GraphNode<V>>> edgeSet() {
        final AbstractSSet<GraphNode<V>> nonRoots = nodes.suchThat(node -> !node.equals(getElementOrThrow()));
        return (nonRoots.cardinality() <= 1) ? emptySet() : nonRoots.image(n -> new Link<>(n, n.getParent().orElse(null)));
    }

    @Override
    public Tree<V> vertexSet() {
        return this;
    }

    /**
     * @return the points that have a parent but no children - assumes that, for each node, all its children are in the
     *         tree (ie assuming that this tree contains all nodes alpha verifying Node.areRelated(this.root, alpha)).
     */
    public AbstractSSet<GraphNode<V>> getLeaves() {
        return nodes.suchThat(node -> !node.isParent());
    }

    /**
     * @param targetDepth the wanted depth of nodes
     * @return all nodes sharing this depth
     */
    public AbstractSSet<GraphNode<V>> getNodesAtDepth(int targetDepth) {
        return suchThat(node -> node.getDepth() == targetDepth);
    }

    /**
     * @return the root node to which every node should be linked
     */
    public GraphNode<V> getRoot() {
        return getElementOrThrow();
    }

    public int getTotalDepth() {
        return maxDepth - getElementOrThrow().getDepth();
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public int getMinDepth() {
        return getElementOrThrow().getDepth();
    }
}
