angular.module('newUserApp', ['common', 'spring-security-csrf-token-interceptor', 'editableTableWidgets'])
    .controller('NewUserCtrl', ['$scope', '$http', function ($scope, $http) {

        $scope.uploadFile = function(event){
            var files = event.target.files;
            $scope.vm.profilePicture = files[0];
        };

        $scope.createUser = function () {
            $scope.vm.submitted = true;

            if ($scope.form.$invalid) {
                return;
            }

            var postData = {
                username: $scope.vm.username,
                plainTextPassword: $scope.vm.password,
                email: $scope.vm.email
            };
            var fd = new FormData();
            fd.append('data', new Blob([angular.toJson(postData)], {
                type: "application/json"
            }));
            fd.append("profilePicture", $scope.vm.profilePicture);

            $http.post("/user", fd, {
                transformRequest : angular.identity,
                headers : {
                    'Content-Type' : undefined
                }
            })
            .success(function(data) {
                window.location.href = "/resources/public/thankyou.html";
            })
            .error(function(data) {
                $scope.vm.errorMessages.push({description: errorMessage});
            });
        }
    }])
    .directive('customOnChange', function() {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var onChangeHandler = scope.$eval(attrs.customOnChange);
                element.bind('change', onChangeHandler);
            }
        };
    });