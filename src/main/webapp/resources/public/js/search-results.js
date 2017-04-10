angular.module('SearchResultsApp', ['ngCookies', 'ServicesModule', 'spring-security-csrf-token-interceptor'])
    .controller('SearchResultsCtrl', ['$scope', '$cookieStore', '$http', 'DriveService',
        function($scope, $cookieStore, $http, DriveService) {
            $scope.greeting = "Privet Erik!";
            $scope.destination = $cookieStore.get('to');
            $scope.source = $cookieStore.get('from');


            var searchDriveDTO = {from: $scope.source, to: $scope.destination};
            $http({
                method: 'POST',
                url: '/drive/find',
                data: searchDriveDTO,
                headers: {
                    "Content-Type": "application/json;charset=UTF-8"
                }
            })
            .then(function (response) {
                if (response.status == 200) {
                    console.log(response.data);
                    $("#content").html(response.data.drives[0].driveFrom);
                }
            });

            //$http.get("/drive/find?from=" + $scope.source + "&to=" + $scope.destination, {header : {'Content-Type' : 'application/json; charset=UTF-8'}})
            //    .then(function(response) {
            //        if(response.status == 200){
            //            $("#content").html(response.data.drives[0].driveFrom);
            //        }
            //    });
        }]
);