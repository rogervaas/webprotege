package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyRootsAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyRootsResult;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 30 Nov 2017
 */
public class GetHierarchyRootsActionHandler extends AbstractProjectActionHandler<GetHierarchyRootsAction, GetHierarchyRootsResult> {

    @Nonnull
    private final HierarchyProviderMapper hierarchyProviderMapper;

    @Nonnull
    private final EntityHierarchyNodeRenderer renderer;

    @Inject
    public GetHierarchyRootsActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull HierarchyProviderMapper hierarchyProviderMapper,
                                          @Nonnull EntityHierarchyNodeRenderer renderer) {
        super(accessManager);
        this.hierarchyProviderMapper = checkNotNull(hierarchyProviderMapper);
        this.renderer = checkNotNull(renderer);
    }

    @Nonnull
    @Override
    public Class<GetHierarchyRootsAction> getActionClass() {
        return GetHierarchyRootsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetHierarchyRootsResult execute(@Nonnull GetHierarchyRootsAction action, @Nonnull ExecutionContext executionContext) {
        HierarchyId hierarchyId = action.getHierarchyId();
        return hierarchyProviderMapper.getHierarchyProvider(hierarchyId).map(hierarchyProvider -> {
            Set<OWLEntity> roots = hierarchyProvider.getRoots();
            List<GraphNode<EntityHierarchyNode>> rootNodes =
                    roots.stream()
                         .map(rootEntity -> {
                             EntityHierarchyNode rootNode = renderer.render(rootEntity);
                             return new GraphNode<>(rootNode, hierarchyProvider.getChildren(rootEntity).isEmpty());
                         })
                         .sorted(comparing(node -> node.getUserObject().getBrowserText()))
                         .collect(toList());
            return new GetHierarchyRootsResult(rootNodes);
        }).orElse(new GetHierarchyRootsResult(emptyList()));
    }
}
