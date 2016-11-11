(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .controller('ItemEntityDialogController', ItemEntityDialogController);

    ItemEntityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ItemEntity', 'HoldingsEntity'];

    function ItemEntityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ItemEntity, HoldingsEntity) {
        var vm = this;

        vm.itemEntity = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.holdingsentities = HoldingsEntity.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.itemEntity.id !== null) {
                ItemEntity.update(vm.itemEntity, onSaveSuccess, onSaveError);
            } else {
                ItemEntity.save(vm.itemEntity, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('scsbhipsterApp:itemEntityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.lastUpdatedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
