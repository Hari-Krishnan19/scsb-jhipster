(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('institution-entity', {
            parent: 'entity',
            url: '/institution-entity?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'InstitutionEntities'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/institution-entity/institution-entities.html',
                    controller: 'InstitutionEntityController',
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
        .state('institution-entity-detail', {
            parent: 'entity',
            url: '/institution-entity/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'InstitutionEntity'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/institution-entity/institution-entity-detail.html',
                    controller: 'InstitutionEntityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'InstitutionEntity', function($stateParams, InstitutionEntity) {
                    return InstitutionEntity.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'institution-entity',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('institution-entity-detail.edit', {
            parent: 'institution-entity-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/institution-entity/institution-entity-dialog.html',
                    controller: 'InstitutionEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstitutionEntity', function(InstitutionEntity) {
                            return InstitutionEntity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('institution-entity.new', {
            parent: 'institution-entity',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/institution-entity/institution-entity-dialog.html',
                    controller: 'InstitutionEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                institutionId: null,
                                institutionCode: null,
                                institutionName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('institution-entity', null, { reload: 'institution-entity' });
                }, function() {
                    $state.go('institution-entity');
                });
            }]
        })
        .state('institution-entity.edit', {
            parent: 'institution-entity',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/institution-entity/institution-entity-dialog.html',
                    controller: 'InstitutionEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InstitutionEntity', function(InstitutionEntity) {
                            return InstitutionEntity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('institution-entity', null, { reload: 'institution-entity' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('institution-entity.delete', {
            parent: 'institution-entity',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/institution-entity/institution-entity-delete-dialog.html',
                    controller: 'InstitutionEntityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InstitutionEntity', function(InstitutionEntity) {
                            return InstitutionEntity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('institution-entity', null, { reload: 'institution-entity' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
