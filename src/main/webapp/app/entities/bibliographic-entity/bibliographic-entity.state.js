(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bibliographic-entity', {
            parent: 'entity',
            url: '/bibliographic-entity?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BibliographicEntities'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bibliographic-entity/bibliographic-entities.html',
                    controller: 'BibliographicEntityController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('bibliographic-entity-detail', {
            parent: 'entity',
            url: '/bibliographic-entity/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BibliographicEntity'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bibliographic-entity/bibliographic-entity-detail.html',
                    controller: 'BibliographicEntityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'BibliographicEntity', function($stateParams, BibliographicEntity) {
                    return BibliographicEntity.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bibliographic-entity',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bibliographic-entity-detail.edit', {
            parent: 'bibliographic-entity-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bibliographic-entity/bibliographic-entity-dialog.html',
                    controller: 'BibliographicEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BibliographicEntity', function(BibliographicEntity) {
                            return BibliographicEntity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bibliographic-entity.new', {
            parent: 'bibliographic-entity',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bibliographic-entity/bibliographic-entity-dialog.html',
                    controller: 'BibliographicEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                bibliographicId: null,
                                content: null,
                                owningInstitutionId: null,
                                createdDate: null,
                                createdBy: null,
                                lastUpdatedDate: null,
                                lastUpdatedBy: null,
                                owningInstitutionBibId: null,
                                isDeleted: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bibliographic-entity', null, { reload: 'bibliographic-entity' });
                }, function() {
                    $state.go('bibliographic-entity');
                });
            }]
        })
        .state('bibliographic-entity.edit', {
            parent: 'bibliographic-entity',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bibliographic-entity/bibliographic-entity-dialog.html',
                    controller: 'BibliographicEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BibliographicEntity', function(BibliographicEntity) {
                            return BibliographicEntity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bibliographic-entity', null, { reload: 'bibliographic-entity' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bibliographic-entity.delete', {
            parent: 'bibliographic-entity',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bibliographic-entity/bibliographic-entity-delete-dialog.html',
                    controller: 'BibliographicEntityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BibliographicEntity', function(BibliographicEntity) {
                            return BibliographicEntity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bibliographic-entity', null, { reload: 'bibliographic-entity' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
