package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 19 Dec 2017
 */
public class GraphNodeRenderer {

    @Nonnull
    private final EntityHierarchyNodeRenderer renderer;

    @Inject
    public GraphNodeRenderer(@Nonnull EntityHierarchyNodeRenderer renderer) {
        this.renderer = checkNotNull(renderer);
    }

    /**
     * Render the specified entity into a {@link GraphNode} whose user object
     * is and {@link EntityHierarchyNode}.
     * @param entity The entity to be rendered.
     * @param hierarchyProvider A hierarchy that is used to provide information.
     */
    public GraphNode<EntityHierarchyNode> toGraphNode(@Nonnull OWLEntity entity,
                                                      @Nonnull HierarchyProvider<OWLEntity> hierarchyProvider) {
        return new GraphNode<>(renderer.render(entity), hierarchyProvider.getChildren(entity).isEmpty());
    }
}
