'use strict';

describe('Controller Tests', function() {

    describe('HoldingsEntity Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockHoldingsEntity, MockBibliographicEntity, MockItemEntity, MockInstitutionEntity;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockHoldingsEntity = jasmine.createSpy('MockHoldingsEntity');
            MockBibliographicEntity = jasmine.createSpy('MockBibliographicEntity');
            MockItemEntity = jasmine.createSpy('MockItemEntity');
            MockInstitutionEntity = jasmine.createSpy('MockInstitutionEntity');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'HoldingsEntity': MockHoldingsEntity,
                'BibliographicEntity': MockBibliographicEntity,
                'ItemEntity': MockItemEntity,
                'InstitutionEntity': MockInstitutionEntity
            };
            createController = function() {
                $injector.get('$controller')("HoldingsEntityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'scsbhipsterApp:holdingsEntityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
