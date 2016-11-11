(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('holdings-entity', {
            parent: 'entity',
            url: '/holdings-entity?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'HoldingsEntities'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/holdings-entity/holdings-entities.html',
                    controller: 'HoldingsEntityController',
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
        .state('holdings-entity-detail', {
            parent: 'entity',
            url: '/holdings-entity/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'HoldingsEntity'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/holdings-entity/holdings-entity-detail.html',
                    controller: 'HoldingsEntityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'HoldingsEntity', function($stateParams, HoldingsEntity) {
                    return HoldingsEntity.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'holdings-entity',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('holdings-entity-detail.edit', {
            parent: 'holdings-entity-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/holdings-entity/holdings-entity-dialog.html',
                    controller: 'HoldingsEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HoldingsEntity', function(HoldingsEntity) {
                            return HoldingsEntity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('holdings-entity.new', {
            parent: 'holdings-entity',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/holdings-entity/holdings-entity-dialog.html',
                    controller: 'HoldingsEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                holdingsId: null,
                                content: null,
                                createdDate: null,
                                lastUpdatedDate: null,
                                lastUpdatedBy: null,
                                owningInstitutionId: null,
                                owningInstitutionHoldingsId: null,
                                isDeleted: null,
                                createdBy: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('holdings-entity', null, { reload: 'holdings-entity' });
                }, function() {
                    $state.go('holdings-entity');
                });
            }]
        })
        .state('holdings-entity.edit', {
            parent: 'holdings-entity',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/holdings-entity/holdings-entity-dialog.html',
                    controller: 'HoldingsEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HoldingsEntity', function(HoldingsEntity) {
                            return HoldingsEntity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('holdings-entity', null, { reload: 'holdings-entity' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('holdings-entity.delete', {
            parent: 'holdings-entity',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/holdings-entity/holdings-entity-delete-dialog.html',
                    controller: 'HoldingsEntityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HoldingsEntity', function(HoldingsEntity) {
                            return HoldingsEntity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('holdings-entity', null, { reload: 'holdings-entity' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
