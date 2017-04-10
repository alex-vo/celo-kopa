
angular.module('editableTableWidgets', [])
    .directive('ttErrorMessages', function() {
        //TODO disappear (x)
        return {
            restrict: 'E',
            link: function(scope, element, attrs) {
                scope.extraStyles = attrs.extraStyles;
            },
            templateUrl: '/resources/public/partials/error-messages.html'
        }
    })
    .directive('ttSuccessMessages', function() {
        return {
            restrict: 'E',
            link: function(scope, element, attrs) {
                scope.extraStyles = attrs.extraStyles;
            },
            templateUrl: '/resources/public/partials/success-messages.html'
        }
    })
    .directive('ttEditableRow', function() {
        return {
            scope:{
                rowValue: '='
            },
            controller: ['$scope', function($scope) {
                $scope.rowState = {
                    new: false
                }
            }]
        };
    })
    .directive('ttEditableCell', function() {
        return {
            scope: {
                value: '=',
                isNew: '='
            },
            transclude:true,
            replace:true,
            templateUrl: '/resources/partials/editable-cell.html',
            controller: ['$scope', function($scope) {

                $scope.cellState = {
                    editMode: false
                };

                $scope.onValueChanged = function (newValue) {
                    $scope.value = newValue;
                };

                $scope.edit = function() {
                    $scope.cellState.editMode = true;
                };

            }]
        };
    })
    .directive('ttCellField', ['$timeout', function($timeout) {
        return {

            controller: ['$scope', '$element', function($scope, $element) {

                $scope.$watch('$parent.cellState.editMode', function(isEditModeOn) {
                    if (isEditModeOn == undefined) return;

                    if (isEditModeOn) {
                        $timeout(function() {
                            $element[0].focus();
                            $element[0].select();
                        });
                    }
                });

                $element.on('blur', function () {
                    $timeout(function () {
                        $scope.$parent.cellState.editMode = false;
                        $scope.$parent.onValueChanged($element[0].value);
                    });
                });

                $element.on('keydown', function($event) {
                    if ($event.keyCode == 13) {
                        $event.preventDefault();
                        $event.stopPropagation();
                        $element[0].blur();
                    }
                });

            }]
        };
    }])
    .directive('ttNumericField', function () {
        return {
            controller: ['$element', function ($element) {
                $element.on('keydown', function ($event) {
                    var keyCode = $event.keyCode;
                    if (!(keyCode >= 48 && keyCode <= 57) && !(keyCode == 8) && !(keyCode >= 37 && keyCode <= 38)) {
                        $event.preventDefault();
                        $event.stopPropagation();
                    }
                });
            }]
        };
    });