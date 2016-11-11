(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('item-entity', {
            parent: 'entity',
            url: '/item-entity?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ItemEntities'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/item-entity/item-entities.html',
                    controller: 'ItemEntityController',
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
        .state('item-entity-detail', {
            parent: 'entity',
            url: '/item-entity/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ItemEntity'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/item-entity/item-entity-detail.html',
                    controller: 'ItemEntityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ItemEntity', function($stateParams, ItemEntity) {
                    return ItemEntity.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'item-entity',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('item-entity-detail.edit', {
            parent: 'item-entity-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item-entity/item-entity-dialog.html',
                    controller: 'ItemEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ItemEntity', function(ItemEntity) {
                            return ItemEntity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('item-entity.new', {
            parent: 'item-entity',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item-entity/item-entity-dialog.html',
                    controller: 'ItemEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                itemId: null,
                                barcode: null,
                                customerCode: null,
                                callNumber: null,
                                callNumberType: null,
                                itemAvailabilityStatusId: null,
                                copyNumber: null,
                                owningInstitutionId: null,
                                collectionGroupId: null,
                                createdDate: null,
                                createdBy: null,
                                lastUpdatedDate: null,
                                lastUpdatedBy: null,
                                useRestrictions: null,
                                volumePartYear: null,
                                owningInstitutionItemId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('item-entity', null, { reload: 'item-entity' });
                }, function() {
                    $state.go('item-entity');
                });
            }]
        })
        .state('item-entity.edit', {
            parent: 'item-entity',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item-entity/item-entity-dialog.html',
                    controller: 'ItemEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ItemEntity', function(ItemEntity) {
                            return ItemEntity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('item-entity', null, { reload: 'item-entity' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('item-entity.delete', {
            parent: 'item-entity',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item-entity/item-entity-delete-dialog.html',
                    controller: 'ItemEntityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ItemEntity', function(ItemEntity) {
                            return ItemEntity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('item-entity', null, { reload: 'item-entity' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
