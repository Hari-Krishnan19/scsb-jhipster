(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .controller('InstitutionEntityDetailController', InstitutionEntityDetailController);

    InstitutionEntityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'InstitutionEntity'];

    function InstitutionEntityDetailController($scope, $rootScope, $stateParams, previousState, entity, InstitutionEntity) {
        var vm = this;

        vm.institutionEntity = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('scsbhipsterApp:institutionEntityUpdate', function(event, result) {
            vm.institutionEntity = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
